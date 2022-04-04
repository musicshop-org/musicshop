package application;

import domain.Invoice;
import domain.InvoiceLineItem;
import domain.repositories.InvoiceRepository;
import infrastructure.InvoiceRepositoryImpl;
import jakarta.transaction.Transactional;
import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvoiceServiceImpl extends UnicastRemoteObject implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl() throws RemoteException {
        this.invoiceRepository = new InvoiceRepositoryImpl();
    }

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) throws RemoteException {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    @Override
    public Optional<InvoiceDTO> findInvoiceById(InvoiceId invoiceId) throws RemoteException {

        Optional<Invoice> result = invoiceRepository.findInvoiceById(invoiceId);

        return result.map(invoice -> new InvoiceDTO(
                invoice.getInvoiceId(),
                invoice.getInvoiceLineItems()
                        .stream().map(invoiceLineItem -> new InvoiceLineItemDTO(
                                invoiceLineItem.getMediumType(),
                                invoiceLineItem.getName(),
                                invoiceLineItem.getQuantity(),
                                invoiceLineItem.getPrice()))
                        .collect(Collectors.toList()),
                invoice.getPaymentMethod(),
                invoice.getDate()
        ));
    }

    @Transactional
    @Override
    public void createInvoice(InvoiceDTO invoiceDTO) throws RemoteException {

        List <InvoiceLineItem> invoiceLineItems = new LinkedList<>();

        for (InvoiceLineItemDTO invoiceLineItemDTO : invoiceDTO.getInvoiceLineItems()) {
            invoiceLineItems.add(new InvoiceLineItem(
                    invoiceLineItemDTO.getMediumType(),
                    invoiceLineItemDTO.getName(),
                    invoiceLineItemDTO.getQuantity(),
                    invoiceLineItemDTO.getPrice()
            ));
        }

        Invoice invoice = new Invoice(
                invoiceDTO.getInvoiceId(),
                invoiceLineItems,
                invoiceDTO.getPaymentMethod(),
                invoiceDTO.getDate()
        );

        this.invoiceRepository.createInvoice(invoice);
    }

}

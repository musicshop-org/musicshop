package application;

import domain.Invoice;
import domain.InvoiceLineItem;
import infrastructure.InvoiceRepository;
import infrastructure.InvoiceRepositoryImpl;
import infrastructure.ProductRepository;
import infrastructure.ProductRepositoryImpl;
import jakarta.transaction.Transactional;
import sharedrmi.application.dto.ArtistDTO;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceServiceImpl extends UnicastRemoteObject implements InvoiceService{

    private InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl() throws RemoteException {
        this.invoiceRepository = new InvoiceRepositoryImpl();
    }

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) throws RemoteException {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<InvoiceDTO> findInvoiceById(InvoiceId invoiceId) throws RemoteException{
        List<InvoiceDTO> invoiceDTOS = new LinkedList<>();

        List<Invoice> invoices = invoiceRepository.findInvoiceById(invoiceId);

        for (Invoice invoice:invoices) {
            invoiceDTOS.add(new InvoiceDTO(
                    invoice.getInvoiceId(),
                    invoice.getInvoiceLineItems().stream().map(invoiceLineItem -> new InvoiceLineItemDTO(invoiceLineItem.getMediumType(), invoiceLineItem.getName(), invoiceLineItem.getQuantity(), invoiceLineItem.getPrice())).collect(Collectors.toList()),
                    invoice.getPaymentMethod(),
                    invoice.getCustomerType(),
                    invoice.getDate()
            ));
        }

        return invoiceDTOS;
    }

    @Transactional
    @Override
    public void createInvoice(InvoiceDTO invoiceDTO){
        List <InvoiceLineItem> invoiceLineItems = new LinkedList<>();
        for (InvoiceLineItemDTO invoiceLineItemDTO: invoiceDTO.getInvoiceLineItems()) {
            invoiceLineItems.add(new InvoiceLineItem(invoiceLineItemDTO.getMediumType(),invoiceLineItemDTO.getName(),invoiceLineItemDTO.getQuantity(),invoiceLineItemDTO.getPrice()));
        }

        Invoice invoice = new Invoice(new InvoiceId(),invoiceLineItems,invoiceDTO.getPaymentMethod(),invoiceDTO.getCustomerType(),invoiceDTO.getDate());

        invoiceRepository.ereateInvoice(invoice);
        //return invoice;
    }


}

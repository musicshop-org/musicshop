package application;

import domain.Album;
import domain.Invoice;
import domain.InvoiceLineItem;
import domain.repositories.InvoiceRepository;
import domain.repositories.ProductRepository;
import infrastructure.InvoiceRepositoryImpl;
import infrastructure.ProductRepositoryImpl;
import jakarta.transaction.Transactional;
import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InvoiceServiceImpl extends UnicastRemoteObject implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;

    public InvoiceServiceImpl() throws RemoteException {
        this.invoiceRepository = new InvoiceRepositoryImpl();
        this.productRepository = new ProductRepositoryImpl();
    }

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ProductRepository productRepository) throws RemoteException {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws RemoteException, InvoiceNotFoundException {

        Optional<Invoice> result = invoiceRepository.findInvoiceById(invoiceId);

        if (result.isEmpty()) {
            throw new InvoiceNotFoundException("invoice not found");
        }

        return new InvoiceDTO(
                result.get().getInvoiceId(),
                result.get().getInvoiceLineItems()
                        .stream().map(invoiceLineItem -> new InvoiceLineItemDTO(
                                invoiceLineItem.getMediumType(),
                                invoiceLineItem.getName(),
                                invoiceLineItem.getQuantity(),
                                invoiceLineItem.getPrice(),
                                invoiceLineItem.getReturnedQuantity()))
                        .collect(Collectors.toList()),
                result.get().getPaymentMethod(),
                result.get().getDate()
        );
    }

    @Transactional
    @Override
    public void createInvoice(InvoiceDTO invoiceDTO) throws RemoteException {

        List<InvoiceLineItem> invoiceLineItems = new LinkedList<>();

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

    @Transactional
    @Override
    public void returnInvoiceLineItem(InvoiceId invoiceId, InvoiceLineItemDTO invoiceLineItemDTO, int returnQuantity) throws RemoteException, InvoiceNotFoundException {
        Optional<Invoice> invoice = invoiceRepository.findInvoiceById(invoiceId);

        if (invoice.isEmpty()) {
            throw new InvoiceNotFoundException("invoice not found");
        }

        for (InvoiceLineItem invoiceLineItem : invoice.get().getInvoiceLineItems()) {
            if (invoiceLineItem.getName().equals(invoiceLineItemDTO.getName()) &&
                    invoiceLineItem.getMediumType().equals(invoiceLineItemDTO.getMediumType())) {
                invoiceLineItem.returnInvoiceLineItem(returnQuantity);
            }
        }
        invoiceRepository.update(invoice.get());

        List<Album> albums = productRepository.findAlbumsByAlbumTitle(invoiceLineItemDTO.getName());
        for (Album album: albums) {
            if (album.getMediumType().equals(invoiceLineItemDTO.getMediumType())){
                album.increaseStock(returnQuantity);
                productRepository.updateAlbum(album);
            }
        }

    }

}

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
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;

    public InvoiceServiceImpl() {
        this.invoiceRepository = new InvoiceRepositoryImpl();
        this.productRepository = new ProductRepositoryImpl();
    }

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws InvoiceNotFoundException {

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
                result.get().getDate(),
                null
        );
    }

    @Transactional
    @Override
    public InvoiceId createInvoice(InvoiceDTO invoiceDTO) throws NotEnoughStockException, AlbumNotFoundException {
        List<Album> albums = new LinkedList<>();
        for (InvoiceLineItemDTO invoiceLineItem: invoiceDTO.getInvoiceLineItems()) {
            Album album  = productRepository.findAlbumByAlbumTitleAndMedium(invoiceLineItem.getName(), invoiceLineItem.getMediumType());
            if (album == null){
                throw new AlbumNotFoundException("Album "+invoiceLineItem.getName()+" does not exist");
            }
            if (album.getStock() < invoiceLineItem.getQuantity()){
                throw new NotEnoughStockException("not enough " + album.getTitle() + " available ... in stock: " + album.getStock() + ", in cart: " + invoiceLineItem.getQuantity());
            }
            albums.add(album);
        }

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
                invoiceDTO.getDate(),
                invoiceDTO.getCustomerData()
        );

        for (int i = 0; i < albums.size(); i++) {
            albums.get(i).decreaseStock(invoiceDTO.getInvoiceLineItems().get(i).getQuantity());
            productRepository.updateAlbum(albums.get(i));
        }

        this.invoiceRepository.createInvoice(invoice);
        return invoiceDTO.getInvoiceId();
    }

    @Transactional
    @Override
    public void returnInvoiceLineItem(InvoiceId invoiceId, InvoiceLineItemDTO invoiceLineItemDTO, int returnQuantity) throws InvoiceNotFoundException {
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

        Album album = productRepository.findAlbumByAlbumTitleAndMedium(invoiceLineItemDTO.getName(),invoiceLineItemDTO.getMediumType());

        if (album != null){
            album.increaseStock(returnQuantity);
            productRepository.updateAlbum(album);
        }


    }

}

package application;

import domain.Album;
import domain.Invoice;
import domain.InvoiceLineItem;
import domain.repositories.InvoiceRepository;
import domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.AlbumId;
import sharedrmi.domain.valueobjects.InvoiceId;

import javax.naming.NoPermissionException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    private Invoice givenInvoice;
    private Album givenAlbum;
    private InvoiceService invoiceService;

    @Captor
    ArgumentCaptor<Invoice> invoiceCaptor;

    @Mock
    private static InvoiceRepository invoiceRepository;

    @Mock
    private static ProductRepository productRepository;

    @BeforeEach
    void initMockAndService() {
        givenAlbum = new Album("Test Album",
                "",
                new BigDecimal("5.00"),
                99,
                MediumType.CD,
                LocalDate.now(),
                new AlbumId(),
                "Test Label",
                Set.of());

        givenInvoice = new Invoice(
                new InvoiceId(111),
                List.of(new InvoiceLineItem(
                        MediumType.CD,
                        "Test Album",
                        4,
                        new BigDecimal("5.00")
                )),
                PaymentMethod.CREDIT_CARD,
                LocalDate.now(),
                null
        );

        invoiceService = new InvoiceServiceImpl(invoiceRepository, productRepository);
    }

    @Test
    void given_invoiceId_when_findInvoiceById_then_returnInvoice() throws NoPermissionException, InvoiceNotFoundException {
        // given
        InvoiceId invoiceId = new InvoiceId(111);
        Mockito.when(invoiceRepository.findInvoiceById(invoiceId)).thenReturn(Optional.of(givenInvoice));

        // when
        InvoiceDTO invoiceDTO = invoiceService.findInvoiceById(invoiceId);

        // then
        assertAll(
                () -> assertEquals(givenInvoice.getInvoiceId().getInvoiceId(), invoiceDTO.getInvoiceId().getInvoiceId()),
                () -> assertEquals(givenInvoice.getInvoiceLineItems().size(), invoiceDTO.getInvoiceLineItems().size()),
                () -> assertEquals(givenInvoice.getPaymentMethod(), invoiceDTO.getPaymentMethod()),
                () -> assertEquals(givenInvoice.getDate(), invoiceDTO.getDate())
        );
    }

    @Test
    void given_notExistingInvoiceId_when_findInvoiceById_then_returnEmptyOptional() {
        // given
        InvoiceId invoiceId = new InvoiceId(-111);

        Mockito.when(invoiceRepository.findInvoiceById(invoiceId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.findInvoiceById(invoiceId));
    }

    @Test
    void given_invoiceLineItemDTOandAmount_when_returnInvoiceLineItem_then_addReturnQuantity() throws NoPermissionException, InvoiceNotFoundException {
        // given
        InvoiceId invoiceId = new InvoiceId(111);
        Mockito.when(invoiceRepository.findInvoiceById(invoiceId)).thenReturn(Optional.of(givenInvoice));

        InvoiceLineItemDTO invoiceLineItemDTO = new InvoiceLineItemDTO(MediumType.CD, "Test Album", 4, new BigDecimal("5.00"),0);
        int returnQuantity = 2;

        // when
        invoiceService.returnInvoiceLineItem(invoiceId, invoiceLineItemDTO, returnQuantity);

        // then
        assertEquals(returnQuantity, givenInvoice.getInvoiceLineItems().get(0).getReturnedQuantity());
    }

    @Test
    void given_notExistingInvoiceId_when_returnInvoiceLineItem_then_throwInvoiceNotFoundException()  {
        // given
        InvoiceId invoiceId = new InvoiceId(-111);

        // when ... then
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.returnInvoiceLineItem(invoiceId, new InvoiceLineItemDTO(), 0));
    }

    @Test
    void given_invoiceDTO_when_createInvoice_then_validInvoice() throws AlbumNotFoundException, NoPermissionException, NotEnoughStockException {
        // given
        InvoiceDTO invoiceDTO = new InvoiceDTO(
                givenInvoice.getInvoiceId(),
                List.of(new InvoiceLineItemDTO(
                        givenInvoice.getInvoiceLineItems().get(0).getMediumType(),
                        givenInvoice.getInvoiceLineItems().get(0).getName(),
                        givenInvoice.getInvoiceLineItems().get(0).getQuantity(),
                        givenInvoice.getInvoiceLineItems().get(0).getPrice(),
                        givenInvoice.getInvoiceLineItems().get(0).getReturnedQuantity()
                )),
                givenInvoice.getPaymentMethod(),
                givenInvoice.getDate(),
                null
        );

        Mockito.when(productRepository.findAlbumByAlbumTitleAndMedium(
                        givenInvoice.getInvoiceLineItems().get(0).getName(),
                        givenInvoice.getInvoiceLineItems().get(0).getMediumType()))
                .thenReturn(givenAlbum);

        // when
        invoiceService.createInvoice(invoiceDTO);
        Mockito.verify(invoiceRepository).createInvoice(invoiceCaptor.capture());
        Invoice invoice = invoiceCaptor.getValue();

        // then
        assertAll(
                () -> assertEquals(givenInvoice.getInvoiceId().getInvoiceId(), invoice.getInvoiceId().getInvoiceId()),
                () -> assertEquals(givenInvoice.getInvoiceLineItems().size(), invoice.getInvoiceLineItems().size()),
                () -> assertEquals(givenInvoice.getPaymentMethod(), invoice.getPaymentMethod()),
                () -> assertEquals(givenInvoice.getDate(), invoice.getDate())
        );
    }

    @Test
    void given_invalidStockInvoiceDTO_when_createInvoice_then_throws() throws AlbumNotFoundException, NoPermissionException {
        boolean failed = false;
        // given
        InvoiceDTO invoiceDTO = new InvoiceDTO(
                givenInvoice.getInvoiceId(),
                List.of(new InvoiceLineItemDTO(
                        givenInvoice.getInvoiceLineItems().get(0).getMediumType(),
                        givenInvoice.getInvoiceLineItems().get(0).getName(),
                        100,
                        givenInvoice.getInvoiceLineItems().get(0).getPrice(),
                        givenInvoice.getInvoiceLineItems().get(0).getReturnedQuantity()
                )),
                givenInvoice.getPaymentMethod(),
                givenInvoice.getDate(),
                null
        );

        Mockito.when(productRepository.findAlbumByAlbumTitleAndMedium(
                        givenInvoice.getInvoiceLineItems().get(0).getName(),
                        givenInvoice.getInvoiceLineItems().get(0).getMediumType()))
                .thenReturn(givenAlbum);

        // when
        try {
            invoiceService.createInvoice(invoiceDTO);
            Mockito.verify(invoiceRepository).createInvoice(invoiceCaptor.capture());
        } catch (NotEnoughStockException e) {
            failed = true;
        }

        // then
        assertTrue(failed);
    }

    @Test
    void given_invalidNameInvoiceDTO_when_createInvoice_then_throws() throws NoPermissionException, NotEnoughStockException {
        boolean failed = false;
        // given
        InvoiceDTO invoiceDTO = new InvoiceDTO(
                givenInvoice.getInvoiceId(),
                List.of(new InvoiceLineItemDTO(
                        givenInvoice.getInvoiceLineItems().get(0).getMediumType(),
                        "invalidName",
                        givenInvoice.getInvoiceLineItems().get(0).getQuantity(),
                        givenInvoice.getInvoiceLineItems().get(0).getPrice(),
                        givenInvoice.getInvoiceLineItems().get(0).getReturnedQuantity()
                )),
                givenInvoice.getPaymentMethod(),
                givenInvoice.getDate(),
                null
        );

        Mockito.when(productRepository.findAlbumByAlbumTitleAndMedium(
                        invoiceDTO.getInvoiceLineItems().get(0).getName(),
                        givenInvoice.getInvoiceLineItems().get(0).getMediumType()))
                .thenReturn(null);

        // when
        try {
            invoiceService.createInvoice(invoiceDTO);
            Mockito.verify(invoiceRepository).createInvoice(invoiceCaptor.capture());
        } catch (AlbumNotFoundException e) {
            failed = true;
        }

        // then
        assertTrue(failed);
    }
}

package application;

import domain.Invoice;
import domain.InvoiceLineItem;
import domain.repositories.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    private InvoiceId givenInvoiceId;
    private Invoice givenInvoice;

    private InvoiceService invoiceService;

    @Mock
    private static InvoiceRepository invoiceRepository;

    @BeforeEach
    void initMockAndService() throws RemoteException {
        givenInvoiceId = new InvoiceId(111);
        givenInvoice = new Invoice(
                givenInvoiceId,
                List.of(new InvoiceLineItem(
                        MediumType.DIGITAL,
                        "Song",
                        4,
                        new BigDecimal("5.00")
                )),
                PaymentMethod.CREDIT_CARD,
                LocalDate.now()
        );

        invoiceService = new InvoiceServiceImpl(invoiceRepository);
    }

    @Test
    void given_invoiceId_when_findInvoiceById_then_returnInvoice() throws RemoteException {
        // given
        InvoiceId invoiceId = new InvoiceId(111);

        Mockito.when(invoiceRepository.findInvoiceById(invoiceId)).thenReturn(Optional.of(givenInvoice));

        // when
        Optional<InvoiceDTO> invoiceDTO = invoiceService.findInvoiceById(invoiceId);

        // then
        assertAll(
                () -> assertEquals(givenInvoice.getInvoiceId().getInvoiceId(), invoiceDTO.get().getInvoiceId().getInvoiceId()),
                () -> assertEquals(givenInvoice.getInvoiceLineItems().size(), invoiceDTO.get().getInvoiceLineItems().size()),
                () -> assertEquals(givenInvoice.getPaymentMethod(), invoiceDTO.get().getPaymentMethod()),
                () -> assertEquals(givenInvoice.getDate(), invoiceDTO.get().getDate())
        );
    }

    @Test
    void given_notExistingInvoiceId_when_findInvoiceById_then_returnEmptyOptional() throws RemoteException {
        // given
        InvoiceId invoiceId = new InvoiceId(-111);

        Mockito.when(invoiceRepository.findInvoiceById(invoiceId)).thenReturn(Optional.empty());

        // when
        Optional<InvoiceDTO> invoiceDTO = invoiceService.findInvoiceById(invoiceId);

        // then
        assertEquals(Optional.empty(), invoiceDTO);
    }

    @Test
    void given_invoice_when_createInvoice_then_addInvoiceToDatabase() throws RemoteException {
        // given
        List<InvoiceLineItem> items = new LinkedList<>();
        items.add(new InvoiceLineItem(
                MediumType.DIGITAL,
                "Song",
                4,
                new BigDecimal("5.00")
        ));

        InvoiceId invoiceId = new InvoiceId();
        Invoice invoice = new Invoice(
                invoiceId,
                items,
                PaymentMethod.CREDIT_CARD,
                LocalDate.now()
        );

        // when


        // then


    }

}

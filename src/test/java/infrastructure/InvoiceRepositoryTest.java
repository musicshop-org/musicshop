package infrastructure;

import domain.Invoice;
import domain.InvoiceLineItem;
import domain.repositories.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvoiceRepositoryTest {

    @Mock
    InvoiceRepository invoiceRepository;

    @Test
    void given_invoiceId_when_findInvoiceById_then_returnInvoice() throws RemoteException {
        // given
        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
        InvoiceId invoiceId = new InvoiceId(111);

        // when
        Optional<Invoice> invoice = invoiceRepository.findInvoiceById(invoiceId);

        // then
        assertEquals(invoiceId.getInvoiceId(), invoice.get().getInvoiceId().getInvoiceId());
    }

    @Test
    void given_notExistingInvoiceId_when_findInvoiceById_then_returnEmptyOptional() throws RemoteException {
        // given
        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
        InvoiceId invoiceId = new InvoiceId(-111);

        // when
        Optional<Invoice> invoice = invoiceRepository.findInvoiceById(invoiceId);

        // then
        assertTrue(invoice.isEmpty());
    }

    @Test
    void given_invoice_when_createInvoice_then_addInvoiceToDatabase() throws RemoteException {
        // given
        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
        InvoiceId invoiceId = new InvoiceId();

        Invoice invoice = new Invoice(
                invoiceId,
                List.of(new InvoiceLineItem(
                        MediumType.DIGITAL,
                        "Song",
                        4,
                        new BigDecimal("5.00")
                )),
                PaymentMethod.CREDIT_CARD,
                LocalDate.now(),
                null
        );

        // when
        invoiceRepository.createInvoice(invoice);

        // then
        assertEquals(invoiceId.getInvoiceId(), invoiceRepository.findInvoiceById(invoiceId).get().getInvoiceId().getInvoiceId());
    }

}

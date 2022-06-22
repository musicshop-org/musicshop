package infrastructure;

import domain.Invoice;
import domain.InvoiceLineItem;
import org.junit.jupiter.api.Test;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvoiceRepositoryTest {

    @Test
    void given_invoiceId_when_findInvoiceById_then_returnInvoice() {
        // given
        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
        InvoiceId invoiceId = new InvoiceId(111);

        // when
        Optional<Invoice> invoice = invoiceRepository.findInvoiceById(invoiceId);

        // then
        assertEquals(invoiceId.getInvoiceId(), invoice.get().getInvoiceId().getInvoiceId());
    }

    @Test
    void given_notExistingInvoiceId_when_findInvoiceById_then_returnEmptyOptional() {
        // given
        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
        InvoiceId invoiceId = new InvoiceId(-111);

        // when
        Optional<Invoice> invoice = invoiceRepository.findInvoiceById(invoiceId);

        // then
        assertTrue(invoice.isEmpty());
    }

    @Test
    void given_invoice_when_createInvoice_then_addInvoiceToDatabase() {
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

    @Test
    void given_invoice_when_update_then_changeInvoice() {
        // given
        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
        Invoice invoice = invoiceRepository.findInvoiceById(new InvoiceId(111)).get();
        int returnedQuantity = invoice.getInvoiceLineItems().get(0).getReturnedQuantity();
        int returnQuantity = 1;

        invoice.getInvoiceLineItems().get(0).returnInvoiceLineItem(returnQuantity);

        // when
        invoiceRepository.update(invoice);

        // then
        assertEquals(returnedQuantity + returnQuantity, invoiceRepository.findInvoiceById(new InvoiceId(111)).get().getInvoiceLineItems().get(0).getReturnedQuantity());
    }
}
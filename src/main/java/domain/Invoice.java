package domain;

import lombok.Getter;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
public class Invoice implements Serializable {

    private long id;
    private InvoiceId invoiceId;
    private List<InvoiceLineItem> invoiceLineItems;
    private PaymentMethod paymentMethod;
    private LocalDate date;

    protected Invoice() {
    }

    public Invoice(InvoiceId invoiceId, List<InvoiceLineItem> invoiceLineItems, PaymentMethod paymentMethod, LocalDate date) {
        this.invoiceId = invoiceId;
        this.invoiceLineItems = invoiceLineItems;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }
}
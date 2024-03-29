package domain;

import lombok.Getter;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.CustomerData;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
public class Invoice implements Serializable {

    @SuppressWarnings("unused")
    private long id;
    private InvoiceId invoiceId;
    private List<InvoiceLineItem> invoiceLineItems;
    private PaymentMethod paymentMethod;
    private LocalDate date;
    private CustomerData customerData;

    @SuppressWarnings("unused")
    protected Invoice() {
    }

    public Invoice(InvoiceId invoiceId, List<InvoiceLineItem> invoiceLineItems, PaymentMethod paymentMethod, LocalDate date, CustomerData customerData) {
        this.invoiceId = invoiceId;
        this.invoiceLineItems = invoiceLineItems;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.customerData = customerData;
    }
}
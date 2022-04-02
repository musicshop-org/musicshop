package domain;

import lombok.Getter;
import sharedrmi.domain.enums.CustomerType;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.time.LocalDate;
import java.util.List;

@Getter
public class Invoice {

    private InvoiceId invoiceId;
    private List<InvoiceLineItem> invoiceLineItems;
    private PaymentMethod paymentMethod;
    private CustomerType customerType;
    private LocalDate date;

    public Invoice(InvoiceId invoiceId, List<InvoiceLineItem> invoiceLineItems, PaymentMethod paymentMethod, CustomerType customerType, LocalDate date) {
        this.invoiceId = invoiceId;
        this.invoiceLineItems = invoiceLineItems;
        this.paymentMethod = paymentMethod;
        this.customerType = customerType;
        this.date = date;
    }

}

package domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Invoice {
    private List<InvoiceLineItem> invoiceLineItems;

}

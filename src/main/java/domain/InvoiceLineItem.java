package domain;

import lombok.Getter;
import sharedrmi.domain.enums.MediumType;

import java.math.BigDecimal;

@Getter
public class InvoiceLineItem {

    private long id;
    private MediumType mediumType;
    private String name;
    private int quantity;
    private BigDecimal price;
    private int returnedQuantity;

    protected InvoiceLineItem() {
    }

    public InvoiceLineItem(MediumType mediumType, String name, int quantity, BigDecimal price) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.returnedQuantity = 0;
    }

    public void returnInvoiceLineItem(int amount){
        this.returnedQuantity += amount;
    }
}

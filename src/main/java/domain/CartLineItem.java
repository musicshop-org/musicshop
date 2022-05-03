package domain;

import sharedrmi.domain.enums.MediumType;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class CartLineItem implements Serializable {

    private long id;
    private MediumType mediumType;
    private String name;
    private int quantity;
    private BigDecimal price;
    private int stock;

    protected CartLineItem() {
    }

    public CartLineItem(MediumType mediumType, String name, int quantity, BigDecimal price, int stock) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.stock = stock;
    }

    public void changeQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartLineItem cartLineItem = (CartLineItem) o;
        return mediumType == cartLineItem.mediumType && name.equals(cartLineItem.name) && price.equals(cartLineItem.price);
    }

}

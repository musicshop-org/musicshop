package domain;

import sharedrmi.domain.enums.MediumType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CartLineItem {

    private long id;
    private MediumType mediumType;
    private String name;
    private int quantity;
    private BigDecimal price;

    protected CartLineItem() {
    }

    public CartLineItem(MediumType mediumType, String name, int quantity, BigDecimal price) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
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

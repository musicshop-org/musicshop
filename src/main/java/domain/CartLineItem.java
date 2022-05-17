package domain;

import lombok.Getter;
import sharedrmi.domain.enums.MediumType;

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
    private String imageUrl;

    protected CartLineItem() {
    }

    public CartLineItem(MediumType mediumType, String name, int quantity, BigDecimal price, int stock, String imageUrl) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
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

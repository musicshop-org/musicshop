package domain;

import lombok.Getter;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.ProductType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CartLineItem implements Serializable {

    private long id;
    private long productId;
    private MediumType mediumType;
    private String name;
    private int quantity;
    private BigDecimal price;
    private int stock;
    private String imageUrl;
    private ProductType productType;
    private List<String> artists;

    protected CartLineItem() {
    }

    public CartLineItem(MediumType mediumType, String name, int quantity, BigDecimal price, int stock, String imageUrl, ProductType productType, long productId) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.productType = productType;
        this.artists = new ArrayList<String>();
        this.productId = productId;
    }

    public CartLineItem(MediumType mediumType, String name, int quantity, BigDecimal price, int stock, String imageUrl, ProductType productType, List<String> artists, long productId) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.productType = productType;
        this.artists = artists;
        this.productId = productId;
    }

    public void changeQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartLineItem cartLineItem = (CartLineItem) o;
        return mediumType == cartLineItem.mediumType && name.equals(cartLineItem.name) && price.equals(cartLineItem.price) && productType.equals(cartLineItem.productType);
    }
}
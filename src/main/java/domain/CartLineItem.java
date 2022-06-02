package domain;

import lombok.Getter;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.ProductType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class CartLineItem implements Serializable {

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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
        this.artists = new ArrayList<>();
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
        CartLineItem that = (CartLineItem) o;
        return productId == that.productId && quantity == that.quantity && stock == that.stock && mediumType == that.mediumType && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(imageUrl, that.imageUrl) && productType == that.productType && Objects.equals(artists, that.artists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, mediumType, name, quantity, price, stock, imageUrl, productType, artists);
    }
}

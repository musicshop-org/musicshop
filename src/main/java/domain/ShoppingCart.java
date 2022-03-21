package domain;

import java.util.List;

public class ShoppingCart {

    private List<LineItem> lineItems;

    public ShoppingCart(){};

    public ShoppingCart(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
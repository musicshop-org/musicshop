package domain;

import java.util.LinkedList;
import java.util.List;

public class ShoppingCart {

    private List<LineItem> lineItems;

    public ShoppingCart(){this.lineItems = new LinkedList<>();};

    public ShoppingCart(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
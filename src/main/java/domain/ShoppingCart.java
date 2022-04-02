package domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShoppingCart {

    private String ownerId;
    private final List<LineItem> lineItems;

    public ShoppingCart() {
        this.lineItems = new ArrayList<>();
    }

    public ShoppingCart(String ownerId) {
        this();
        this.ownerId = ownerId;
    }

    public ShoppingCart(String ownerId, List<LineItem> lineItems) {
        this.ownerId = ownerId;
        this.lineItems = lineItems;
    }

    public void addLineItem(LineItem newItem) {
        for (LineItem item : lineItems) {
            if (item.equals(newItem)) {
                item.changeQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }

        this.lineItems.add(newItem);
    }

    public void changeQuantity(LineItem lineItem, int quantity) {
        for (LineItem item : lineItems) {
            if (item.equals(lineItem)) {
                item.changeQuantity(quantity);
                return;
            }
        }
    }

    public void removeLineItem(LineItem lineItemToRemove) {
        for (LineItem lineItem : lineItems) {
            if (lineItem.equals(lineItemToRemove)) {
                lineItems.remove(lineItemToRemove);
                return;
            }
        }
    }
}

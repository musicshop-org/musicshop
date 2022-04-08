package domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShoppingCart {

    private String ownerId;
    private final List<CartLineItem> cartLineItems;

    public ShoppingCart() {
        this.cartLineItems = new ArrayList<>();
    }

    public ShoppingCart(String ownerId) {
        this();
        this.ownerId = ownerId;
    }

    public ShoppingCart(String ownerId, List<CartLineItem> cartLineItems) {
        this.ownerId = ownerId;
        this.cartLineItems = cartLineItems;
    }

    public void addLineItem(CartLineItem newItem) {
        for (CartLineItem cartLineItem : this.cartLineItems) {
            if (cartLineItem.equals(newItem)) {
                cartLineItem.changeQuantity(cartLineItem.getQuantity() + newItem.getQuantity());
                return;
            }
        }

        this.cartLineItems.add(newItem);
    }

    public void changeQuantity(CartLineItem lineItem, int quantity) {
        for (CartLineItem cartLineItem : this.cartLineItems) {
            if (cartLineItem.equals(lineItem)) {
                cartLineItem.changeQuantity(quantity);
                return;
            }
        }
    }

    public void removeLineItem(CartLineItem cartLineItemToRemove) {
        for (CartLineItem cartLineItem : cartLineItems) {
            if (cartLineItem.equals(cartLineItemToRemove)) {
                this.cartLineItems.remove(cartLineItemToRemove);
                return;
            }
        }
    }

    public void clear() {
        this.cartLineItems.clear();
    }
}

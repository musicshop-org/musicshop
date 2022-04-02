package domain;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class ShoppingCart {

    private final UUID ownerId;
    private List<CartLineItem> cartLineItems;

    public ShoppingCart(UUID ownerId){
        this.ownerId = ownerId;
        this.cartLineItems = new LinkedList<>();
    };

    public ShoppingCart(UUID ownerId, List<CartLineItem> cartLineItems) {
        this.ownerId = ownerId;
        this.cartLineItems = cartLineItems;
    }

    public void addLineItem(CartLineItem newItem){
        for (CartLineItem item: cartLineItems) {
            if (item.equals(newItem)){
                item.changeQuantity(item.getQuantity()+ newItem.getQuantity());
                return;
            }
        }
        this.cartLineItems.add(newItem);
    }

    public void changeQuantity (CartLineItem cartLineItem, int quantity) {
        for (CartLineItem item: cartLineItems) {
            if (item.equals(cartLineItem)){
                item.changeQuantity(quantity);
                return;
            }
        }
    }
    
    public void removeLineItem (CartLineItem cartLineItemToRemove){
        for (CartLineItem cartLineItem : cartLineItems) {
            if (cartLineItem.equals(cartLineItemToRemove)){
                cartLineItems.remove(cartLineItemToRemove);
                return;
            }
        }
    }
}

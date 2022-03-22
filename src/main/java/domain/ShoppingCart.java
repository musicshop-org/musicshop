package domain;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class ShoppingCart {

    private final UUID ownerId;
    private List<LineItem> lineItems;


    public ShoppingCart(UUID ownerId){
        this.ownerId = ownerId;
        this.lineItems = new LinkedList<>();
    };

    public ShoppingCart(UUID ownerId, List<LineItem> lineItems) {
        this.ownerId = ownerId;
        this.lineItems = lineItems;
    }

    public void addLineItem(LineItem item){
        this.lineItems.add(item);
    }
}

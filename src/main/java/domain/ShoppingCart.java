package domain;

import lombok.Getter;
import sharedrmi.application.dto.LineItemDTO;

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

    public void addLineItem(LineItem newItem){
        for (LineItem item: lineItems) {
            if (item.equals(newItem)){
                item.changeQuantity(item.getQuantity()+ newItem.getQuantity());
                return;
            }
        }
        this.lineItems.add(newItem);
    }
    
    public void removeLineItem (LineItem lineItem){
        for (int i = 0; i < this.lineItems.size(); i++) {
            if (this.lineItems.get(i).equals(lineItem)){
                this.lineItems.remove(lineItem);
        }

        }
    }
}

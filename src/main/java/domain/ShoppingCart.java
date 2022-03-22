package domain;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class ShoppingCart {

    private List<LineItem> lineItems;
    private Employee employee;

    public ShoppingCart(){this.lineItems = new LinkedList<>();};

    public ShoppingCart(Employee employee){
        this.employee = employee;
        this.lineItems = new LinkedList<>();
    }

    public ShoppingCart(Employee employee, List<LineItem> lineItems) {
        this.lineItems = lineItems;
        this.employee = employee;
    }

    public void addLineItem(LineItem item){
        this.lineItems.add(item);
    }
}
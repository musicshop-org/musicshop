package domain;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LineItem {

    private LineItem(){};

    private long id;
    private String name;
    private int quantity;
    private BigDecimal price;

    public LineItem(String name, int quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}

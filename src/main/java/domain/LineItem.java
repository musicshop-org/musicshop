package domain;

import domain.enums.MediumType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LineItem {

    private LineItem(){};

    private long id;
    private MediumType mediumType;
    private String name;
    private int quantity;
    private BigDecimal price;

    public LineItem(MediumType mediumType, String name, int quantity, BigDecimal price) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}

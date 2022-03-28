package domain;

import sharedrmi.domain.enums.MediumType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LineItem {

    private long id;
    private MediumType mediumType;
    private String name;
    private int quantity;
    private BigDecimal price;

    protected LineItem() {
    }

    public LineItem(MediumType mediumType, String name, int quantity, BigDecimal price) {
        this.mediumType = mediumType;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void changeQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return quantity == lineItem.quantity && mediumType == lineItem.mediumType && name.equals(lineItem.name) && price.equals(lineItem.price);
    }

}

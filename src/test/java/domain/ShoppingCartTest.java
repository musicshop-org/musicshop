package domain;

import domain.enums.MediumType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartTest {
    @Test
    void when_lineItem_added_return_equal (){
        //given
        ShoppingCart cart = new ShoppingCart(UUID.randomUUID());
        LineItem expectedItem = new LineItem(MediumType.CD,"24K Magic", 12, BigDecimal.valueOf(18));

        //when
        cart.addLineItem(expectedItem);

        //then
        assertEquals(expectedItem, cart.getLineItems().get(0));
    }
}

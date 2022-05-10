package domain;

import org.junit.jupiter.api.Test;
import sharedrmi.domain.enums.MediumType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartTest {

    @Test
    void when_lineItem_added_return_equal() {
        //given
        ShoppingCart cart = new ShoppingCart(UUID.randomUUID().toString());
        CartLineItem expectedItem = new CartLineItem(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18), 5);

        //when
        cart.addLineItem(expectedItem);

        //then
        assertEquals(expectedItem, cart.getCartLineItems().get(0));
    }

    @Test
    void when_clearCart_return_empty() {
        //given
        ShoppingCart cart = new ShoppingCart(UUID.randomUUID().toString());
        CartLineItem expectedItem = new CartLineItem(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18), 5);
        cart.addLineItem(expectedItem);

        int expected = 0;

        //when
        cart.clear();

        //then
        assertEquals(expected, cart.getCartLineItems().size());
    }

}

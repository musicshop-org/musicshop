package domain;

import org.junit.jupiter.api.Test;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartTest {

    @Test
    void given_cartAndNewLineItem_when_addLineItem_then_expectLineItemAdded() {
        // given
        ShoppingCart cart = new ShoppingCart(UUID.randomUUID().toString());
        CartLineItem expectedItem = new CartLineItem(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18), 5, "", ProductType.ALBUM, 1);

        // when
        cart.addLineItem(expectedItem);

        // then
        assertEquals(expectedItem, cart.getCartLineItems().get(0));
    }

    @Test
    void given_cartAndAlreadyAddedLineItem_when_addLineItem_then_expectLineItemAdded() {
        // given
        ShoppingCart cart = new ShoppingCart(UUID.randomUUID().toString());
        CartLineItem cartLineItem = new CartLineItem(MediumType.CD, "24K Magic", 2, BigDecimal.valueOf(18), 5, "", ProductType.ALBUM, 1);

        int expectedQuantity = 4;

        // when
        cart.addLineItem(cartLineItem);
        cart.addLineItem(cartLineItem);

        // then
        assertEquals(expectedQuantity, cart.getCartLineItems().get(0).getQuantity());
    }


    @Test
    void given_cart_when_clearCart_then_expectEmptyCart() {
        // given
        ShoppingCart cart = new ShoppingCart(UUID.randomUUID().toString());
        CartLineItem expectedItem = new CartLineItem(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18), 5, "", ProductType.ALBUM, 1);
        cart.addLineItem(expectedItem);

        int expected = 0;

        // when
        cart.clear();

        // then
        assertEquals(expected, cart.getCartLineItems().size());
    }
}
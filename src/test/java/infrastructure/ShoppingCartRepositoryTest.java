package infrastructure;

import domain.ShoppingCart;
import domain.repositories.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShoppingCartRepositoryTest {
    private static ShoppingCartRepository shoppingCartRepository;
    private static String givenOwnerId;

    @BeforeAll
    static void init_repository() {
        shoppingCartRepository = new ShoppingCartRepositoryImpl();
        givenOwnerId = UUID.randomUUID().toString();
        shoppingCartRepository.createShoppingCart(givenOwnerId);
    }

    @Test
    void when_given_ownerID_return_cart() {
        //when
        Optional<ShoppingCart> actualCart = shoppingCartRepository.findShoppingCartByOwnerId(givenOwnerId);

        //then
        assertTrue(actualCart.isPresent());
        assertEquals(givenOwnerId, actualCart.get().getOwnerId());
    }

    @Test
    void when_given_invalid_ownerID_return_empty() {
        //given
        String invalidOwnerId = UUID.randomUUID().toString();

        //when
        Optional<ShoppingCart> actualCart = shoppingCartRepository.findShoppingCartByOwnerId(invalidOwnerId);

        //then
        assertTrue(actualCart.isEmpty());
    }

    @Test
    void when_given_ownerID_create_cart() {
        //given
        String ownerId = UUID.randomUUID().toString();
        shoppingCartRepository.createShoppingCart(ownerId);

        //when
        Optional<ShoppingCart> actualCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId);

        //then
        assertTrue(actualCart.isPresent());
        assertEquals(ownerId, actualCart.get().getOwnerId());
    }
}

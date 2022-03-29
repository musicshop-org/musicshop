package infrastructure;

import domain.ShoppingCart;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShoppingCartRepositoryTest {
    private static ShoppingCartRepository shoppingCartRepository;
    private static UUID givenOwnerId;

    @BeforeAll
    static void init_repository(){
        shoppingCartRepository = new ShoppingCartRepositoryImpl();
        givenOwnerId = UUID.randomUUID();
        shoppingCartRepository.createShoppingCart(givenOwnerId);
    }

    @Test
    void when_given_ownerID_return_cart (){
        //when
        Optional<ShoppingCart> actualCart = shoppingCartRepository.findShoppingCartByOwnerId(givenOwnerId);

        //then
        assertTrue(actualCart.isPresent());
        assertEquals(givenOwnerId,actualCart.get().getOwnerId());
    }

    @Test
    void when_given_invalid_ownerID_return_empty (){
        //given
        UUID invalidOwnerId = UUID.randomUUID();

        //when
        Optional<ShoppingCart> actualCart = shoppingCartRepository.findShoppingCartByOwnerId(invalidOwnerId);

        //then
        assertTrue(actualCart.isEmpty());
    }

    @Test
    void when_given_ownerID_create_cart (){
        //given
        UUID ownerId = UUID.randomUUID();
        shoppingCartRepository.createShoppingCart(ownerId);

        //when
        Optional<ShoppingCart> actualCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId);

        //then
        assertTrue(actualCart.isPresent());
        assertEquals(ownerId,actualCart.get().getOwnerId());
    }
}

package domain.repositories;

import domain.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartRepository {

    Optional<ShoppingCart> findShoppingCartByOwnerId(String ownerId);

    ShoppingCart createShoppingCart(String ownerId);

}

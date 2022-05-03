package domain.repositories;

import domain.ShoppingCart;

import java.io.Serializable;
import java.util.Optional;

public interface ShoppingCartRepository extends Serializable {

    Optional<ShoppingCart> findShoppingCartByOwnerId(String ownerId);

    ShoppingCart createShoppingCart(String ownerId);

}

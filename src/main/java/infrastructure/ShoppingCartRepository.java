package infrastructure;

import domain.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository {
    Optional<ShoppingCart> findShoppingCartByOwnerId (UUID ownerId);
    void createShoppingCart(UUID ownerId);
}

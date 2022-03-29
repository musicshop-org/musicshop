package infrastructure;

import domain.ShoppingCart;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShoppingCartRepositoryImpl implements ShoppingCartRepository{
    private static final List<ShoppingCart> shoppingCarts = new LinkedList<>();

    @Override
    public Optional<ShoppingCart> findShoppingCartByOwnerId (UUID ownerId) {
        for (ShoppingCart cart: shoppingCarts) {
            if (cart.getOwnerId().equals(ownerId)) {
                return Optional.of(cart);
            }
        }
        return Optional.empty();
    }

    @Override
    public ShoppingCart createShoppingCart(UUID ownerId) {
        ShoppingCart shoppingCart = new ShoppingCart(ownerId);
        shoppingCarts.add(shoppingCart);

        return shoppingCart;
    }

}

package application;

import domain.CartLineItem;
import domain.ShoppingCart;
import domain.repositories.ShoppingCartRepository;
import infrastructure.ShoppingCartRepositoryImpl;

import jakarta.transaction.Transactional;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.CartLineItemDTO;
import sharedrmi.application.dto.ShoppingCartDTO;

import javax.naming.NoPermissionException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCart shoppingCart;

    public ShoppingCartServiceImpl() {
        super();

        this.shoppingCartRepository = new ShoppingCartRepositoryImpl();
        this.shoppingCart = new ShoppingCart();
    }

    public ShoppingCartServiceImpl(String ownerId) {
        super();

        this.shoppingCartRepository = new ShoppingCartRepositoryImpl();

        Optional<ShoppingCart> existingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId);

        if (existingCart.isEmpty()) {
            this.shoppingCart = shoppingCartRepository.createShoppingCart(ownerId);
        } else {
            this.shoppingCart = existingCart.get();
        }
    }

    public ShoppingCartServiceImpl(String ownerId, ShoppingCartRepository repo) {
        super();

        this.shoppingCartRepository = repo;
        this.shoppingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId).get();
    }

    @Transactional
    @Override
    public ShoppingCartDTO getCart() {
        List<CartLineItemDTO> cartLineItemsDTO = new LinkedList<>();

        for (CartLineItem cartLineItem : shoppingCart.getCartLineItems()) {
            cartLineItemsDTO.add(new CartLineItemDTO(
                    cartLineItem.getMediumType(),
                    cartLineItem.getName(),
                    cartLineItem.getQuantity(),
                    cartLineItem.getPrice(),
                    cartLineItem.getStock()
            ));
        }

        return new ShoppingCartDTO(shoppingCart.getOwnerId(), cartLineItemsDTO);
    }

    @Transactional
    @Override
    public void addProductToCart(AlbumDTO album, int amount) {
        CartLineItem cartLineItem = new CartLineItem(
                album.getMediumType(),
                album.getTitle(), amount,
                album.getPrice(),
                album.getStock()
        );

        this.shoppingCart.addLineItem(cartLineItem);
    }

    @Transactional
    @Override
    public void changeQuantity(CartLineItemDTO cartLineItemDTO, int quantity) {
        CartLineItem cartLineItem = new CartLineItem(
                cartLineItemDTO.getMediumType(),
                cartLineItemDTO.getName(),
                cartLineItemDTO.getQuantity(),
                cartLineItemDTO.getPrice(),
                cartLineItemDTO.getStock()
        );

        this.shoppingCart.changeQuantity(cartLineItem, quantity);
    }

    @Transactional
    @Override
    public void removeProductFromCart(CartLineItemDTO cartLineItemDTO) {
        CartLineItem cartLineItem = new CartLineItem(
                cartLineItemDTO.getMediumType(),
                cartLineItemDTO.getName(),
                cartLineItemDTO.getQuantity(),
                cartLineItemDTO.getPrice(),
                cartLineItemDTO.getStock()
        );

        this.shoppingCart.removeLineItem(cartLineItem);
    }

    @Transactional
    @Override
    public void clearCart() {
        this.shoppingCart.clear();
    }
}

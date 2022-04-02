package application;

import domain.CartLineItem;
import domain.ShoppingCart;
import domain.repositories.ShoppingCartRepository;
import infrastructure.ShoppingCartRepositoryImpl;

import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.CartLineItemDTO;
import sharedrmi.application.dto.ShoppingCartDTO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ShoppingCartServiceImpl extends UnicastRemoteObject implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCart shoppingCart;

    public ShoppingCartServiceImpl() throws RemoteException {
        super();

        this.shoppingCartRepository = new ShoppingCartRepositoryImpl();
        this.shoppingCart = new ShoppingCart();
    }

    public ShoppingCartServiceImpl(String ownerId) throws RemoteException {
        super();

        this.shoppingCartRepository = new ShoppingCartRepositoryImpl();

        Optional<ShoppingCart> existingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId);

        if (existingCart.isEmpty()) {
            this.shoppingCart = shoppingCartRepository.createShoppingCart(ownerId);
        } else {
            this.shoppingCart = existingCart.get();
        }
    }

    public ShoppingCartServiceImpl(String ownerId, ShoppingCartRepository repo) throws RemoteException {
        super();

        this.shoppingCartRepository = repo;
        this.shoppingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId).get();
    }

    @Override
    public ShoppingCartDTO getCart() {
        List<CartLineItemDTO> cartLineItemsDTO = new LinkedList<>();

        for (CartLineItem cartLineItem : shoppingCart.getLineItems()) {
            cartLineItemsDTO.add(new CartLineItemDTO(
                    cartLineItem.getMediumType(),
                    cartLineItem.getName(),
                    cartLineItem.getQuantity(),
                    cartLineItem.getPrice()
            ));
        }

        return new ShoppingCartDTO(shoppingCart.getOwnerId(), cartLineItemsDTO);
    }

    @Override
    public void addProductToCart(AlbumDTO album, int amount) {
        CartLineItem cartLineItem = new CartLineItem(
                album.getMediumType(),
                album.getTitle(), amount,
                album.getPrice()
        );

        this.shoppingCart.addLineItem(cartLineItem);
    }

    @Override
    public void changeQuantity(CartLineItemDTO cartLineItemDTO, int quantity) {
        CartLineItem cartLineItem = new CartLineItem(
                cartLineItemDTO.getMediumType(),
                cartLineItemDTO.getName(),
                cartLineItemDTO.getQuantity(),
                cartLineItemDTO.getPrice()
        );

        this.shoppingCart.changeQuantity(cartLineItem, quantity);
    }

    @Override
    public void removeProductFromCart(CartLineItemDTO cartLineItemDTO) {
        CartLineItem cartLineItem = new CartLineItem(
                cartLineItemDTO.getMediumType(),
                cartLineItemDTO.getName(),
                cartLineItemDTO.getQuantity(),
                cartLineItemDTO.getPrice()
        );

        this.shoppingCart.removeLineItem(cartLineItem);
    }
}

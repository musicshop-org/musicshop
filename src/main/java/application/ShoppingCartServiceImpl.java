package application;

import domain.LineItem;
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
        List<CartLineItemDTO> lineItemsDTO = new LinkedList<>();

        for (LineItem item : shoppingCart.getLineItems()) {
            lineItemsDTO.add(new CartLineItemDTO(
                    item.getMediumType(),
                    item.getName(),
                    item.getQuantity(),
                    item.getPrice()
            ));
        }

        return new ShoppingCartDTO(shoppingCart.getOwnerId(), lineItemsDTO);
    }

    @Override
    public void addProductToCart(AlbumDTO album, int amount) {
        LineItem item = new LineItem(
                album.getMediumType(),
                album.getTitle(), amount,
                album.getPrice()
        );

        this.shoppingCart.addLineItem(item);
    }

    @Override
    public void changeQuantity(CartLineItemDTO lineItemDTO, int quantity) {
        LineItem lineItem = new LineItem(
                lineItemDTO.getMediumType(),
                lineItemDTO.getName(),
                lineItemDTO.getQuantity(),
                lineItemDTO.getPrice()
        );

        this.shoppingCart.changeQuantity(lineItem, quantity);
    }

    @Override
    public void removeProductFromCart(CartLineItemDTO lineItemDTO) {
        LineItem lineItem = new LineItem(
                lineItemDTO.getMediumType(),
                lineItemDTO.getName(),
                lineItemDTO.getQuantity(),
                lineItemDTO.getPrice()
        );

        this.shoppingCart.removeLineItem(lineItem);
    }
}

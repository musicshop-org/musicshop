package application.api;

import domain.LineItem;
import domain.ShoppingCart;
import infrastructure.ShoppingCartRepository;
import infrastructure.ShoppingCartRepositoryImpl;
import sharedrmi.application.dto.AlbumDTO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;
import java.util.UUID;

public class ShoppingCartServiceImpl extends UnicastRemoteObject implements ShoppingCartService{

    private ShoppingCartRepository shoppingCartRepository;
    private ShoppingCart shoppingCart;

    public ShoppingCartServiceImpl(UUID ownerId) throws RemoteException {
        super();
        shoppingCartRepository = new ShoppingCartRepositoryImpl();
        Optional<ShoppingCart> existingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId);

        if (existingCart.isPresent()){
            this.shoppingCart = existingCart.get();
        }else{
            this.shoppingCart = new ShoppingCart(ownerId);
        }
    };

    @Override
    public void addProductToCart(AlbumDTO album, int amount) throws RemoteException {
        LineItem item = new LineItem(album.getMediumType(),album.getTitle(),amount,album.getPrice());
        this.shoppingCart.addLineItem(item);
    }


}

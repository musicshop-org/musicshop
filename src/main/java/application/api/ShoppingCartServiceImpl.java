package application.api;

import domain.LineItem;
import domain.ShoppingCart;
import infrastructure.ShoppingCartRepository;
import infrastructure.ShoppingCartRepositoryImpl;

import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.LineItemDTO;
import sharedrmi.application.dto.ShoppingCartDTO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShoppingCartServiceImpl extends UnicastRemoteObject implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCart shoppingCart;

    public ShoppingCartServiceImpl(UUID ownerId) throws RemoteException {
        this.shoppingCartRepository = new ShoppingCartRepositoryImpl();

        Optional<ShoppingCart> existingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId);

        if (existingCart.isEmpty())
            this.shoppingCart = shoppingCartRepository.createShoppingCart(ownerId);
        else
            this.shoppingCart = existingCart.get();
    }

    public ShoppingCartServiceImpl(UUID ownerId, ShoppingCartRepository repo) throws RemoteException {
        super();

        this.shoppingCartRepository = repo;
        this.shoppingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId).get();
    }

    @Override
    public ShoppingCartDTO getCart() throws RemoteException {
        List<LineItemDTO> lineItemsDTO = new LinkedList<>();

        for (LineItem item : shoppingCart.getLineItems()) {
            lineItemsDTO.add(new LineItemDTO(
                    item.getMediumType(),
                    item.getName(),
                    item.getQuantity(),
                    item.getPrice()
            ));
        }

        return new ShoppingCartDTO(shoppingCart.getOwnerId(), lineItemsDTO);
    }

    @Override
    public void addProductToCart(AlbumDTO album, int amount) throws RemoteException {
        LineItem item = new LineItem(album.getMediumType(), album.getTitle(), amount, album.getPrice());
        this.shoppingCart.addLineItem(item);
    }

    @Override
    public void changeQuantity(LineItemDTO lineItemDTO, int quantity)throws RemoteException {
        LineItem lineItem = new LineItem(lineItemDTO.getMediumType(), lineItemDTO.getName(), lineItemDTO.getQuantity(), lineItemDTO.getPrice());
        this.shoppingCart.changeQuantity(lineItem, quantity);
    }

    @Override
    public void removeProductFromCart(LineItemDTO lineItemDTO) throws RemoteException {
        LineItem lineItem = new LineItem(lineItemDTO.getMediumType(), lineItemDTO.getName(), lineItemDTO.getQuantity(), lineItemDTO.getPrice());
        this.shoppingCart.removeLineItem(lineItem);
    }
}

package application;

import application.api.SessionFacade;

import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;
import sharedrmi.domain.valueobjects.Role;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class SessionFacadeImpl extends UnicastRemoteObject implements SessionFacade {

    private final List<Role> roles;
    private final String username;
    private final ShoppingCartService shoppingCartService;

    private final ProductService productService = new ProductServiceImpl();

    public SessionFacadeImpl(List<Role> roles, String username) throws RemoteException {
        this.roles = roles;
        this.username = username;

        this.shoppingCartService = new ShoppingCartServiceImpl(username);
    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String title) throws RemoteException {
        return this.productService.findAlbumsBySongTitle(title);
    }

    @Override
    public List<SongDTO> findSongsByTitle(String title) throws RemoteException {
        return this.productService.findSongsByTitle(title);
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String name) throws RemoteException {
        return this.productService.findArtistsByName(name);
    }

    @Override
    public ShoppingCartDTO getCart() throws RemoteException {
        return this.shoppingCartService.getCart();
    }

    @Override
    public void addProductToCart(AlbumDTO albumDTO, int i) throws RemoteException {
        this.shoppingCartService.addProductToCart(albumDTO, i);
    }

    @Override
    public void changeQuantity(CartLineItemDTO cartLineItemDTO, int i) throws RemoteException {
        this.shoppingCartService.changeQuantity(cartLineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(CartLineItemDTO cartLineItemDTO) throws RemoteException {
        this.shoppingCartService.removeProductFromCart(cartLineItemDTO);
    }

    @Override
    public void clearCart() throws RemoteException {
        this.shoppingCartService.clearCart();
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }
}

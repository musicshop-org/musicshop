package application;

import domain.valueobjects.Role;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;

import java.rmi.RemoteException;
import java.util.List;

public class SessionFacadeImpl implements SessionFacade {

    List<Role> roles;
    String username;

    ProductService productService;
    ShoppingCartService shoppingCartService;

    public SessionFacadeImpl(List<Role> roles, String username) {
        this.roles = roles;
        this.username = username;
    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String s) throws RemoteException {
        return productService.findAlbumsBySongTitle(s);
    }

    @Override
    public List<SongDTO> findSongsByTitle(String s) throws RemoteException {
        return productService.findSongsByTitle(s);
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String s) throws RemoteException {
        return productService.findArtistsByName(s);
    }

    @Override
    public ShoppingCartDTO getCart() throws RemoteException {
        return shoppingCartService.getCart();
    }

    @Override
    public void addProductToCart(AlbumDTO albumDTO, int i) throws RemoteException {
        shoppingCartService.addProductToCart(albumDTO, i);
    }

    @Override
    public void changeQuantity(LineItemDTO lineItemDTO, int i) throws RemoteException {
        shoppingCartService.changeQuantity(lineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(LineItemDTO lineItemDTO) throws RemoteException {
        shoppingCartService.removeProductFromCart(lineItemDTO);
    }
}

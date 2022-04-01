package application;

import domain.valueobjects.Role;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;

import java.rmi.RemoteException;
import java.util.List;

public class SessionFacadeImpl implements SessionFacade {

    private final List<Role> roles;
    private final String username;
    private final ProductService productService = new ProductServiceImpl();
    private final ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl();

    public SessionFacadeImpl(List<Role> roles, String username) {
        this.roles = roles;
        this.username = username;
    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String s) throws RemoteException {
        return this.productService.findAlbumsBySongTitle(s);
    }

    @Override
    public List<SongDTO> findSongsByTitle(String s) throws RemoteException {
        return this.productService.findSongsByTitle(s);
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String s) throws RemoteException {
        return this.productService.findArtistsByName(s);
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
    public void changeQuantity(LineItemDTO lineItemDTO, int i) throws RemoteException {
        this.shoppingCartService.changeQuantity(lineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(LineItemDTO lineItemDTO) throws RemoteException {
        this.shoppingCartService.removeProductFromCart(lineItemDTO);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getUsername() {
        return username;
    }
}

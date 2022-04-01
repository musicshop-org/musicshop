package application;

import application.api.SessionFacade;
import domain.valueobjects.Role;

import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;

import java.rmi.RemoteException;
import java.util.List;

public class SessionFacadeImpl implements SessionFacade {

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
    public List<AlbumDTO> findAlbumsBySongTitle(String title) {
        return this.productService.findAlbumsBySongTitle(title);
    }

    @Override
    public List<SongDTO> findSongsByTitle(String title) {
        return this.productService.findSongsByTitle(title);
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String name) {
        return this.productService.findArtistsByName(name);
    }

    @Override
    public ShoppingCartDTO getCart() {
        return this.shoppingCartService.getCart();
    }

    @Override
    public void addProductToCart(AlbumDTO albumDTO, int i) {
        this.shoppingCartService.addProductToCart(albumDTO, i);
    }

    @Override
    public void changeQuantity(LineItemDTO lineItemDTO, int i) {
        this.shoppingCartService.changeQuantity(lineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(LineItemDTO lineItemDTO) {
        this.shoppingCartService.removeProductFromCart(lineItemDTO);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getUsername() {
        return username;
    }
}

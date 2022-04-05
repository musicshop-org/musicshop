package application;

import application.api.SessionFacade;

import sharedrmi.application.api.CustomerService;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;
import sharedrmi.domain.valueobjects.Role;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class SessionFacadeImpl extends UnicastRemoteObject implements SessionFacade {

    private final List<Role> roles;
    private final String username;
    private final ShoppingCartService shoppingCartService;
    private CustomerService customerService;

    private final ProductService productService = new ProductServiceImpl();

    public SessionFacadeImpl(List<Role> roles, String username) throws RemoteException {
        this.roles = roles;
        this.username = username;

        this.shoppingCartService = new ShoppingCartServiceImpl(username);

        try {
            customerService = (CustomerService) Naming.lookup("rmi://localhost/CustomerService");
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }

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
    public void changeQuantity(CartLineItemDTO lineItemDTO, int i) throws RemoteException {
        this.shoppingCartService.changeQuantity(lineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(CartLineItemDTO lineItemDTO) throws RemoteException {
        this.shoppingCartService.removeProductFromCart(lineItemDTO);
    }

    @Override
    public List<CustomerDTO> findCustomersByName(String name) {

        List<CustomerDTO> customers = new LinkedList<>();
        try {
            customers = customerService.findCustomersByName(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return customers;
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

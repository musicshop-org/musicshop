package application;

import application.api.SessionFacade;

import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;
import sharedrmi.domain.valueobjects.InvoiceId;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.NoPermissionException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;

public class SessionFacadeImpl extends UnicastRemoteObject implements SessionFacade {

    private final List<Role> roles;
    private final String username;
    private final ShoppingCartService shoppingCartService;

    private final ProductService productService = new ProductServiceImpl();
    private final InvoiceService invoiceService = new InvoiceServiceImpl();

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
    public ShoppingCartDTO getCart() throws RemoteException, NoPermissionException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                return this.shoppingCartService.getCart();
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public void addProductToCart(AlbumDTO albumDTO, int i) throws RemoteException, NoPermissionException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                this.shoppingCartService.addProductToCart(albumDTO, i);
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public void changeQuantity(CartLineItemDTO cartLineItemDTO, int i) throws RemoteException, NoPermissionException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                this.shoppingCartService.changeQuantity(cartLineItemDTO, i);
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public void removeProductFromCart(CartLineItemDTO cartLineItemDTO) throws RemoteException, NoPermissionException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                this.shoppingCartService.removeProductFromCart(cartLineItemDTO);
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public void clearCart() throws RemoteException, NoPermissionException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                this.shoppingCartService.clearCart();
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws RemoteException {
        return invoiceService.findInvoiceById(invoiceId);
    }

    @Override
    public void createInvoice(InvoiceDTO invoiceDTO) throws RemoteException {
        invoiceService.createInvoice(invoiceDTO);
    }
}

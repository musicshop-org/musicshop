package communication.rmi;

import application.api.LoginService;
import application.LoginServiceImpl;
import application.api.SessionFacade;

import sharedrmi.application.dto.*;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.communication.rmi.RMIController;
import sharedrmi.domain.valueobjects.InvoiceId;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.NoPermissionException;
import javax.security.auth.login.FailedLoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIControllerImpl extends UnicastRemoteObject implements RMIController {

    private final SessionFacade sessionFacade;

    protected RMIControllerImpl(String username, String password) throws FailedLoginException, RemoteException {
        super();

        LoginService loginService = new LoginServiceImpl();
        this.sessionFacade = loginService.login(username, password);
    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String s) throws RemoteException {
        return sessionFacade.findAlbumsBySongTitle(s);
    }

    @Override
    public List<SongDTO> findSongsByTitle(String s) throws RemoteException {
        return sessionFacade.findSongsByTitle(s);
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String s) throws RemoteException {
        return sessionFacade.findArtistsByName(s);
    }

    @Override
    public ShoppingCartDTO getCart() throws RemoteException, NoPermissionException {
        return sessionFacade.getCart();
    }

    @Override
    public void addProductToCart(AlbumDTO albumDTO, int i) throws RemoteException, NoPermissionException {
        sessionFacade.addProductToCart(albumDTO, i);
    }

    @Override
    public void changeQuantity(CartLineItemDTO lineItemDTO, int i) throws RemoteException, NoPermissionException {
        sessionFacade.changeQuantity(lineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(CartLineItemDTO lineItemDTO) throws RemoteException, NoPermissionException {
        sessionFacade.removeProductFromCart(lineItemDTO);
    }

    @Override
    public void clearCart() throws RemoteException, NoPermissionException {

    }

    @Override
    public List<Role> getRoles() throws RemoteException {
        return sessionFacade.getRoles();
    }

    @Override
    public String getUsername() throws RemoteException {
        return sessionFacade.getUsername();
    }

    @Override
    public List<CustomerDTO> findCustomersByName(String name) throws RemoteException {
        return sessionFacade.findCustomersByName(name);
    }

    @Override
    public InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws RemoteException, NoPermissionException, InvoiceNotFoundException {
        return null;
    }

    @Override
    public void createInvoice(InvoiceDTO invoiceDTO) throws RemoteException, NoPermissionException {

    }

    @Override
    public void returnInvoiceLineItem(InvoiceId invoiceId, InvoiceLineItemDTO invoiceLineItemDTO, int i) throws RemoteException, NoPermissionException, InvoiceNotFoundException {

    }
}

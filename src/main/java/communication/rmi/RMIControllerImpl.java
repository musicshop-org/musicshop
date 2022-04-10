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
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIControllerImpl extends UnicastRemoteObject implements RMIController {

    private final SessionFacade sessionFacade;

    protected RMIControllerImpl(String username, String password) throws FailedLoginException, RemoteException, AccessDeniedException {
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
    public void changeQuantity(CartLineItemDTO cartLineItemDTO, int i) throws RemoteException, NoPermissionException {
        sessionFacade.changeQuantity(cartLineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(CartLineItemDTO cartLineItemDTO) throws RemoteException, NoPermissionException {
        sessionFacade.removeProductFromCart(cartLineItemDTO);
    }

    @Override
    public void clearCart() throws RemoteException, NoPermissionException {
        sessionFacade.clearCart();
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
    public InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws RemoteException, InvoiceNotFoundException {
        return sessionFacade.findInvoiceById(invoiceId);
    }

    @Override
    public void createInvoice(InvoiceDTO invoiceDTO) throws RemoteException {
        sessionFacade.createInvoice(invoiceDTO);
    }

    @Override
    public void returnInvoiceLineItem(InvoiceId invoiceId, InvoiceLineItemDTO invoiceLineItemDTO, int returnQuantity) throws RemoteException, InvoiceNotFoundException {
        sessionFacade.returnInvoiceLineItem(invoiceId,invoiceLineItemDTO,returnQuantity);
    }
}
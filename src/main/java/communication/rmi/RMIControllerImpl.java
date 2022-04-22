package communication.rmi;

import application.api.LoginService;
import application.LoginServiceImpl;
import application.api.SessionFacade;

import sharedrmi.application.dto.*;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.communication.rmi.RMIController;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.InvoiceId;
import sharedrmi.domain.valueobjects.Role;

import javax.jms.JMSException;
import javax.naming.NoPermissionException;
import javax.security.auth.login.FailedLoginException;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIControllerImpl extends UnicastRemoteObject implements RMIController {

    private final SessionFacade sessionFacade;

    protected RMIControllerImpl(String username, String password) throws FailedLoginException, RemoteException, AccessDeniedException {
        super(1099);

        LoginService loginService = new LoginServiceImpl();
        this.sessionFacade = loginService.login(username, password);
    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String s) throws RemoteException {
        return sessionFacade.findAlbumsBySongTitle(s);
    }

    @Override
    public AlbumDTO findAlbumByAlbumTitleAndMedium(String s, MediumType mediumType) throws RemoteException, AlbumNotFoundException {
        return sessionFacade.findAlbumByAlbumTitleAndMedium(s, mediumType);
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
    public void decreaseStockOfAlbum(String title, MediumType mediumType, int decreaseAmount) throws RemoteException, NoPermissionException, NotEnoughStockException {
        sessionFacade.decreaseStockOfAlbum(title, mediumType, decreaseAmount);
    }

    @Override
    public void increaseStockOfAlbum(String title, MediumType mediumType, int increaseAmount) throws RemoteException, NoPermissionException {
        sessionFacade.increaseStockOfAlbum(title, mediumType, increaseAmount);
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
    public InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws RemoteException, NoPermissionException, InvoiceNotFoundException {
        return sessionFacade.findInvoiceById(invoiceId);
    }

    @Override
    public List<CustomerDTO> findCustomersByName(String name) throws RemoteException, NoPermissionException {
        return sessionFacade.findCustomersByName(name);
    }

    @Override
    public void createInvoice(InvoiceDTO invoiceDTO) throws RemoteException, NoPermissionException, AlbumNotFoundException, NotEnoughStockException {
        sessionFacade.createInvoice(invoiceDTO);
    }

    @Override
    public void returnInvoiceLineItem(InvoiceId invoiceId, InvoiceLineItemDTO invoiceLineItemDTO, int returnQuantity) throws RemoteException, NoPermissionException, InvoiceNotFoundException {
        sessionFacade.returnInvoiceLineItem(invoiceId,invoiceLineItemDTO,returnQuantity);
    }

    @Override
    public void publish(List<String> topics, MessageDTO messageDTO) throws RemoteException, NoPermissionException {
        sessionFacade.publish(topics, messageDTO);
    }

    @Override
    public List<String> getAllTopics() throws RemoteException {
        return sessionFacade.getAllTopics();
    }

    @Override
    public List<String> getSubscribedTopicsForUser(String username) throws RemoteException {
        return sessionFacade.getSubscribedTopicsForUser(username);
    }
}
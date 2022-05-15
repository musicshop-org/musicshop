package communication.rmi;

import application.LoginServiceImpl;
import application.api.LoginService;
import application.api.SessionFacade;
import sharedrmi.application.dto.*;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.application.exceptions.UserNotFoundException;
import sharedrmi.communication.rmi.RMIController;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.InvoiceId;
import sharedrmi.domain.valueobjects.Role;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.naming.NoPermissionException;
import javax.security.auth.login.FailedLoginException;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

@Remote(RMIController.class)
@Stateful
public class RMIControllerImpl implements RMIController {

    private SessionFacade sessionFacade;


    public RMIControllerImpl() {
    }

    @Override
    public void login(String username, String password) throws FailedLoginException, RemoteException, AccessDeniedException {
        LoginService loginService = new LoginServiceImpl();
        this.sessionFacade = loginService.login(username, password);
    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String s) {
        return sessionFacade.findAlbumsBySongTitle(s);
    }

    @Override
    public AlbumDTO findAlbumByAlbumTitleAndMedium(String s, MediumType mediumType) throws AlbumNotFoundException {
        return sessionFacade.findAlbumByAlbumTitleAndMedium(s, mediumType);
    }

    @Override
    public List<SongDTO> findSongsByTitle(String s) {
        return sessionFacade.findSongsByTitle(s);
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String s) {
        return sessionFacade.findArtistsByName(s);
    }

    @Override
    public AlbumDTO findAlbumByAlbumId(String albumId) throws AlbumNotFoundException {
        return sessionFacade.findAlbumByAlbumId(albumId);
    }

    @Override
    public void decreaseStockOfAlbum(String title, MediumType mediumType, int decreaseAmount) throws NoPermissionException, NotEnoughStockException {
        sessionFacade.decreaseStockOfAlbum(title, mediumType, decreaseAmount);
    }

    @Override
    public void increaseStockOfAlbum(String title, MediumType mediumType, int increaseAmount) throws NoPermissionException {
        sessionFacade.increaseStockOfAlbum(title, mediumType, increaseAmount);
    }

    @Override
    public ShoppingCartDTO getCart() throws NoPermissionException {
        return sessionFacade.getCart();
    }

    @Override
    public void addProductToCart(AlbumDTO albumDTO, int i) throws NoPermissionException {
        sessionFacade.addProductToCart(albumDTO, i);
    }

    @Override
    public void addSongsToCart(List<SongDTO> songs) throws NoPermissionException {
        sessionFacade.addSongsToCart(songs);
    }

    @Override
    public void changeQuantity(CartLineItemDTO cartLineItemDTO, int i) throws NoPermissionException {
        sessionFacade.changeQuantity(cartLineItemDTO, i);
    }

    @Override
    public void removeProductFromCart(CartLineItemDTO cartLineItemDTO) throws NoPermissionException {
        sessionFacade.removeProductFromCart(cartLineItemDTO);
    }

    @Override
    public void clearCart() throws NoPermissionException {
        sessionFacade.clearCart();
    }

    @Override
    public List<Role> getRoles() {
        return sessionFacade.getRoles();
    }

    @Override
    public String getUsername() {
        return sessionFacade.getUsername();
    }

    @Override
    public InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws NoPermissionException, InvoiceNotFoundException {
        return sessionFacade.findInvoiceById(invoiceId);
    }

    @Override
    public List<CustomerDTO> findCustomersByName(String name) throws NoPermissionException, RemoteException {
        return sessionFacade.findCustomersByName(name);
    }

    @Override
    public InvoiceId createInvoice(InvoiceDTO invoiceDTO) throws NoPermissionException, AlbumNotFoundException, NotEnoughStockException {
        return sessionFacade.createInvoice(invoiceDTO);
    }

    @Override
    public void returnInvoiceLineItem(InvoiceId invoiceId, InvoiceLineItemDTO invoiceLineItemDTO, int returnQuantity) throws NoPermissionException, InvoiceNotFoundException {
        sessionFacade.returnInvoiceLineItem(invoiceId,invoiceLineItemDTO,returnQuantity);
    }

    @Override
    public void publish(List<String> topics, MessageDTO messageDTO) throws NoPermissionException {
        sessionFacade.publish(topics, messageDTO);
    }

    @Override
    public List<String> getAllTopics() {
        return sessionFacade.getAllTopics();
    }

    @Override
    public List<String> getSubscribedTopicsForUser(String username) {
        return sessionFacade.getSubscribedTopicsForUser(username);
    }

    @Override
    public void changeLastViewed(String username, LocalDateTime lastViewed) throws UserNotFoundException {
        sessionFacade.changeLastViewed(username, lastViewed);
    }

    @Override
    public LocalDateTime getLastViewedForUser(String username) throws UserNotFoundException {
        return sessionFacade.getLastViewedForUser(username);

    }

    @Override
    public boolean subscribe(String topic, String username) {
        return sessionFacade.subscribe(topic, username);
    }

    @Override
    public boolean unsubscribe(String topic, String username) {
        return sessionFacade.unsubscribe(topic, username);
    }
}
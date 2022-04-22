package application;

import application.api.SessionFacade;

import sharedrmi.application.api.*;
import sharedrmi.application.dto.*;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.InvoiceId;
import sharedrmi.domain.valueobjects.Role;

import javax.jms.JMSException;
import javax.naming.NoPermissionException;
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
    private final InvoiceService invoiceService = new InvoiceServiceImpl();
    private final MessageProducerService messageProducerService = new MessageProducerServiceImpl();
    private final UserService userService = new UserServiceImpl();

    public SessionFacadeImpl(List<Role> roles, String username) throws RemoteException {
        this.roles = roles;
        this.username = username;

        this.shoppingCartService = new ShoppingCartServiceImpl(username);

        try {
            customerService = (CustomerService) Naming.lookup("rmi://10.0.40.163/CustomerService");
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String title) throws RemoteException {
        return this.productService.findAlbumsBySongTitle(title);
    }

    @Override
    public AlbumDTO findAlbumByAlbumTitleAndMedium(String title, MediumType mediumType) throws RemoteException, AlbumNotFoundException {
        return this.productService.findAlbumByAlbumTitleAndMedium(title, mediumType);
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
    public void decreaseStockOfAlbum(String title, MediumType mediumType, int decreaseAmount) throws RemoteException, NoPermissionException, NotEnoughStockException {
        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                this.productService.decreaseStockOfAlbum(title, mediumType, decreaseAmount);
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public void increaseStockOfAlbum(String title, MediumType mediumType, int increaseAmount) throws RemoteException, NoPermissionException {
        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                this.productService.increaseStockOfAlbum(title, mediumType, increaseAmount);
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
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
    public List<CustomerDTO> findCustomersByName(String name) throws NoPermissionException, RemoteException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                return customerService.findCustomersByName(name);
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
    public InvoiceDTO findInvoiceById(InvoiceId invoiceId) throws RemoteException, NoPermissionException, InvoiceNotFoundException {
        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                return invoiceService.findInvoiceById(invoiceId);
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public void createInvoice(InvoiceDTO invoiceDTO) throws RemoteException, NoPermissionException, AlbumNotFoundException, NotEnoughStockException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                invoiceService.createInvoice(invoiceDTO);
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public void returnInvoiceLineItem(InvoiceId invoiceId, InvoiceLineItemDTO invoiceLineItemDTO, int returnQuantity) throws RemoteException, NoPermissionException, InvoiceNotFoundException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.SALESPERSON)) {
                invoiceService.returnInvoiceLineItem(invoiceId, invoiceLineItemDTO, returnQuantity);
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public void publish(List<String> topics, MessageDTO messageDTO) throws RemoteException, NoPermissionException {

        for (Role role : this.roles)
        {
            if (role.equals(Role.OPERATOR)) {
                messageProducerService.publish(topics, messageDTO);
                return;
            }
        }

        throw new NoPermissionException("no permission to call this method!");
    }

    @Override
    public List<String> getAllTopics() throws RemoteException {
        return userService.getAllTopics();
    }

    @Override
    public List<String> getSubscribedTopicsForUser(String username) throws RemoteException {
        return userService.getSubscribedTopicsForUser(username);
    }

    @Override
    public boolean subscribe(String topic, String username) throws RemoteException {
        return userService.subscribe(topic, username);
    }

    @Override
    public boolean unsubscribe(String topic, String username) throws RemoteException {
        return userService.unsubscribe(topic, username);
    }
}

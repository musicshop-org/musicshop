package communication.rest;

import application.InvoiceServiceImpl;
import application.ProductServiceImpl;
import application.ShoppingCartServiceImpl;
import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.application.dto.ShoppingCartDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.InvoiceId;

import javax.naming.NoPermissionException;
import javax.ws.rs.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

@Path("")
public class RestController {

    private final ProductService productService = new ProductServiceImpl();
    private final ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl("PythonTestClient");
    private final InvoiceService invoiceService = new InvoiceServiceImpl();

    public RestController() {}

    @GET
    @Produces("text/html")
    public String welcome() {
        return "<h1> welcome to our music shop :) </h1>";
    }

    @GET
    @Path("/albums/{songTitle}")
    @Produces("application/json")
    public List<AlbumDTO> findAlbumsBySongTitle (@PathParam("songTitle") String songTitle) {
        return productService.findAlbumsBySongTitle(songTitle);
    }

    @POST
    @Path("/albums/addToCart")
    @Consumes("application/json")
    @Produces("text/plain")
    public boolean addToCart(AlbumDTO album) {

        try {
            shoppingCartService.addProductToCart(album, album.getQuantityToAddToCart());
        } catch (NoPermissionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @POST
    @Path("/albums/buyProducts")
    @Consumes("application/json")
    @Produces("text/plain")
    public boolean buyProduct(List <InvoiceLineItemDTO> invoiceLineItemDTO) {
        InvoiceDTO invoiceDTO = new InvoiceDTO(new InvoiceId(), invoiceLineItemDTO, PaymentMethod.CASH, LocalDate.now());
        try {
            invoiceService.createInvoice(invoiceDTO);
        } catch (NotEnoughStockException | NoPermissionException | AlbumNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    @GET
    @Path("/shoppingCart/display")
    @Produces("application/json")
    public ShoppingCartDTO displayShoppingCart() {

        try {
            return shoppingCartService.getCart();
        } catch (NoPermissionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GET
    @Path("/shoppingCart/clear")
    @Produces("text/plain")
    public boolean clearShoppingCart() {

        try {
            shoppingCartService.clearCart();
        } catch (NoPermissionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}

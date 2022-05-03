package communication.rest;

import application.ProductServiceImpl;
import application.ShoppingCartServiceImpl;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ShoppingCartDTO;

import javax.naming.NoPermissionException;
import javax.ws.rs.*;
import java.rmi.RemoteException;
import java.util.List;

@Path("")
public class RestController {

    private final ProductService productService = new ProductServiceImpl();
    private final ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl("PythonTestClient");

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

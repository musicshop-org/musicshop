package view;

import application.ProductServiceImpl;
import application.ShoppingCartServiceImpl;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;

import javax.naming.NoPermissionException;
import javax.ws.rs.*;
import java.rmi.RemoteException;
import java.util.List;

@Path("")
public class RestController {

    private final ProductService productService = new ProductServiceImpl();
    private final ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl();

    public RestController() throws RemoteException {}

    @GET
    @Produces("text/html")
    public String welcome() {
        return "<h1> welcome to our musicshop :) </h1>";
    }

    @GET
    @Path("/albums/{songTitle}")
    @Produces("application/json")
    public List<AlbumDTO> findAlbumsBySongTitle (@PathParam("songTitle") String songTitle) throws RemoteException {
        return productService.findAlbumsBySongTitle(songTitle);
    }

    @POST
    @Path("/albums/addToCart")
    @Consumes("application/json")
    @Produces("text/plain")
    public boolean addToCart(AlbumDTO album) {
        // needed: title of album, medium type, price, stock, quantity
        try {
            shoppingCartService.addProductToCart(album, album.getQuantityToAddToCart());
        } catch (RemoteException | NoPermissionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}

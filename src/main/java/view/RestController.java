package view;

import application.ProductServiceImpl;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;

import javax.ws.rs.*;
import java.rmi.RemoteException;
import java.util.List;

@Path("")
public class RestController {

    private final ProductService productService = new ProductServiceImpl();

    public RestController() throws RemoteException {}

    @GET
    @Produces("text/html")
    public String welcome() {
        return "<h1> welcome to our music shop :) </h1>";
    }

    @GET
    @Path("/albums/{songTitle}")
    @Produces("application/json")
    public List<AlbumDTO> findAlbumsBySongTitle (@PathParam("songTitle") String songTitle) throws RemoteException {
        return productService.findAlbumsBySongTitle(songTitle);
    }
}

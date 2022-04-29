package view;

import application.Person;
import application.ProductServiceImpl;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.AlbumDTOString;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.rmi.RemoteException;
import java.util.List;

@Path("")
public class RestController {

    private final ProductService productService = new ProductServiceImpl();

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

    // only for testing -> will be removed later
    @POST
    @Path("/abc")
    @Consumes("application/json")
    @Produces("application/json")
    public String jsonTest (Person person) {
        System.out.println("Person: " +  person.getFirst());
        return "F: " + person.getFirst() + ", L: " + person.getLast() + ", A: " + person.getAge();
    }
}

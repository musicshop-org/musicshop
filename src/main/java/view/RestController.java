package view;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("")
public class RestController {

    @GET
    @Path("/getAlbums/{albumTitle}")
    @Produces("text/plain")
    public String hello(@PathParam("albumTitle") String albumTitle) {
        return "Album: " + albumTitle;
    }
}

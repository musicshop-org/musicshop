package view;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("")
public class RestController {

    @GET
    @Path("/hello-world")
    @Produces("text/plain")
    public String hello() {
        return "hello world :)";
    }
}

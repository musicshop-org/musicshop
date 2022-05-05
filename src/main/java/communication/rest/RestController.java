package communication.rest;

import application.ProductServiceImpl;
import application.ShoppingCartServiceImpl;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ShoppingCartDTO;
import sharedrmi.application.dto.UserDataDTO;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.NoPermissionException;
import javax.ws.rs.*;
import java.util.Collections;
import java.util.List;

@Path("")
public class RestController {

    private final ProductService productService = new ProductServiceImpl();
    private final ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl("PythonTestClient");
    private final RestLoginServiceImpl restLoginServiceImpl = new RestLoginServiceImpl();


    public RestController() {}


    @GET
    @Produces("text/html")
    public String welcome() {
        return "<h1> welcome to our music shop :) </h1>";
    }


    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("text/plain")
    public String login(UserDataDTO userData) {

        String emailAddress = userData.getEmailAddress();
        String password = userData.getPassword();

        if (restLoginServiceImpl.checkCredentials(emailAddress, password))
            return JwtManager.createJWT(emailAddress, 900000);

        return "";
    }


    @GET
    @Path("/albums/{songTitle}")
    @Produces("application/json")
    public List<AlbumDTO> findAlbumsBySongTitle (@PathParam("songTitle") String songTitle, @HeaderParam("Authorization") String jwt_Token) {

        if (JwtManager.isValidToken(jwt_Token) && isCustomerOrLicensee(jwt_Token))
            return productService.findAlbumsBySongTitle(songTitle);

        return Collections.emptyList();
    }


    @POST
    @Path("/albums/addToCart")
    @Consumes("application/json")
    @Produces("text/plain")
    public boolean addToCart(AlbumDTO album, @HeaderParam("Authorization") String jwt_Token) throws NoPermissionException {

        if (JwtManager.isValidToken(jwt_Token) && isCustomerOrLicensee(jwt_Token)) {
            shoppingCartService.addProductToCart(album, album.getQuantityToAddToCart());
            return true;
        }

        return false;
    }


    @GET
    @Path("/shoppingCart/display")
    @Produces("application/json")
    public ShoppingCartDTO displayShoppingCart(@HeaderParam("Authorization") String jwt_Token) throws NoPermissionException {

        if (JwtManager.isValidToken(jwt_Token) && isCustomerOrLicensee(jwt_Token)) {
            return shoppingCartService.getCart();
        }

        return null;
    }


    @GET
    @Path("/shoppingCart/clear")
    @Produces("text/plain")
    public boolean clearShoppingCart(@HeaderParam("Authorization") String jwt_Token) throws NoPermissionException {

        if (JwtManager.isValidToken(jwt_Token) && isCustomerOrLicensee(jwt_Token)) {
            shoppingCartService.clearCart();
            return true;
        }

        return false;
    }


    private boolean isCustomerOrLicensee(String jwt_Token) {
        List<Role> userRoles = JwtManager.getRoles(jwt_Token);

        return userRoles.contains(Role.CUSTOMER) || userRoles.contains(Role.LICENSEE);
    }
}

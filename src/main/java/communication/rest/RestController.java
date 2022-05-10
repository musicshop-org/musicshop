package communication.rest;

import application.ProductServiceImpl;
import application.ShoppingCartServiceImpl;
import communication.rest.api.RestLoginService;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ShoppingCartDTO;
import sharedrmi.application.dto.UserDataDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.NoPermissionException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;


@Path("")
public class RestController {

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
        RestLoginService restLoginService = new RestLoginServiceImpl();

        String emailAddress = userData.getEmailAddress();
        String password = userData.getPassword();

        if (restLoginService.checkCredentials(emailAddress, password) && restLoginService.getRole(emailAddress).contains(Role.LICENSEE))
            return JwtManager.createJWT(emailAddress, 900000);

        return "";
    }

    @POST
    @Path("/loginWeb")
    @Consumes("application/json")
    @Produces("text/plain")
    public String loginWeb(UserDataDTO userData) {
        RestLoginService restLoginService = new RestLoginServiceImpl();

        String emailAddress = userData.getEmailAddress();
        String password = userData.getPassword();

        if (restLoginService.checkCredentials(emailAddress, password) && restLoginService.getRole(emailAddress).contains(Role.CUSTOMER))
            return JwtManager.createJWT(emailAddress, 900000);

        return "";
    }


    @GET
    @Path("/albums/{songTitle}")
    @Produces("application/json")
    public List<AlbumDTO> findAlbumsBySongTitle(@PathParam("songTitle") String songTitle, @HeaderParam("Authorization") String jwt_Token) {


        ProductService productService = new ProductServiceImpl();
        return productService.findAlbumsBySongTitle(songTitle);

    }


    @GET
    @Path("/album/{albumId}")
    @Produces("application/json")
    public AlbumDTO findAlbumByAlbumId(@PathParam("albumId") String albumId, @HeaderParam("Authorization") String jwt_Token) {


        ProductService productService = new ProductServiceImpl();
        try {
            return productService.findAlbumByAlbumId(albumId);
        } catch (AlbumNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }


    @POST
    @Path("/albums/addToCart")
    @Consumes("application/json")
    @Produces("text/plain")
    public boolean addToCart(AlbumDTO album, @HeaderParam("Authorization") String jwt_Token) throws NoPermissionException {

        if (JwtManager.isValidToken(jwt_Token) && isCustomerOrLicensee(jwt_Token)) {
            ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(JwtManager.getEmailAddress(jwt_Token));
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
            ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(JwtManager.getEmailAddress(jwt_Token));
            return shoppingCartService.getCart();
        }

        return null;
    }


    @GET
    @Path("/shoppingCart/clear")
    @Produces("text/plain")
    public boolean clearShoppingCart(@HeaderParam("Authorization") String jwt_Token) throws NoPermissionException {

        if (JwtManager.isValidToken(jwt_Token) && isCustomerOrLicensee(jwt_Token)) {
            ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(JwtManager.getEmailAddress(jwt_Token));
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

package communication.rest;

import application.InvoiceServiceImpl;
import application.ProductServiceImpl;
import application.ShoppingCartServiceImpl;
import communication.rest.api.RestLoginService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;

import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.InvoiceId;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.NoPermissionException;
import javax.ws.rs.*;
import java.time.LocalDate;
import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "OpenAPIDefinition",
                description = "Music shop REST API"
        ),
        servers = {
                @Server(
                        url = "https://localhost:8080/musicshop-1.0",
                        description = "Music shop REST"
                )
        }
)
@Path("")
public class RestController {

    private final ProductService productService = new ProductServiceImpl();
    private final ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl("PythonTestClient");
    private final InvoiceService invoiceService = new InvoiceServiceImpl();

    public RestController() {
    }

    @GET
    @Produces("text/html")
    public String welcome() {
        return "<h1> welcome to our music shop :) </h1>";
    }


    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("text/plain")
    @ApiResponse(responseCode = "200", description = "Login ok", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
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
    @ApiResponse(responseCode = "200", description = "Login successful", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
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
    @ApiResponse(responseCode = "200", description = "Album found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
    public List<AlbumDTO> findAlbumsBySongTitle(@PathParam("songTitle") String songTitle, @HeaderParam("Authorization") String jwt_Token) {


        ProductService productService = new ProductServiceImpl();
        return productService.findAlbumsBySongTitleDigital(songTitle);

    }


    @GET
    @Path("/album/{albumId}")
    @Produces("application/json")
    @ApiResponse(responseCode = "200", description = "Album found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
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
    @ApiResponse(responseCode = "200", description = "Add to cart successful", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
    public boolean addToCart(AlbumDTO album, @HeaderParam("Authorization") String jwt_Token) throws NoPermissionException {

        if (JwtManager.isValidToken(jwt_Token) && isCustomerOrLicensee(jwt_Token)) {
            ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(JwtManager.getEmailAddress(jwt_Token));
            shoppingCartService.addProductToCart(album, album.getQuantityToAddToCart());
            return true;
        }

        return false;
    }


    @POST
    @Path("/shoppingCart/buyProducts")
    @Consumes("application/json")
    @Produces("text/plain")
    @ApiResponse(responseCode = "200", description = "Buy product successful", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
    public boolean buyProduct(List<InvoiceLineItemDTO> invoiceLineItemDTOs, @HeaderParam("Authorization") String jwt_Token) throws AlbumNotFoundException, NoPermissionException, NotEnoughStockException, NotEnoughStockException {

        if (JwtManager.isValidToken(jwt_Token) && isCustomerOrLicensee(jwt_Token)) {
            InvoiceDTO invoiceDTO = new InvoiceDTO(
                    new InvoiceId(),
                    invoiceLineItemDTOs,
                    PaymentMethod.CASH,
                    LocalDate.now(),
                    null
            );

            invoiceService.createInvoice(invoiceDTO);
            return true;
        }

        return false;
    }


    @GET
    @Path("/shoppingCart/display")
    @Produces("application/json")
    @ApiResponse(responseCode = "200", description = "Success", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
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
    @ApiResponse(responseCode = "200", description = "Success", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
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

package communication.rest;

import application.InvoiceServiceImpl;
import application.ProductServiceImpl;
import application.ShoppingCartServiceImpl;
import communication.rest.api.RestLoginService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "OpenAPIDefinition",
                description = "Music shop REST API",
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080/musicshop-1.0",
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
    // TODO:: API Response
    public String login(UserDataDTO userData) {
        RestLoginService restLoginService = new RestLoginServiceImpl();

        String emailAddress = userData.getEmailAddress();
        String password = userData.getPassword();

        if (restLoginService.checkCredentials(emailAddress, password) && restLoginService.getRole(emailAddress).contains(Role.LICENSEE)) {
            return JwtManager.createJWT(emailAddress, 900000);
        }

        return "";
    }


    @POST
    @Path("/loginWeb")
    @Consumes("application/json")
    @Produces("text/plain")
    @ApiResponse(responseCode = "200", description = "Login successful", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
    // TODO:: API Response
    public String loginWeb(UserDataDTO userData) {
        RestLoginService restLoginService = new RestLoginServiceImpl();

        String emailAddress = userData.getEmailAddress();
        String password = userData.getPassword();

        if (restLoginService.checkCredentials(emailAddress, password) && restLoginService.getRole(emailAddress).contains(Role.CUSTOMER)) {
            return JwtManager.createJWT(emailAddress, 900000);
        }

        return "";
    }


    @GET
    @Path("/albums/{songTitle}")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Album(s) found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON,
                                            array = @ArraySchema(schema = @Schema(implementation = AlbumDTO.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No album found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response findAlbumsBySongTitle(@PathParam("songTitle") String songTitle, @HeaderParam("Authorization") String jwt_Token) {
        ProductService productService = new ProductServiceImpl();

        List<AlbumDTO> albumDTOList = productService.findAlbumsBySongTitleDigital(songTitle);

        if (albumDTOList.size() <= 0) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("No album found")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(albumDTOList)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/album/{albumId}")
    @Produces("application/json")
    @ApiResponse(responseCode = "200", description = "Album found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
    // TODO:: API Response
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
    @Path("/albums/addAlbumsToCart")
    @Consumes("application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Add to cart successful",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No authorization provided",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "No permission",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            array = @ArraySchema(schema = @Schema(implementation = String.class))
                                    )
                            }
                    )
            })
    public boolean addToCart(AlbumDTO album, @HeaderParam("CartUUID") String UUID) throws NoPermissionException {

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);
        shoppingCartService.addAlbumsToCart(album, album.getQuantityToAddToCart());
        return true;

//        Response.Status status;
//        String responseText;
//
//        if (jwt_Token == null || jwt_Token.equals("")) {
//
//            status = Response.Status.UNAUTHORIZED;
//            responseText = "No authorization provided";
//
//        } else if (!JwtManager.isValidToken(jwt_Token)) {
//
//            status = Response.Status.UNAUTHORIZED;
//            responseText = "Invalid JWT token provided";
//
//        } else if (isCustomerOrLicensee(jwt_Token)) {
//
//            ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(JwtManager.getEmailAddress(jwt_Token));
//            shoppingCartService.addProductToCart(album, album.getQuantityToAddToCart());
//
//            status = Response.Status.OK;
//            responseText = "Add to cart successful";
//
//        } else {
//
//            status = Response.Status.FORBIDDEN;
//            responseText = "No permission";
//
//        }
//
//        return Response
//                .status(status)
//                .entity(responseText)
//                .type(MediaType.TEXT_PLAIN)
//                .build();
    }


    @POST
    @Path("/albums/addSongsToCart")
    @Consumes("application/json")
    @Produces("text/plain")
    // TODO:: API Response
    public boolean addSongsToCart(List<SongDTO> songs, @HeaderParam("CartUUID") String UUID) throws NoPermissionException {

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);
        shoppingCartService.addSongsToCart(songs);
        return true;

    }


    @POST
    @Path("/albums/addSongsFromAlbumToCart")
    @Consumes("application/json")
    @Produces("text/plain")
    // TODO:: API Response
    public boolean addSongsFromAlbumToCart(AlbumDTO album, @HeaderParam("CartUUID") String UUID) throws NoPermissionException {

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);
        shoppingCartService.addSongsToCart(new LinkedList<>(album.getSongs()));
        return true;

    }


    @POST
    @Path("/shoppingCart/buyProducts")
    @Consumes("application/json")
    @Produces("text/plain")
    @ApiResponse(responseCode = "200", description = "Buy product successful", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Method name not found", useReturnTypeSchema = true)
    // TODO:: API Response
    public boolean buyProduct(List<InvoiceLineItemDTO> invoiceLineItemDTOs, @HeaderParam("Authorization") String jwt_Token) throws AlbumNotFoundException, NoPermissionException, NotEnoughStockException {

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
    // TODO:: API Response
    public ShoppingCartDTO displayShoppingCart(@HeaderParam("CartUUID") String UUID) throws NoPermissionException {

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);
        return shoppingCartService.getCart();

    }


    @POST
    @Path("/shoppingCart/removeLineItemFromCart")
    @Consumes("application/json")
    @Produces("text/plain")
    // TODO:: API Response
    public boolean removeLineItemFromCart(CartLineItemDTO cartLineItemDTO, @HeaderParam("CartUUID") String UUID) throws NoPermissionException {

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);
        shoppingCartService.removeLineItemFromCart(cartLineItemDTO);
        return true;

    }


    @GET
    @Path("/shoppingCart/clear")
    @Produces("text/plain")
    // TODO:: API Response
    public boolean clearShoppingCart(@HeaderParam("CartUUID") String UUID) throws NoPermissionException {

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);
        shoppingCartService.clearCart();
        return true;

    }


    private boolean isCustomerOrLicensee(String jwt_Token) {
        List<Role> userRoles = JwtManager.getRoles(jwt_Token);

        return userRoles.contains(Role.CUSTOMER) || userRoles.contains(Role.LICENSEE);
    }
}

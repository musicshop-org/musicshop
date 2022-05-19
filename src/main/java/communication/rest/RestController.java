package communication.rest;

import application.InvoiceServiceImpl;
import application.ProductServiceImpl;
import application.ShoppingCartServiceImpl;
import communication.rest.api.RestLoginService;

import communication.rest.util.ResponseWrapper;
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
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameter not ok",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Username or password wrong",
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
                            responseCode = "500",
                            description = "Internal server error, please contact our support",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response login(UserDataDTO userData) {

        if (userData == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        RestLoginService restLoginService = new RestLoginServiceImpl();

        String emailAddress = userData.getEmailAddress();
        String password = userData.getPassword();

        return ResponseWrapper
                .builder()
                .checkCredentials(restLoginService.checkCredentials(emailAddress, password))
                .checkRoles(restLoginService.getRole(emailAddress).contains(Role.LICENSEE))
                .response(() -> Response
                        .status(Response.Status.OK)
                        .entity(JwtManager.createJWT(emailAddress, 900000))
                        .type(MediaType.TEXT_PLAIN)
                        .build())
                .build();
    }


    @POST
    @Path("/loginWeb")
    @Consumes("application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameter not ok",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Username or password wrong",
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
                            responseCode = "500",
                            description = "Internal server error, please contact our support",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response loginWeb(UserDataDTO userData) {

        if (userData == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        RestLoginService restLoginService = new RestLoginServiceImpl();

        String emailAddress = userData.getEmailAddress();
        String password = userData.getPassword();

        return ResponseWrapper
                .builder()
                .checkCredentials(restLoginService.checkCredentials(emailAddress, password))
                .checkRoles(restLoginService.getRole(emailAddress).contains(Role.CUSTOMER))
                .response(() -> Response
                        .status(Response.Status.OK)
                        .entity(JwtManager.createJWT(emailAddress, 900000))
                        .type(MediaType.TEXT_PLAIN)
                        .build())
                .build();
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
                            responseCode = "400",
                            description = "Request parameter not ok",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
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
    public Response findAlbumsBySongTitle(@PathParam("songTitle") String songTitle) {

        if (songTitle == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return ResponseWrapper
                .builder()
                .response(() -> {

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
                })
                .build();
    }

    @GET
    @Path("/album/{albumId}")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Album found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON,
                                            schema = @Schema(implementation = AlbumDTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameter not ok",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
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
    public Response findAlbumByAlbumId(@PathParam("albumId") String albumId) {

        if (albumId == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return ResponseWrapper
                .builder()
                .response(() -> {

                    try {
                        return Response
                                .status(Response.Status.OK)
                                .entity(productService.findAlbumByAlbumId(albumId))
                                .type(MediaType.APPLICATION_JSON)
                                .build();
                    } catch (AlbumNotFoundException e) {
                        return Response
                                .status(Response.Status.NOT_FOUND)
                                .entity("No album found")
                                .type(MediaType.TEXT_PLAIN)
                                .build();
                    }
                })
                .build();
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
                            responseCode = "400",
                            description = "Request parameter not ok",
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
                            description = "Shopping cart not found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error, please contact our support",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response addAlbumsToCart(AlbumDTO album, @HeaderParam("CartUUID") String UUID) {

        if (album == null || UUID == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return ResponseWrapper
                .builder()
                .response(() -> {

                    ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);

                    try {
                        shoppingCartService.addAlbumsToCart(album, album.getQuantityToAddToCart());
                    } catch (NoPermissionException e) {
                        return Response
                                .status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity("Internal server error, please contact our support")
                                .type(MediaType.TEXT_PLAIN)
                                .build();
                    }

                    return Response
                            .status(Response.Status.OK)
                            .entity("Add to cart successful")
                            .type(MediaType.TEXT_PLAIN)
                            .build();
                })
                .build();
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

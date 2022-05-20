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

import domain.Album;
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

        if (restLoginService.checkCredentials(emailAddress, password)) {
            if (restLoginService.getRole(emailAddress).contains(Role.LICENSEE)) {
                return Response
                        .status(Response.Status.OK)
                        .entity(JwtManager.createJWT(emailAddress, 900000))
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            } else {
                return Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("No permission")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }
        }

        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Username or password wrong")
                .type(MediaType.TEXT_PLAIN)
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

        if (restLoginService.checkCredentials(emailAddress, password)) {
            if (restLoginService.getRole(emailAddress).contains(Role.CUSTOMER)) {
                return Response
                        .status(Response.Status.OK)
                        .entity(JwtManager.createJWT(emailAddress, 900000))
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            } else {
                return Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("No permission")
                        .type(MediaType.TEXT_PLAIN)
                        .build();
            }
        }

        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Username or password wrong")
                .type(MediaType.TEXT_PLAIN)
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

//        return ResponseWrapper
//                .builder()
//                .response(() -> {
//                    List<AlbumDTO> albumDTOList = productService.findAlbumsBySongTitleDigital(songTitle);
//
//                    if (albumDTOList.size() <= 0) {
//                        return Response
//                                .status(Response.Status.NOT_FOUND)
//                                .entity("No album found")
//                                .type(MediaType.TEXT_PLAIN)
//                                .build();
//                    }
//
//                    return Response
//                            .status(Response.Status.OK)
//                            .entity(albumDTOList)
//                            .type(MediaType.APPLICATION_JSON)
//                            .build();
//                })
//                .build();
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
    public Response findAlbumsBySongTitlePhysical(@PathParam("songTitle") String songTitle) {

        if (songTitle == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        List<AlbumDTO> albumDTOList = productService.findAlbumsBySongTitle(songTitle);

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
                            responseCode = "403",
                            description = "No permission",
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

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);

        try {
            shoppingCartService.addAlbumsToCart(album, album.getQuantityToAddToCart());
        } catch (NoPermissionException e) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("No permission")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Add to cart successful")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }


    @POST
    @Path("/albums/addSongsToCart")
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
                            responseCode = "403",
                            description = "No permission",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response addSongsToCart(List<SongDTO> songs, @HeaderParam("CartUUID") String UUID) {

        if (songs == null || UUID == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);

        try {
            shoppingCartService.addSongsToCart(songs);
        } catch (NoPermissionException e) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("No permission")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Add to cart successful")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }


    @POST
    @Path("/albums/addSongsFromAlbumToCart")
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
                            responseCode = "403",
                            description = "No permission",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response addSongsFromAlbumToCart(AlbumDTO album, @HeaderParam("CartUUID") String UUID) {

        if (album == null || UUID == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);

        try {
            shoppingCartService.addSongsToCart(new LinkedList<>(album.getSongs()));
        } catch (NoPermissionException e) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("No permission")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Add to cart successful")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    @GET
    @Path("/shoppingCart/display")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON,
                                            schema = @Schema(implementation = ShoppingCartDTO.class)
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
                            responseCode = "403",
                            description = "No permission",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response displayShoppingCart(@HeaderParam("CartUUID") String UUID) {

        if (UUID == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);

        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(shoppingCartService.getCart())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (NoPermissionException e) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("No permission")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }


    @POST
    @Path("/shoppingCart/removeLineItemFromCart")
    @Consumes("application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Remove item successful",
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
                            responseCode = "403",
                            description = "No permission",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response removeLineItemFromCart(CartLineItemDTO cartLineItemDTO, @HeaderParam("CartUUID") String UUID) {

        if (cartLineItemDTO == null || UUID == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);

        try {
            shoppingCartService.removeLineItemFromCart(cartLineItemDTO);
        } catch (NoPermissionException e) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("No permission")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Remove item successful")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }


    @GET
    @Path("/shoppingCart/clear")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "CLear cart successful",
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
                            responseCode = "403",
                            description = "No permission",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response clearShoppingCart(@HeaderParam("CartUUID") String UUID) {

        if (UUID == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Request parameter not ok")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        ShoppingCartService shoppingCartService = new ShoppingCartServiceImpl(UUID);

        try {
            shoppingCartService.clearCart();
        } catch (NoPermissionException e) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("No permission")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("CLear cart successful")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    @POST
    @Path("/shoppingCart/buyProducts")
    @Consumes("application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Buy product successful",
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
                            responseCode = "401",
                            description = "Invalid JWT token provided",
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
                            description = "Album not found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Not enough stock available",
                            content = {
                                    @Content(
                                            mediaType = MediaType.TEXT_PLAIN,
                                            schema = @Schema(implementation = String.class)
                                    )
                            }
                    )
            })
    public Response buyProduct(List<InvoiceLineItemDTO> invoiceLineItemDTOs, @HeaderParam("Authorization") String jwt_Token) {

        Response.Status status;
        Object entity;

        if (invoiceLineItemDTOs == null || jwt_Token == null) {
            status = Response.Status.BAD_REQUEST;
            entity = "Request parameter not ok";
        } else if (jwt_Token.equals("")) {
            status = Response.Status.UNAUTHORIZED;
            entity = "No authorization provided";
        } else if (!JwtManager.isValidToken(jwt_Token)) {
            status = Response.Status.UNAUTHORIZED;
            entity = "Invalid JWT token provided";
        } else if (!this.isCustomerOrLicensee(jwt_Token)) {
            status = Response.Status.FORBIDDEN;
            entity = "No permission";
        } else {
            InvoiceDTO invoiceDTO = new InvoiceDTO(
                    new InvoiceId(),
                    invoiceLineItemDTOs,
                    PaymentMethod.CASH,
                    LocalDate.now(),
                    null
            );

            try {
                invoiceService.createInvoice(invoiceDTO);

                status = Response.Status.OK;
                entity = "Buy product successful";
            } catch (NoPermissionException e) {
                status = Response.Status.FORBIDDEN;
                entity = "No permission";
            } catch (NotEnoughStockException e) {
                status = Response.Status.CONFLICT;
                entity = "Not enough stock available";
            } catch (AlbumNotFoundException e) {
                status = Response.Status.NOT_FOUND;
                entity = "Album not found";
            }
        }

        return Response
                .status(status)
                .entity(entity)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    private boolean isCustomerOrLicensee(String jwt_Token) {
        List<Role> userRoles = JwtManager.getRoles(jwt_Token);

        return userRoles.contains(Role.CUSTOMER) || userRoles.contains(Role.LICENSEE);
    }
}

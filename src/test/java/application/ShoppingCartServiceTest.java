package application;

import domain.CartLineItem;
import domain.ShoppingCart;
import domain.repositories.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.CartLineItemDTO;
import sharedrmi.application.dto.ShoppingCartDTO;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    private ShoppingCart givenCart;
    private ShoppingCartService shoppingCartService;

    @Mock
    private static ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    void initMockAndService() throws RemoteException {
        String ownerId = UUID.randomUUID().toString();

        List<CartLineItem> lineItems = new LinkedList<>();
        lineItems.add(new CartLineItem(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18)));
        lineItems.add(new CartLineItem(MediumType.CD, "BAM BAM", 20, BigDecimal.valueOf(36)));

        givenCart = new ShoppingCart(ownerId, lineItems);

        Mockito.when(shoppingCartRepository.findShoppingCartByOwnerId(givenCart.getOwnerId())).thenReturn(Optional.of(givenCart));
        shoppingCartService = new ShoppingCartServiceImpl(givenCart.getOwnerId(), shoppingCartRepository);
    }

    @Test
    void when_displayCart_return_correct_dto() throws RemoteException {
        //when
        ShoppingCartDTO cartDTO = shoppingCartService.getCart();

        //then
        assertEquals(givenCart.getOwnerId(), cartDTO.getOwnerId());
        assertAll("LineItem 1",
                () -> assertEquals(givenCart.getCartLineItems().get(0).getName(), cartDTO.getCartLineItems().get(0).getName()),
                () -> assertEquals(givenCart.getCartLineItems().get(0).getQuantity(), cartDTO.getCartLineItems().get(0).getQuantity()),
                () -> assertEquals(givenCart.getCartLineItems().get(0).getPrice(), cartDTO.getCartLineItems().get(0).getPrice()),
                () -> assertEquals(givenCart.getCartLineItems().get(0).getMediumType(), cartDTO.getCartLineItems().get(0).getMediumType())
        );
        assertAll("LineItem 2",
                () -> assertEquals(givenCart.getCartLineItems().get(1).getName(), cartDTO.getCartLineItems().get(1).getName()),
                () -> assertEquals(givenCart.getCartLineItems().get(1).getQuantity(), cartDTO.getCartLineItems().get(1).getQuantity()),
                () -> assertEquals(givenCart.getCartLineItems().get(1).getPrice(), cartDTO.getCartLineItems().get(1).getPrice()),
                () -> assertEquals(givenCart.getCartLineItems().get(1).getMediumType(), cartDTO.getCartLineItems().get(1).getMediumType())
        );
    }

    @Test
    void given_album_when_addProduct_return_new_entry() throws RemoteException {
        //given
        int quantity = 2;
        AlbumDTO album = new AlbumDTO("TestAlbum", BigDecimal.TEN, 10, MediumType.CD, LocalDate.now(), new AlbumId(), "TestLabel", null);

        //when
        shoppingCartService.addProductToCart(album, quantity);

        //then
        ShoppingCartDTO cartDTO = shoppingCartService.getCart();
        assertEquals(givenCart.getOwnerId(), cartDTO.getOwnerId());
        assertAll("LineItem 3",
                () -> assertEquals(album.getTitle(), cartDTO.getCartLineItems().get(2).getName()),
                () -> assertEquals(quantity, cartDTO.getCartLineItems().get(2).getQuantity()),
                () -> assertEquals(album.getPrice(), cartDTO.getCartLineItems().get(2).getPrice()),
                () -> assertEquals(album.getMediumType(), cartDTO.getCartLineItems().get(2).getMediumType())
        );
    }

    @Test
    void given_newquantity_when_changeQuantity_return_new_quantity() throws RemoteException {
        //given
        int newQuantity = 10;

        CartLineItemDTO lineItemDTO = new CartLineItemDTO(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18));

        //when
        shoppingCartService.changeQuantity(lineItemDTO, newQuantity);

        //then
        assertEquals(newQuantity, shoppingCartService.getCart().getCartLineItems().get(0).getQuantity());
    }

    @Test
    void given_cart_when_removeProductFromCart_return_new_size() throws RemoteException {
        //given
        int expected = 1;
        CartLineItemDTO lineItemDTO = new CartLineItemDTO(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18));

        //when
        shoppingCartService.removeProductFromCart(lineItemDTO);

        //then
        assertEquals(expected, shoppingCartService.getCart().getCartLineItems().size());
    }

    @Test
    void given_cart_when_clearCart_return_empty_cart() throws RemoteException {
        //given
        int expected = 0;

        //when
        shoppingCartService.clearCart();

        //then
        assertEquals(expected, shoppingCartService.getCart().getCartLineItems().size());
    }
}

package application.api;

import domain.LineItem;
import domain.ShoppingCart;
import infrastructure.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.ShoppingCartDTO;
import sharedrmi.domain.enums.MediumType;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShoppingCartServiceTest {

    private static ShoppingCartService shoppingCartService;
    private static ShoppingCart givenCart;
    @Mock
    private static ShoppingCartRepository shoppingCartRepository;

    @BeforeAll
    static void init() throws RemoteException {
        UUID ownerId = UUID.randomUUID();
        shoppingCartService = new ShoppingCartServiceImpl(ownerId);

        List<LineItem> lineItems = new LinkedList<>();
        lineItems.add(new LineItem(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18)));
        lineItems.add(new LineItem(MediumType.CD,"BAM BAM", 20, BigDecimal.valueOf(36)));

        givenCart = new ShoppingCart(ownerId, lineItems);
        Mockito.when(shoppingCartRepository.findShoppingCartByOwnerId(ownerId)).thenReturn(Optional.of(givenCart));
    }

    @Test
    void when_displayCart_return_correct_dto() throws RemoteException {
        //when
        ShoppingCartDTO cartDTO = shoppingCartService.displayCart();

        //then
        assertEquals(givenCart.getOwnerId(),cartDTO.getOwnerId());
        assertAll("LineItems",
                () -> assertEquals(givenCart.getLineItems().get(0),cartDTO.getLineItems().get(0)),
                () -> assertEquals(givenCart.getLineItems().get(1),cartDTO.getLineItems().get(1))
        );
    }
}

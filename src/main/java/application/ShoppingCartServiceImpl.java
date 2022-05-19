package application;

import domain.CartLineItem;
import domain.ShoppingCart;
import domain.repositories.ShoppingCartRepository;
import infrastructure.ShoppingCartRepositoryImpl;

import jakarta.transaction.Transactional;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.CartLineItemDTO;
import sharedrmi.application.dto.ShoppingCartDTO;
import sharedrmi.application.dto.SongDTO;
import sharedrmi.domain.enums.ProductType;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCart shoppingCart;

    public ShoppingCartServiceImpl() {
        super();

        this.shoppingCartRepository = new ShoppingCartRepositoryImpl();
        this.shoppingCart = new ShoppingCart();
    }

    public ShoppingCartServiceImpl(String ownerId) {
        super();

        this.shoppingCartRepository = new ShoppingCartRepositoryImpl();

        Optional<ShoppingCart> existingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId);

        if (existingCart.isEmpty()) {
            this.shoppingCart = shoppingCartRepository.createShoppingCart(ownerId);
        } else {
            this.shoppingCart = existingCart.get();
        }
    }

    public ShoppingCartServiceImpl(String ownerId, ShoppingCartRepository repo) {
        super();

        this.shoppingCartRepository = repo;
        this.shoppingCart = shoppingCartRepository.findShoppingCartByOwnerId(ownerId).get();
    }

    @Transactional
    @Override
    public ShoppingCartDTO getCart() {
        List<CartLineItemDTO> cartLineItemsDTO = new LinkedList<>();

        for (CartLineItem cartLineItem : shoppingCart.getCartLineItems()) {
            cartLineItemsDTO.add(new CartLineItemDTO(
                    cartLineItem.getMediumType(),
                    cartLineItem.getName(),
                    cartLineItem.getQuantity(),
                    cartLineItem.getPrice(),
                    cartLineItem.getStock(),
                    cartLineItem.getImageUrl(),
                    cartLineItem.getProductType()
            ));
        }

        return new ShoppingCartDTO(shoppingCart.getOwnerId(), cartLineItemsDTO);
    }

    @Transactional
    @Override
    public void addAlbumsToCart(AlbumDTO album, int amount) {
        CartLineItem cartLineItem = new CartLineItem(
                album.getMediumType(),
                album.getTitle(), amount,
                album.getPrice(),
                album.getStock(),
                album.getImageUrl(),
                ProductType.ALBUM
        );

        this.shoppingCart.addLineItem(cartLineItem);
    }

    @Transactional
    @Override
    public void addSongsToCart(List<SongDTO> songs) {

        for (SongDTO song : songs) {

            String imageUrl;
            if (!song.getInAlbum().isEmpty()) {
                imageUrl = song.getInAlbum().iterator().next().getImageUrl();
            } else {
                imageUrl = "";
            }

            CartLineItem cartLineItem = new CartLineItem(
                    song.getMediumType(),
                    song.getTitle(), 1,
                    song.getPrice(),
                    song.getStock(),
                    imageUrl,
                    ProductType.SONG
            );

            this.shoppingCart.addLineItem(cartLineItem);
        }
    }

    @Transactional
    @Override
    public void changeQuantity(CartLineItemDTO cartLineItemDTO, int quantity) {
        CartLineItem cartLineItem = new CartLineItem(
                cartLineItemDTO.getMediumType(),
                cartLineItemDTO.getName(),
                cartLineItemDTO.getQuantity(),
                cartLineItemDTO.getPrice(),
                cartLineItemDTO.getStock(),
                cartLineItemDTO.getImageUrl(),
                cartLineItemDTO.getProductType()
        );

        this.shoppingCart.changeQuantity(cartLineItem, quantity);
    }

    @Transactional
    @Override
    public void removeLineItemFromCart(CartLineItemDTO cartLineItemDTO) {
        CartLineItem cartLineItem = new CartLineItem(
                cartLineItemDTO.getMediumType(),
                cartLineItemDTO.getName(),
                cartLineItemDTO.getQuantity(),
                cartLineItemDTO.getPrice(),
                cartLineItemDTO.getStock(),
                cartLineItemDTO.getImageUrl(),
                cartLineItemDTO.getProductType()
        );

        this.shoppingCart.removeLineItem(cartLineItem);
    }

    @Transactional
    @Override
    public void clearCart() {
        this.shoppingCart.clear();
    }
}

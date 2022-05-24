package application;

import domain.Album;
import domain.CartLineItem;
import domain.ShoppingCart;
import domain.Song;
import domain.repositories.ProductRepository;
import domain.repositories.ShoppingCartRepository;
import infrastructure.ProductRepositoryImpl;
import infrastructure.ShoppingCartRepositoryImpl;

import jakarta.transaction.Transactional;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;
import sharedrmi.domain.enums.ProductType;

import javax.naming.NoPermissionException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCart shoppingCart;
    private ProductRepository productRepository = new ProductRepositoryImpl();

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
                    cartLineItem.getProductType(),
                    cartLineItem.getArtists(),
                    cartLineItem.getProductId()
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
                ProductType.ALBUM,
                album.getSongs().stream().map(song -> song.getArtists().stream().map(artist -> artist.getName()).findFirst().get()).collect(Collectors.toList()),
                album.getLongId()
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
                    ProductType.SONG,
                    song.getArtists().stream().map(artist -> artist.getName()).collect(Collectors.toList()),
                    song.getLongId()
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
                cartLineItemDTO.getProductType(),
                cartLineItemDTO.getProductId()
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
                cartLineItemDTO.getProductType(),
                cartLineItemDTO.getProductId()
        );

        this.shoppingCart.removeLineItem(cartLineItem);
    }

    @Override
    public void buyShoppingCart() {
        List<CartLineItem> cartLineItems = shoppingCart.getCartLineItems();
        Set<SongDTO> songDTOs = new HashSet<>();
        //TODO: getSongs from CartLineItem
        for (CartLineItem cartLineItem : cartLineItems) {
            if (cartLineItem.getProductType().equals(ProductType.ALBUM)) {
                Album album = productRepository.findAlbumByLongId(cartLineItem.getProductId());
                for (Song song : album.getSongs()) {
                    songDTOs.add(SongDTO.builder()
                            .title(song.getTitle())
                            .price(song.getPrice())
                            .stock(song.getStock())
                            .mediumType(song.getMediumType())
                            .releaseDate(song.getReleaseDate().toString())
                            .genre(song.getGenre())
                            .artists(song.getArtists()
                                    .stream()
                                    .map(artist -> new ArtistDTO(artist.getName()))
                                    .collect(Collectors.toList())
                            ).inAlbum(Set.of(new AlbumDTO(
                                    album.getTitle(),
                                    album.getImageUrl() != null ? album.getImageUrl() : " ",
                                    album.getPrice(),
                                    album.getStock(),
                                    album.getMediumType(),
                                    album.getReleaseDate().toString(),
                                    album.getAlbumId(),
                                    album.getLabel(),
                                    Collections.emptySet(),
                                    0,
                                    album.getId()
                            )))
                            .longId(song.getId())
                            .build());
                }
            }
            else if (cartLineItem.getProductType().equals(ProductType.SONG)){
                Song song = productRepository.findSongByLongId(cartLineItem.getProductId());
                songDTOs.add(new SongDTO(
                        song.getTitle(),
                        song.getPrice(),
                        song.getStock(),
                        song.getMediumType(),
                        song.getReleaseDate().toString(),
                        song.getGenre(),
                        song.getArtists()
                                .stream()
                                .map(artist -> new ArtistDTO(artist.getName()))
                                .collect(Collectors.toList()),
                        song.getInAlbum()
                                .stream()
                                .map(album -> new AlbumDTO(
                                        album.getTitle(),
                                        album.getImageUrl() != null ? album.getImageUrl() : " ",
                                        album.getPrice(),
                                        album.getStock(),
                                        album.getMediumType(),
                                        album.getReleaseDate().toString(),
                                        album.getAlbumId(),
                                        album.getLabel(),
                                        Collections.emptySet(),
                                        0,
                                        album.getId()
                                ))
                                .collect(Collectors.toSet()),
                        song.getId()
                ));
            }
        }


        //TODO: tell microservice which songs have been bought
        for (SongDTO songDTO : songDTOs) {
            System.out.println("Songs bought: " + songDTO.getTitle());
        }

    }

    @Transactional
    @Override
    public void clearCart() {
        this.shoppingCart.clear();
    }
}

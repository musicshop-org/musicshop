package application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import domain.*;
import domain.repositories.ProductRepository;
import domain.repositories.ShoppingCartRepository;
import infrastructure.ProductRepositoryImpl;
import infrastructure.ShoppingCartRepositoryImpl;

import jakarta.transaction.Transactional;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.*;
import sharedrmi.domain.enums.ProductType;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCart shoppingCart;
    private final ProductRepository productRepository = new ProductRepositoryImpl();

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

        this.shoppingCart = shoppingCartRepository
                .findShoppingCartByOwnerId(ownerId)
                .orElse(new ShoppingCart(ownerId));
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
                album.getSongs()
                        .stream()
                        .map(song -> song.getArtists()
                                .stream()
                                .map(ArtistDTO::getName)
                                .findFirst()
                                .orElse("")
                        ).collect(Collectors.toList()),
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
                    song.getArtists()
                            .stream()
                            .map(ArtistDTO::getName)
                            .collect(Collectors.toList()),
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
    public void buyShoppingCart(String ownerId) {
        List<CartLineItem> cartLineItems = shoppingCart.getCartLineItems();
        Set<SongDTO> songDTOs = new HashSet<>();

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
            } else if (cartLineItem.getProductType().equals(ProductType.SONG)) {
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


        // tell microservice which songs have been bought

        ObjectMapper objectMapper = new ObjectMapper();

        // set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            // convert songDTOs to stringifyJSON
            String stringifyJSON = objectMapper.writeValueAsString(songDTOs);

            System.out.println("Convert List of person objects to JSON:");
            System.out.println(stringifyJSON);

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

                HttpPost httpPost = new HttpPost("http://localhost:9001/playlist/addSongs");

                httpPost.setEntity(new StringEntity(stringifyJSON, ContentType.APPLICATION_JSON));
                httpPost.setHeader("ownerId", ownerId);
                httpPost.setHeader("accept", "text/plain");
                httpPost.setHeader("Content-Type", "application/json");

                HttpResponse response = httpClient.execute(httpPost);

                if (response.getStatusLine().getStatusCode() == 200) {
                    shoppingCart.clear();
                } else {
                    throw new ServerException("Error while buying cart");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public void clearCart() {
        this.shoppingCart.clear();
    }
}

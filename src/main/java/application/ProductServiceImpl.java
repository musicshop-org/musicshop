package application;

import domain.Artist;
import jakarta.transaction.Transactional;

import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ArtistDTO;
import sharedrmi.application.dto.SongDTO;

import domain.Album;
import domain.Song;

import domain.repositories.ProductRepository;
import infrastructure.ProductRepositoryImpl;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;


public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl() {
        super();
        this.productRepository = new ProductRepositoryImpl();
    }

    public ProductServiceImpl(ProductRepository productRepository) {
        super();
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String title) {

        List<AlbumDTO> albumDTOs = new LinkedList<>();

        Set<Album> albums = productRepository.findAlbumsBySongTitle(title);
        List<MediumType> acceptedMediums = List.of(MediumType.CD,MediumType.VINYL);

        for (Album album : albums) {
            if (acceptedMediums.contains(album.getMediumType())){
                Set<SongDTO> songDTOs = new HashSet<>();

                for (Song song : album.getSongs()) {
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
                            Collections.emptySet()
                    ));
                }

                albumDTOs.add(new AlbumDTO(
                        album.getTitle(),
                        album.getPrice(),
                        album.getStock(),
                        album.getMediumType(),
                        album.getReleaseDate().toString(),
                        album.getAlbumId(),
                        album.getLabel(),
                        songDTOs,
                        0
                ));
            }
        }

        return albumDTOs;
    }

    @Transactional
    @Override
    public List<AlbumDTO> findAlbumsBySongTitleDigital(String title) {

        List<AlbumDTO> albumDTOs = new LinkedList<>();

        Set<Album> albums = productRepository.findAlbumsBySongTitle(title);
        List<MediumType> acceptedMediums = List.of(MediumType.DIGITAL);

        for (Album album : albums) {
            if (acceptedMediums.contains(album.getMediumType())){
                Set<SongDTO> songDTOs = new HashSet<>();

                for (Song song : album.getSongs()) {
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
                            Collections.emptySet()
                    ));
                }

                albumDTOs.add(new AlbumDTO(
                        album.getTitle(),
                        album.getPrice(),
                        album.getStock(),
                        album.getMediumType(),
                        album.getReleaseDate().toString(),
                        album.getAlbumId(),
                        album.getLabel(),
                        songDTOs,
                        0
                ));
            }
        }

        return albumDTOs;
    }

    @Transactional
    @Override
    public AlbumDTO findAlbumByAlbumTitleAndMedium(String title, MediumType mediumType) throws AlbumNotFoundException {
        Album album = productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType);

        if (null == album) {
            throw new AlbumNotFoundException("album not found");
        }

        return AlbumDTO.builder()
                .title(album.getTitle())
                .price(album.getPrice())
                .stock(album.getStock())
                .mediumType(album.getMediumType())
                .releaseDate(album.getReleaseDate().toString())
                .albumId(album.getAlbumId())
                .label(album.getLabel())
                .songs(album.getSongs()
                        .stream()
                        .map(song -> SongDTO.builder()
                                .title(song.getTitle())
                                .artists(song.getArtists()
                                        .stream()
                                        .map(artist -> new ArtistDTO(artist.getName()))
                                        .collect(Collectors.toList())
                                )
                                .mediumType(song.getMediumType())
                                .price(song.getPrice())
                                .releaseDate(song.getReleaseDate().toString())
                                .genre(song.getGenre())
                                .stock(song.getStock())
                                .inAlbum(Collections.emptySet())
                                .build()
                        )
                        .collect(Collectors.toSet())
                )
                .build();
    }

    @Transactional
    @Override
    public AlbumDTO findAlbumByAlbumId(String albumId) throws AlbumNotFoundException {

        Optional<Album> albumOpt = productRepository.findAlbumByAlbumId(albumId);
        if(albumOpt.isPresent()) {
            Album album = albumOpt.get();
            return AlbumDTO.builder()
                    .title(album.getTitle())
                    .price(album.getPrice())
                    .stock(album.getStock())
                    .mediumType(album.getMediumType())
                    .releaseDate(album.getReleaseDate().toString())
                    .albumId(album.getAlbumId())
                    .label(album.getLabel())
                    .songs(album.getSongs()
                            .stream()
                            .map(song -> SongDTO.builder()
                                    .title(song.getTitle())
                                    .artists(song.getArtists()
                                            .stream()
                                            .map(artist -> new ArtistDTO(artist.getName()))
                                            .collect(Collectors.toList())
                                    )
                                    .mediumType(song.getMediumType())
                                    .price(song.getPrice())
                                    .releaseDate(song.getReleaseDate().toString())
                                    .genre(song.getGenre())
                                    .stock(song.getStock())
                                    .inAlbum(Collections.emptySet())
                                    .build()
                            )
                            .collect(Collectors.toSet())
                    )
                    .build();
        } else {
            throw new AlbumNotFoundException("album not found");
        }

    }

    @Transactional
    @Override
    public List<SongDTO> findSongsByTitle(String title) {
        List<SongDTO> songDTOs = new LinkedList<>();

        List<Song> songs = productRepository.findSongsByTitle(title);

        for (Song song : songs) {
            songDTOs.add(new SongDTO(
                    song.getTitle(),
                    song.getPrice(),
                    song.getStock(),
                    song.getMediumType(),
                    song.getReleaseDate().toString(),
                    song.getGenre(),
                    song.getArtists().stream().map(artist -> new ArtistDTO(artist.getName())).collect(Collectors.toList()),
                    Collections.emptySet()
            ));
        }

        return songDTOs;
    }

    @Transactional
    @Override
    public List<ArtistDTO> findArtistsByName(String name) {
        List<ArtistDTO> artistDTOs = new LinkedList<>();

        List<Artist> artists = productRepository.findArtistsByName(name);

        for (Artist artist : artists) {
            artistDTOs.add(new ArtistDTO(
                    artist.getName()
            ));
        }

        return artistDTOs;
    }

    @Transactional
    @Override
    public void decreaseStockOfAlbum(String title, MediumType mediumType, int decreaseAmount) throws NotEnoughStockException {
        Album album = productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType);
        if (decreaseAmount > album.getStock()){
            throw new NotEnoughStockException("not enough " + album.getTitle() + " available ... in stock: " + album.getStock() + ", in cart: " + decreaseAmount);
        }
        album.decreaseStock(decreaseAmount);
        productRepository.updateAlbum(album);
    }

    @Transactional
    @Override
    public void increaseStockOfAlbum(String title, MediumType mediumType, int increaseAmount) {
        Album album = productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType);
        album.increaseStock(increaseAmount);
        productRepository.updateAlbum(album);
    }

}

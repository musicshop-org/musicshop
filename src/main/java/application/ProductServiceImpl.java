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
import sharedrmi.domain.enums.MediumType;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;


public class ProductServiceImpl extends UnicastRemoteObject implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl() throws RemoteException {
        super();
        this.productRepository = new ProductRepositoryImpl();
    }

    public ProductServiceImpl(ProductRepository productRepository) throws RemoteException {
        super();
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String title) {

        List<AlbumDTO> albumDTOs = new LinkedList<>();

        Set<Album> albums = productRepository.findAlbumsBySongTitle(title);

        for (Album album : albums) {
            Set<SongDTO> songDTOs = new HashSet<>();

            for (Song song : album.getSongs()) {
                songDTOs.add(new SongDTO(
                        song.getTitle(),
                        song.getPrice(),
                        song.getStock(),
                        song.getMediumType(),
                        song.getReleaseDate(),
                        song.getGenre(),
                        song.getArtists().stream().map(artist -> new ArtistDTO(artist.getName())).collect(Collectors.toList()),
                        Collections.emptySet()
                ));
            }

            albumDTOs.add(new AlbumDTO(
                    album.getTitle(),
                    album.getPrice(),
                    album.getStock(),
                    album.getMediumType(),
                    album.getReleaseDate(),
                    album.getAlbumId(),
                    album.getLabel(),
                    songDTOs
            ));
        }

        return albumDTOs;
    }

    @Transactional
    @Override
    public AlbumDTO findAlbumByAlbumTitleAndMedium(String title, MediumType mediumType) throws RemoteException, AlbumNotFoundException {
        Album album = productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType);

        if (null == album) {
            throw new AlbumNotFoundException("album not found");
        }

        AlbumDTO albumDTO = AlbumDTO.builder()
                .title(album.getTitle())
                .price(album.getPrice())
                .stock(album.getStock())
                .mediumType(album.getMediumType())
                .releaseDate(album.getReleaseDate())
                .albumId(album.getAlbumId())
                .label(album.getLabel())
                .songs(album.getSongs().stream().map(song -> SongDTO.builder().title(song.getTitle()).build()).collect(Collectors.toSet()))
                .build();

        return albumDTO;
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
                    song.getReleaseDate(),
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
    public void decreaseStockOfAlbum(String title, MediumType mediumType, int decreaseAmount) {
        Album album = productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType);
        album.decreaseStock(decreaseAmount);
        productRepository.updateAlbum(album);
    }

    @Transactional
    @Override
    public void increaseStockOfAlbum(String title, MediumType mediumType, int increaseAmount) throws RemoteException {
        Album album = productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType);
        album.increaseStock(increaseAmount);
        productRepository.updateAlbum(album);
    }

}

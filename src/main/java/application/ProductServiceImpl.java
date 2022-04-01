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

import java.util.*;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl() {
        this.productRepository = new ProductRepositoryImpl();
    }

    public ProductServiceImpl(ProductRepository productRepository) {
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

}

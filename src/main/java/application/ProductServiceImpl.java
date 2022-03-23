package application;

import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ArtistDTO;
import sharedrmi.application.dto.SongDTO;

import domain.Album;
import domain.Song;

import infrastructure.ProductRepository;
import infrastructure.ProductRepositoryImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ProductServiceImpl extends UnicastRemoteObject implements ProductService {

    ProductRepository productRepository = new ProductRepositoryImpl();

    public ProductServiceImpl() throws RemoteException {
    }

    @Override
    public List<AlbumDTO> findAlbumsBySongTitle(String title) throws RemoteException {
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
                        null,
                        null
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

    @Override
    public List<SongDTO> findSongsByTitle(String title) throws RemoteException {
        // todo: implement method

        return null;
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String name) throws RemoteException {
        // todo: implement method

        return null;
    }

}

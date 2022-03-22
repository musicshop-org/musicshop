package application;

import domain.Album;
import domain.Song;
import jakarta.persistence.SecondaryTable;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ArtistDTO;
import sharedrmi.application.dto.SongDTO;

import infrastructure.ProductRepository;
import infrastructure.ProductRepositoryImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ProductServiceImpl extends UnicastRemoteObject  implements ProductService {

    ProductRepository productRepository = new ProductRepositoryImpl();

    public ProductServiceImpl() throws RemoteException {
    }

    @Override
    public List<AlbumDTO> findAlbumsByTitle(String title) throws RemoteException {
        List<AlbumDTO> albumDTOs = new LinkedList<>();
        Set<Album> albumTitles = productRepository.findAlbumsByTitle(title);

        for (Album albumTitle : albumTitles) {
            albumDTOs.add(new AlbumDTO(albumTitle.getTitle(), null, null, null, null, 0));
        }

        return albumDTOs;
    }

    @Override
    public List<SongDTO> findSongsByTitle(String title) throws RemoteException {
        List<SongDTO> songDTOs = new LinkedList<>();
        List<Song> songTitles = productRepository.findSongsByTitle(title);

        for (Song songTitle : songTitles) {
            songDTOs.add(new SongDTO(songTitle.getTitle(), null, null, 0, null, null, 0));
        }

        return songDTOs;
    }

    @Override
    public List<ArtistDTO> findArtistsByName(String name) throws RemoteException {
        List<ArtistDTO> artistDTOs = new LinkedList<>();
        List<String> artistNames = productRepository.findArtistsByName(name);

        for (String artistName : artistNames) {
            artistDTOs.add(new ArtistDTO(artistName));
        }

        return artistDTOs;
    }

}

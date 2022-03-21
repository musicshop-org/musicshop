package application;

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

public class ProductServiceImpl extends UnicastRemoteObject  implements ProductService {

    ProductRepository productRepository = new ProductRepositoryImpl();

    public ProductServiceImpl() throws RemoteException {
    }

    @Override
    public List<AlbumDTO> findAlbumsByTitle(String title) throws RemoteException {
        List<AlbumDTO> albumDTOs = new LinkedList<>();
        List<String> albumTitles = productRepository.findAlbumsByTitle(title);

        for (String albumTitle : albumTitles) {
            albumDTOs.add(new AlbumDTO(albumTitle, null, null, null, null, 0));
        }

        return albumDTOs;
    }

    @Override
    public List<SongDTO> findSongsByTitle(String title) throws RemoteException {
        List<SongDTO> songDTOs = new LinkedList<>();
        List<String> songTitles = productRepository.findSongsByTitle(title);

        for (String songTitle : songTitles) {
            songDTOs.add(new SongDTO(songTitle, null, null, 0, null, null, 0));
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

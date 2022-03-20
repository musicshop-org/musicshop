package application;

import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ArtistDTO;
import sharedrmi.application.dto.SongDTO;

import infrastructure.ProductRepository;
import infrastructure.ProductRepositoryImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ProductServiceImpl extends UnicastRemoteObject  implements ProductService {

    ProductRepository productRepository = new ProductRepositoryImpl();


    public ProductServiceImpl() throws RemoteException {
    }

    public List<AlbumDTO> findAlbumsByTitle(String title) throws RemoteException {
        // return productRepository.findAlbumsByTitle(title);

        return null;
    }

    public List<SongDTO> findSongsByTitle(String title) throws RemoteException {
        // return productRepository.findSongsByTitle(title);

        return null;
    }

    public List<ArtistDTO> findArtistsByName(String name) throws RemoteException {
        // return productRepository.findArtistsByName(name);

        return null;
    }

}

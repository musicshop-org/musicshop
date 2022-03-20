package application;

import infrastructure.AlbumRepository;
import infrastructure.AlbumRepositoryImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AlbumServiceImpl extends UnicastRemoteObject /* implements AlbumService */ {

    AlbumRepository albumRepository = new AlbumRepositoryImpl();

    public AlbumServiceImpl() throws RemoteException {
    }

    /*

    public List<AlbumDTO> findAlbumsByTitle(String title) throws RemoteException{
        return albumRepository.findAlbumsByTitle(title);
    }

    public List<SongDTO> findSongsByTitle(String title) throws RemoteException{
        return albumRepository.findSongsByTitle(title);
    }

    public List<ArtistDTO> findArtistsByName(String name) throws RemoteException{
        return albumRepository.findArtistsByTitle(name);
    }

     */

}

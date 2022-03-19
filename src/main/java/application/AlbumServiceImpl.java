package application;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AlbumServiceImpl extends UnicastRemoteObject /* implements AlbumService */ {

    public AlbumServiceImpl() throws RemoteException {
    }

    /*

    AlbumRepository albumRepository = new AlbumRepositoryImpl();



    -- constructor comes here --



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

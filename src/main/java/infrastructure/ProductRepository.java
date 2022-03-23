package infrastructure;

import domain.Album;
import domain.Song;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface ProductRepository {

    Set<Album> findAlbumsBySongTitle(String title) throws RemoteException;

    List<Song> findSongsByTitle(String title) throws RemoteException;

    List<String> findArtistsByName(String name) throws RemoteException;

}

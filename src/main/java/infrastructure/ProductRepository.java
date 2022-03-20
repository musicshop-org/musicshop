package infrastructure;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface ProductRepository {

    List<String> findAlbumsByTitle(String title) throws RemoteException;

    List<String> findSongsByTitle(String title) throws RemoteException;

    List<String> findArtistsByName(String name) throws RemoteException;

}

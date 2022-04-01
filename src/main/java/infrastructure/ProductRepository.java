package infrastructure;

import domain.Album;
import domain.Artist;
import domain.Song;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface ProductRepository {

    Set<Album> findAlbumsBySongTitle(String title);

    List<Song> findSongsByTitle(String title);

    List<Artist> findArtistsByName(String name);

}

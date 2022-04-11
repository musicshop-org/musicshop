package domain.repositories;

import domain.Album;
import domain.Artist;
import domain.Song;
import sharedrmi.domain.enums.MediumType;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface ProductRepository {

    Set<Album> findAlbumsBySongTitle(String title);

    List<Song> findSongsByTitle(String title);

    List<Artist> findArtistsByName(String name);

    List<Album> findAlbumsByAlbumTitle(String title);

    Album findAlbumByAlbumTitleAndMedium(String title, MediumType mediumType);

    void updateAlbum(Album album);
}

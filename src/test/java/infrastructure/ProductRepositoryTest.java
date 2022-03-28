package infrastructure;

import domain.Album;

import domain.Artist;
import domain.Song;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import java.util.List;
import java.util.Set;

public class ProductRepositoryTest {

    @Test
    void given_songTitle_when_findAlbumsBySongTitle_then_returnAlbumsWithThisSongIn() throws RemoteException {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "Thriller";

        // when
        Set <Album> albums = productRepository.findAlbumsBySongTitle(songTitle);
        int foundSongs = 0;

        for (Album album : albums) {
            for (Song song : album.getSongs())
            {
                if (song.getTitle().equalsIgnoreCase(songTitle))
                    foundSongs++;
            }
        }

        assertEquals(albums.size(), foundSongs);
    }

    @Test
    void given_notExistingSongTitle_when_findAlbumsBySongTitle_then_returnEmptySet() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "notExistingSongTitle";

        // when
        Set <Album> albums = productRepository.findAlbumsBySongTitle(songTitle);

        // then
        assertEquals(0, albums.size());
    }

    @Test
    void given_existingSongWithRandomCase_when_findAlbumsBySongTitle_then_expectSong() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "bEaUtiFul";

        // when
        Set <Album> albums = productRepository.findAlbumsBySongTitle(songName);

        // then
        assertEquals(albums.size(), 1);
    }

    @Test
    void given_existingSong_when_findSongsByTitle_then_expectSong() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "Beautiful";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songName);

        // then
        assertEquals(songs.size(), 1);
    }

    @Test
    void given_notExistingSong_when_findSongsByTitle_then_expectEmptyList() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "notExistingSongName";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songName);

        // then
        assertEquals(songs.size(), 0);
    }

    @Test
    void given_existingSongWithRandomCase_when_findSongsByTitle_then_expectSong() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "beaUtiFul";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songName);

        // then
        assertEquals(songs.size(), 1);
    }

    @Test
    void given_artist_when_findArtistsByName_then_expectArtist() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "Seeed";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(artists.size(), 1);
    }

    @Test
    void given_notExistingArtist_when_findArtistsByName_then_expectEmptyList() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "notExistingArtistName";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(artists.size(), 0);
    }

    @Test
    void given_existingArtistWithRandomCase_when_findArtistsByName_then_expectArtist() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "seEed";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(artists.size(), 1);
    }
}

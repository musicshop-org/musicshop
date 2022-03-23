package infrastructure;

import domain.Album;

import domain.Artist;
import domain.Song;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestProductRepositoryImpl {

    @Test
    void given_songname_when_findalbumsbysongtitle_then_expectallalbumswiththissongin() throws RemoteException {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "Thriller";

        // when
        Set <Album> albums = productRepository.findAlbumsBySongTitle(songName);
        int foundSongs = 0;

        for (Album album : albums) {
            for (Song song : album.getSongs())
            {
                if (song.getTitle().equalsIgnoreCase(songName))
                    foundSongs++;
            }
        }

        assertEquals(albums.size(), foundSongs);
    }

    @Test
    void given_notexistingsong_when_findalbumsbysongtitle_then_expectemptyset() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "notExistingSongName";

        // when
        Set <Album> albums = productRepository.findAlbumsBySongTitle(songName);

        // then
        assertEquals(albums.size(), 0);
    }

    @Test
    void given_existingsongwithrandomcase_when_findalbumsbysongtitle_then_expectsong() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "bEaUtiFul";

        // when
        Set <Album> albums = productRepository.findAlbumsBySongTitle(songName);

        // then
        assertEquals(albums.size(), 1);
    }

    @Test
    void given_existingsong_when_findSongsByTitle_then_expectemptylist() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "Beautiful";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songName);

        // then
        assertEquals(songs.size(), 1);
    }

    @Test
    void given_notexistingsong_when_findSongsByTitle_then_expectemptylist() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "notExistingSongName";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songName);

        // then
        assertEquals(songs.size(), 0);
    }

    @Test
    void given_existingsongwithrandomcase_when_findSongsByTitle_then_expectsong() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "beaUtiFul";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songName);

        // then
        assertEquals(songs.size(), 1);
    }

    @Test
    void given_artist_when_findartistsbyname_then_expectartist() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "Seeed";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(artists.size(), 1);
    }

    @Test
    void given_notexistingartist_when_findartistsbyname_then_expectemptylist() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "notExistingArtistName";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(artists.size(), 0);
    }

    @Test
    void given_existingartistwithrandomcase_when_findartistsbyname_then_expectartist() throws RemoteException {

        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "seEed";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(artists.size(), 1);
    }
}

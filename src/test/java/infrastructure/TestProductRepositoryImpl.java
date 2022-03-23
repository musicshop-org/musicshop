package infrastructure;

import domain.Album;

import domain.Song;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestProductRepositoryImpl {
//wenn null zrück kommt; Case sensitive

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
                if (song.getTitle().equals(songName))
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
}

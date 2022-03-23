package infrastructure;

import domain.Album;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class TestProductRepositoryImpl {

    @Test
    void given_songName_when_findAlbumsByTitle_then_expectallAlbumsWithThisSongInIt() throws RemoteException {
        //given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "Beautiful";
        String AlbumTitleExpected = "Seeed";
        Album albumResult = new Album();

        Set <Album> albums = productRepository.findAlbumsByTitle(songName);

        for (Album album : albums) {
            albumResult = album;
        }

        assertEquals(AlbumTitleExpected, albumResult.getTitle());
    }
}

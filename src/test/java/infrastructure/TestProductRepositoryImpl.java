package infrastructure;

import domain.Album;

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
        //given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "Beautiful";
        String AlbumTitleExpected = "Seeed";
        List <Album> albumResult = new ArrayList<>();

        Set <Album> albums = productRepository.findAlbumsBySongTitle(songName);

        for (Album album : albums) {
            albumResult.add(album);
        }

        for (int i = 0; i < albumResult.size(); i++) {
            assertEquals(AlbumTitleExpected, albumResult.get(i).getTitle());
        }
    }
}

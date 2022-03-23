package infrastructure;

import domain.Album;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;



public class TestProductRepositoryImpl {

    @Test
    void given_songName_when_findAlbumsByTitle_then_expectallAlbumsWithThisSongInIt() throws RemoteException {
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
            if (AlbumTitleExpected.equals(albumResult.get(i).getTitle())){
                System.out.println("passt");
            }
            assertEquals(AlbumTitleExpected, albumResult.get(i).getTitle());
        }

    }
}

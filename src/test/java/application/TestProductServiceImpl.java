package application;

import domain.Album;
import infrastructure.ProductRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.rmi.RemoteException;
import java.util.Collections;

public class TestProductServiceImpl {

    @Test
    void given_songname_when_findalbumsbysongtitle_then_expectallalbumswiththissongin() throws RemoteException {
        // given
        ProductServiceImpl productService = new ProductServiceImpl();
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songName = "notExistingSongName";

        // when
        Mockito.when(productRepository.findAlbumsBySongTitle(songName)).thenReturn(Collections.emptySet());

        // then
        Assertions.assertNull(productService.findAlbumsBySongTitle(songName));
    }

    @Test
    void given_notexistingsong_when_findalbumsbysongtitle_then_expectemptyset() throws RemoteException {
        // given


        // when


        // then

    }

    @Test
    void given_existingsongwithrandomcase_when_findalbumsbysongtitle_then_expectsong() throws RemoteException {
        // given


        // when


        // then

    }

    @Test
    void given_existingsong_when_findSongsByTitle_then_expectemptylist() throws RemoteException {
        // given


        // when


        // then

    }

    @Test
    void given_notexistingsong_when_findSongsByTitle_then_expectemptylist() throws RemoteException {
        // given


        // when


        // then

    }

    @Test
    void given_existingsongwithrandomcase_when_findSongsByTitle_then_expectsong() throws RemoteException {
        // given


        // when


        // then

    }

}

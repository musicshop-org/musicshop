package application;

import domain.Album;
import domain.Song;
import infrastructure.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.SongDTO;

import java.rmi.RemoteException;
import java.util.*;

public class TestProductServiceImpl {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;


    @BeforeEach
    public void setUp() throws RemoteException {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl();
    }

    @Test
    void given_songname_when_findalbumsbysongtitle_then_expectallalbumswiththissongin() throws RemoteException {
        // given
        String songName = "Thriller";

        Set<Song> songs = new HashSet<>();
        songs.add(new Song(songName, null, 0, null, null, null, null));

        Set<SongDTO> songDTOs = new HashSet<>();
        songDTOs.add(new SongDTO(songName, null, 0, null, null, null, null, null));


        Set<Album> albums = new HashSet<>();
        albums.add(new Album(null, null, 0, null, null, null, null, songs));

        List<AlbumDTO> albumDTOs = new LinkedList<>();
        albumDTOs.add(new AlbumDTO(null, null, 0, null, null, null, null, songDTOs));

        // when
        Mockito.when(this.productRepository.findAlbumsBySongTitle(songName)).thenReturn(albums);

        // then
        Assertions.assertEquals(albumDTOs, this.productService.findAlbumsBySongTitle(songName));

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

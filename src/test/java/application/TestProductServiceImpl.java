package application;

import domain.Album;
import domain.Song;
import infrastructure.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.SongDTO;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestProductServiceImpl {

    private List<AlbumDTO> givenAlbumDTOs;
    private ProductService productService;

    @Mock
    private static ProductRepository productRepository;

    @BeforeEach
    void initMockAndService() throws RemoteException {
        String songTitle = "Thriller";

        Set<Song> songs = new HashSet<>();
        songs.add(new Song(songTitle, new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(1982, 11, 30), "pop, disco, pop-soul", null));

        Set<SongDTO> songDTOs = new HashSet<>();
        songDTOs.add(new SongDTO(songTitle, new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(1982, 11, 30), "pop, disco, pop-soul", null, null));


        Set<Album> albums = new HashSet<>();
        albums.add(new Album("Thriller", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6), new AlbumId(), "Epic", songs));

        givenAlbumDTOs = new LinkedList<>();
        givenAlbumDTOs.add(new AlbumDTO("Thriller", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6), new AlbumId(), "Epic", songDTOs));

        Mockito.when(productRepository.findAlbumsBySongTitle(songTitle)).thenReturn(albums);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void given_songTitle_when_findAlbumsBySongTitle_then_returnAlbumsWithThisSongIn() throws RemoteException {
        // when
        String songTitle = "Thriller";
        List<AlbumDTO> albumDTOs = productService.findAlbumsBySongTitle(songTitle);

        // then
        Assertions.assertAll(
                () -> assertEquals(givenAlbumDTOs.get(0).getTitle(), albumDTOs.get(0).getTitle()),
                () -> assertEquals(givenAlbumDTOs.get(0).getPrice(), albumDTOs.get(0).getPrice()),
                () -> assertEquals(givenAlbumDTOs.get(0).getStock(), albumDTOs.get(0).getStock()),
                () -> assertEquals(givenAlbumDTOs.get(0).getMediumType(), albumDTOs.get(0).getMediumType()),
                () -> assertEquals(givenAlbumDTOs.get(0).getReleaseDate(), albumDTOs.get(0).getReleaseDate()),
                () -> assertEquals(givenAlbumDTOs.get(0).getLabel(), albumDTOs.get(0).getLabel()),
                () -> assertEquals(givenAlbumDTOs.get(0).getSongs().size(), albumDTOs.get(0).getSongs().size())
        );
    }

    @Test
    void given_notExistingSongTitle_when_findAlbumsBySongTitle_then_returnEmptySet() throws RemoteException {
        // when
        String songTitle = "notExistingSongTitle";
        List<AlbumDTO> albumDTOs = productService.findAlbumsBySongTitle(songTitle);

        // then
        assertEquals(0, albumDTOs.size());
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

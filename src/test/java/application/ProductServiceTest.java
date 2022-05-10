package application;

import domain.Album;
import domain.Artist;
import domain.Song;
import domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ArtistDTO;
import sharedrmi.application.dto.SongDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

import javax.naming.NoPermissionException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private final List<AlbumDTO> givenAlbumDTOs = new LinkedList<>();
    private final List<SongDTO> givenSongDTOs = new LinkedList<>();
    private final List<ArtistDTO> givenArtistDTOs = new LinkedList<>();

    private ProductService productService;

    @Mock
    private static ProductRepository productRepository;

    @BeforeEach
    void initMockAndService() throws RemoteException {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void given_songTitle_when_findAlbumsBySongTitle_then_returnAlbumsWithThisSongIn() throws RemoteException {
        // given
        String songTitle = "Thriller";

        Set<Song> songs = new HashSet<>();
        songs.add(new Song("Thriller", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(1982, 11, 30), "pop, disco, pop-soul", Collections.emptyList()));

        Set<SongDTO> songDTOs = new HashSet<>();
        songDTOs.add(new SongDTO("Thriller", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(1982, 11, 30).toString(), "pop, disco, pop-soul", Collections.emptyList(), Collections.emptySet()));


        Set<Album> albums = new HashSet<>();
        albums.add(new Album("Thriller", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6), new AlbumId(), "Epic", songs));

        givenAlbumDTOs.add(new AlbumDTO("Thriller", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6).toString(), new AlbumId(), "Epic", songDTOs, 0));

        Mockito.when(productRepository.findAlbumsBySongTitle(songTitle)).thenReturn(albums);

        // when
        List<AlbumDTO> albumDTOs = productService.findAlbumsBySongTitle(songTitle);

        // then
        assertAll(
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
        // given
        String songTitle = "notExistingSongTitle";

        Mockito.when(productRepository.findAlbumsBySongTitle(songTitle)).thenReturn(Collections.emptySet());

        // when
        List<AlbumDTO> albumDTOs = productService.findAlbumsBySongTitle(songTitle);

        // then
        assertEquals(givenAlbumDTOs.size(), albumDTOs.size());
    }

    @Test
    void given_existingSongWithRandomCase_when_findAlbumsBySongTitle_then_expectSong() throws RemoteException {
        // given
        String songTitle = "tHrIlLeR";

        Set<Song> songs = new HashSet<>();
        songs.add(new Song("Thriller", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(1982, 11, 30), "pop, disco, pop-soul", Collections.emptyList()));

        Set<SongDTO> songDTOs = new HashSet<>();
        songDTOs.add(new SongDTO("Thriller", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(1982, 11, 30).toString(), "pop, disco, pop-soul", Collections.emptyList(), Collections.emptySet()));


        Set<Album> albums = new HashSet<>();
        albums.add(new Album("Thriller", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6), new AlbumId(), "Epic", songs));

        givenAlbumDTOs.add(new AlbumDTO("Thriller", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6).toString(), new AlbumId(), "Epic", songDTOs, 0));

        Mockito.when(productRepository.findAlbumsBySongTitle(songTitle)).thenReturn(albums);

        // when
        List<AlbumDTO> albumDTOs = productService.findAlbumsBySongTitle(songTitle);

        // then
        assertAll(
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
    void given_existingSong_when_findSongsByTitle_then_expectEmptyList() throws RemoteException {
        // given
        String songTitle = "Beautiful";

        List<Song> songs = new LinkedList<>();
        songs.add(new Song(songTitle, new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(2012, 9, 28), "dancehall, reggae", Collections.emptyList()));

        givenSongDTOs.add(new SongDTO(songTitle, new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(2012, 9, 28).toString(), "dancehall, reggae", Collections.emptyList(), Collections.emptySet()));

        Mockito.when(productRepository.findSongsByTitle(songTitle)).thenReturn(songs);

        // when
        List<SongDTO> songDTOs = productService.findSongsByTitle(songTitle);

        // then
        assertAll(
                () -> assertEquals(givenSongDTOs.get(0).getTitle(), songDTOs.get(0).getTitle()),
                () -> assertEquals(givenSongDTOs.get(0).getPrice(), songDTOs.get(0).getPrice()),
                () -> assertEquals(givenSongDTOs.get(0).getStock(), songDTOs.get(0).getStock()),
                () -> assertEquals(givenSongDTOs.get(0).getMediumType(), songDTOs.get(0).getMediumType()),
                () -> assertEquals(givenSongDTOs.get(0).getReleaseDate(), songDTOs.get(0).getReleaseDate()),
                () -> assertEquals(givenSongDTOs.get(0).getGenre(), songDTOs.get(0).getGenre())
        );
    }

    @Test
    void given_notExistingSong_when_findSongsByTitle_then_expectEmptyList() throws RemoteException {
        // given
        String songTitle = "notExistingSongTitle";

        Mockito.when(productRepository.findSongsByTitle(songTitle)).thenReturn(Collections.emptyList());

        // when
        List<SongDTO> songDTOs = productService.findSongsByTitle(songTitle);

        // then
        assertEquals(givenSongDTOs.size(), songDTOs.size());
    }

    @Test
    void given_existingSongWithRandomCase_when_findSongsByTitle_then_expectSong() throws RemoteException {
        // given
        String songTitle = "beaUtiFul";

        List<Song> songs = new LinkedList<>();
        songs.add(new Song("Beautiful", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(2012, 9, 28), "dancehall, reggae", Collections.emptyList()));

        givenSongDTOs.add(new SongDTO("Beautiful", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(2012, 9, 28).toString(), "dancehall, reggae", Collections.emptyList(), Collections.emptySet()));

        Mockito.when(productRepository.findSongsByTitle(songTitle)).thenReturn(songs);

        // when
        List<SongDTO> songDTOs = productService.findSongsByTitle(songTitle);

        // then
        assertAll(
                () -> assertEquals(givenSongDTOs.get(0).getTitle(), songDTOs.get(0).getTitle()),
                () -> assertEquals(givenSongDTOs.get(0).getPrice(), songDTOs.get(0).getPrice()),
                () -> assertEquals(givenSongDTOs.get(0).getStock(), songDTOs.get(0).getStock()),
                () -> assertEquals(givenSongDTOs.get(0).getMediumType(), songDTOs.get(0).getMediumType()),
                () -> assertEquals(givenSongDTOs.get(0).getReleaseDate(), songDTOs.get(0).getReleaseDate()),
                () -> assertEquals(givenSongDTOs.get(0).getGenre(), songDTOs.get(0).getGenre())
        );
    }

    @Test
    void given_artist_when_findArtistsByName_then_expectArtist() throws RemoteException {
        // given
        String artistName = "Seeed";

        List<Artist> artists = new LinkedList<>();
        artists.add(new Artist(artistName));

        givenArtistDTOs.add(new ArtistDTO(artistName));

        Mockito.when(productRepository.findArtistsByName(artistName)).thenReturn(artists);

        // when
        List<ArtistDTO> artistDTOs = productService.findArtistsByName(artistName);

        // then
        assertEquals(givenArtistDTOs.get(0).getName(), artistDTOs.get(0).getName());
    }

    @Test
    void given_notExistingArtist_when_findArtistsByName_then_expectEmptyList() throws RemoteException {
        // given
        String artistName = "notExistingArtistName";

        Mockito.when(productRepository.findArtistsByName(artistName)).thenReturn(Collections.emptyList());

        // when
        List<ArtistDTO> artistDTOs = productService.findArtistsByName(artistName);

        // then
        assertEquals(givenArtistDTOs.size(), artistDTOs.size());
    }

    @Test
    void given_existingArtistWithRandomCase_when_findArtistsByName_then_expectArtist() throws RemoteException {
        // given
        String artistName = "seEed";

        List<Artist> artists = new LinkedList<>();
        artists.add(new Artist(artistName));

        givenArtistDTOs.add(new ArtistDTO(artistName));

        Mockito.when(productRepository.findArtistsByName(artistName)).thenReturn(artists);

        // when
        List<ArtistDTO> artistDTOs = productService.findArtistsByName(artistName);

        // then
        assertEquals(givenArtistDTOs.get(0).getName(), artistDTOs.get(0).getName());
    }

    @Test
    void given_album_when_decreasestockofalbum_then_decreasedstock() throws RemoteException, NoPermissionException, NotEnoughStockException {

        // given
        final String title = "title1";
        final MediumType mediumType = MediumType.CD;
        final int givenStock = 8;
        final int decreaseQuantity = 5;
        final int expectedQuantity = 3;

        Album album = new Album(title,
                BigDecimal.TEN,
                givenStock,
                mediumType,
                LocalDate.now(),
                new AlbumId(),
                "label1",
                new HashSet<>());

        Mockito.when(productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType)).thenReturn(album);

        // when
        productService.decreaseStockOfAlbum(title, mediumType, decreaseQuantity);

        // then
        assertEquals(expectedQuantity, album.getStock());
    }

    @Test
    void given_album_when_findAlbumByTitleAndMedium_then_expectalbum () throws RemoteException, AlbumNotFoundException {

        // given
        final String title = "title1";
        final MediumType mediumType = MediumType.CD;

        Album album = new Album(title,
                BigDecimal.TEN,
                8,
                mediumType,
                LocalDate.now(),
                new AlbumId(),
                "label1",
                new HashSet<>());

        Mockito.when(productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType)).thenReturn(album);

        // when
        AlbumDTO albumDTO = productService.findAlbumByAlbumTitleAndMedium(title, mediumType);

        // then
        assertEquals(albumDTO.getTitle(), album.getTitle());
        assertEquals(albumDTO.getMediumType(), album.getMediumType());
        assertEquals(albumDTO.getAlbumId(), album.getAlbumId());
    }

    @Test
    void given_notexistingalbum_when_findAlbumByTitleAndMedium_then_albumnotfoundexception () {

        // given
        final String title = "title1";
        final MediumType mediumType = MediumType.CD;

        Album album = new Album(title,
                BigDecimal.TEN,
                8,
                mediumType,
                LocalDate.now(),
                new AlbumId(),
                "label1",
                new HashSet<>());

        Mockito.when(productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType)).thenReturn(null);

        // when ... then
        assertThrows(AlbumNotFoundException.class, () -> productService.findAlbumByAlbumTitleAndMedium(title, mediumType));
    }
}

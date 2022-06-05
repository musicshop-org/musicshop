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
        songDTOs.add(new SongDTO("Thriller", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(1982, 11, 30).toString(), "pop, disco, pop-soul", Collections.emptyList(), Collections.emptySet(), 2));


        Set<Album> albums = new HashSet<>();
        albums.add(new Album("Thriller", "", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6), new AlbumId(), "Epic", songs));

        givenAlbumDTOs.add(new AlbumDTO("Thriller", "", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6).toString(), new AlbumId(), "Epic", songDTOs, 0, 1));

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
        songDTOs.add(new SongDTO("Thriller", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(1982, 11, 30).toString(), "pop, disco, pop-soul", Collections.emptyList(), Collections.emptySet(), 5));


        Set<Album> albums = new HashSet<>();
        albums.add(new Album("Thriller", "", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6), new AlbumId(), "Epic", songs));

        givenAlbumDTOs.add(new AlbumDTO("Thriller", "", new BigDecimal(12), 4, MediumType.CD, LocalDate.of(1983, 6, 6).toString(), new AlbumId(), "Epic", songDTOs, 0, 1));

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
    void given_songTitle_when_findAlbumsBySongTitleDigital_then_returnDigitalAlbumsWithThisSongIn() throws RemoteException {
        // given
        String songTitle = "Thriller";

        Set<Album> albums = Set.of(new Album(
                "Thriller",
                "",
                new BigDecimal(12),
                4,
                MediumType.DIGITAL,
                LocalDate.of(1983, 6, 6),
                new AlbumId(),
                "Epic",
                Set.of(new Song(
                        "Thriller",
                        new BigDecimal(2),
                        -1,
                        MediumType.DIGITAL,
                        LocalDate.of(1982, 11, 30),
                        "pop, disco, pop-soul",
                        List.of(new Artist("Michael Jackson")),
                        Set.of(new Album(
                                "Thriller",
                                "",
                                new BigDecimal(12),
                                4,
                                MediumType.DIGITAL,
                                LocalDate.of(1983, 6, 6),
                                new AlbumId(),
                                "Epic",
                                Collections.emptySet()
                        ))
                ))
        ));

        givenAlbumDTOs.add(new AlbumDTO(
                "Thriller",
                "",
                new BigDecimal(12),
                4,
                MediumType.DIGITAL,
                LocalDate.of(1983, 6, 6).toString(),
                new AlbumId(),
                "Epic",
                Set.of(new SongDTO(
                        "Thriller",
                        new BigDecimal(2),
                        -1,
                        MediumType.DIGITAL,
                        LocalDate.of(1982, 11, 30).toString(),
                        "pop, disco, pop-soul",
                        List.of(new ArtistDTO("Michael Jackson")),
                        Set.of(new AlbumDTO(
                                "Thriller",
                                "",
                                new BigDecimal(12),
                                4,
                                MediumType.DIGITAL,
                                LocalDate.of(1983, 6, 6).toString(),
                                new AlbumId(),
                                "Epic",
                                Collections.emptySet(),
                                0,
                                1
                        )),
                        2
                )),
                0,
                1
        ));

        Mockito.when(productRepository.findAlbumsBySongTitle(songTitle)).thenReturn(albums);

        // when
        List<AlbumDTO> albumDTOs = productService.findAlbumsBySongTitleDigital(songTitle);

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

        givenSongDTOs.add(new SongDTO(songTitle, new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(2012, 9, 28).toString(), "dancehall, reggae", Collections.emptyList(), Collections.emptySet(), 0));

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

        givenSongDTOs.add(new SongDTO("Beautiful", new BigDecimal(2), -1, MediumType.DIGITAL, LocalDate.of(2012, 9, 28).toString(), "dancehall, reggae", Collections.emptyList(), Collections.emptySet(), 0));

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
                "",
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
    void given_album_when_findAlbumByTitleAndMedium_then_expectAlbum () throws RemoteException, AlbumNotFoundException {
        // given
        final String title = "title1";
        final MediumType mediumType = MediumType.CD;
        Album album = new Album(
                title,
                "",
                BigDecimal.TEN,
                8,
                mediumType,
                LocalDate.now(),
                new AlbumId(),
                "label1",
                Set.of(new Song(
                        "songTitle1",
                        BigDecimal.ONE,
                        1,
                        MediumType.DIGITAL,
                        LocalDate.now(),
                        "genre1",
                        List.of(new Artist("artist1"))
                ))
        );

        Mockito.when(productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType)).thenReturn(album);

        // when
        AlbumDTO albumDTO = productService.findAlbumByAlbumTitleAndMedium(title, mediumType);

        // then
        assertEquals(albumDTO.getTitle(), album.getTitle());
        assertEquals(albumDTO.getMediumType(), album.getMediumType());
        assertEquals(albumDTO.getAlbumId(), album.getAlbumId());
    }

    @Test
    void given_notExistingAlbum_when_findAlbumByTitleAndMedium_then_albumNotFoundException() {
        // given
        final String title = "title1";
        final MediumType mediumType = MediumType.CD;

        Mockito.when(productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType)).thenReturn(null);

        // when ... then
        assertThrows(AlbumNotFoundException.class, () -> productService.findAlbumByAlbumTitleAndMedium(title, mediumType));
    }

    @Test
    void given_albumId_when_findAlbumByAlbumId_then_expectAlbum() throws AlbumNotFoundException {
        // given
        AlbumId albumId = new AlbumId();
        Album album = new Album(
                "title1",
                "",
                BigDecimal.TEN,
                8,
                MediumType.CD,
                LocalDate.now(),
                albumId,
                "label1",
                Set.of(new Song(
                        "songTitle1",
                        BigDecimal.ONE,
                        1,
                        MediumType.DIGITAL,
                        LocalDate.now(),
                        "genre1",
                        List.of(new Artist("artist1"))
                ))
        );

        Mockito.when(productRepository.findAlbumByAlbumId(albumId.toString())).thenReturn(Optional.of(album));

        // when
        AlbumDTO albumDTO = productService.findAlbumByAlbumId(albumId.toString());

        // then
        assertAll(
                () -> assertEquals(albumDTO.getTitle(), album.getTitle()),
                () -> assertEquals(albumDTO.getImageUrl(), album.getImageUrl()),
                () -> assertEquals(albumDTO.getPrice(), album.getPrice()),
                () -> assertEquals(albumDTO.getStock(), album.getStock()),
                () -> assertEquals(albumDTO.getMediumType(), album.getMediumType()),
                () -> assertEquals(albumDTO.getReleaseDate(), album.getReleaseDate().toString()),
                () -> assertEquals(albumDTO.getAlbumId(), album.getAlbumId()),
                () -> assertEquals(albumDTO.getLabel(), album.getLabel()),
                () -> assertEquals(albumDTO.getSongs().size(), album.getSongs().size())
        );
    }

    @Test
    void given_notExistingAlbumId_when_findAlbumByAlbumId_then_expectAlbum() {
        // given
        AlbumId notExistingAlbumId = new AlbumId();

        Mockito.when(productRepository.findAlbumByAlbumId(notExistingAlbumId.toString())).thenReturn(Optional.empty());

        // when ... then
        assertThrows(AlbumNotFoundException.class, () -> productService.findAlbumByAlbumId(notExistingAlbumId.toString()));
    }
}

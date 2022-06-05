package infrastructure;

import domain.Album;
import domain.Artist;
import domain.Song;
import org.junit.jupiter.api.Test;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest {

    @Test
    void given_songTitle_when_findAlbumsBySongTitle_then_returnAlbumsWithThisSongIn() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "Thriller";

        // when
        Set<Album> albums = productRepository.findAlbumsBySongTitle(songTitle);
        int foundSongs = 0;

        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                if (song.getTitle().equalsIgnoreCase(songTitle))
                    foundSongs++;
            }
        }

        // then
        assertEquals(foundSongs, albums.size());
    }

    @Test
    void given_notExistingSongTitle_when_findAlbumsBySongTitle_then_returnEmptySet() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "notExistingSongTitle";

        // when
        Set<Album> albums = productRepository.findAlbumsBySongTitle(songTitle);

        // then
        assertEquals(0, albums.size());
    }

    @Test
    void given_existingSongWithRandomCase_when_findAlbumsBySongTitle_then_expectSong() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "bEaUtiFul";

        // when
        Set<Album> albums = productRepository.findAlbumsBySongTitle(songTitle);

        // then
        assertEquals(2, albums.size());
    }

    @Test
    void given_albumId_when_findAlbumByLongId_then_expectAlbum() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        long albumId = 82;
        String expectedAlbumTitle = "Thriller";

        // when
        Album album = productRepository.findAlbumByLongId(albumId);

        // then
        assertEquals(expectedAlbumTitle, album.getTitle());
    }

    @Test
    void given_notExistingAlbumId_when_findAlbumByLongId_then_expectNull() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        long albumId = -1;

        // when
        Album album = productRepository.findAlbumByLongId(albumId);

        // then
        assertNull(album);
    }

    @Test
    void given_songId_when_findSongByLongId_then_expectSong() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        long songId = 1;
        String expectedSongTitle = "Beautiful";

        // when
        Song song = productRepository.findSongByLongId(songId);

        // then
        assertEquals(expectedSongTitle, song.getTitle());
    }

    @Test
    void given_notExistingSongId_when_findSongByLongId_then_expectNull() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        long songId = -1;

        // when
        Song song = productRepository.findSongByLongId(songId);

        // then
        assertNull(song);
    }

    @Test
    void given_existingSong_when_findSongsByTitle_then_expectSong() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "Beautiful";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songTitle);

        // then
        assertEquals(2, songs.size());
    }

    @Test
    void given_notExistingSong_when_findSongsByTitle_then_expectEmptyList() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "notExistingSongName";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songTitle);

        // then
        assertEquals(0, songs.size());
    }

    @Test
    void given_existingSongWithRandomCase_when_findSongsByTitle_then_expectSong() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "beaUtiFul";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songTitle);

        // then
        assertEquals(2, songs.size());
    }

    @Test
    void given_artist_when_findArtistsByName_then_expectArtist() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "Seeed";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(1, artists.size());
    }

    @Test
    void given_notExistingArtist_when_findArtistsByName_then_expectEmptyList() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "notExistingArtistName";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(0, artists.size());
    }

    @Test
    void given_existingArtistWithRandomCase_when_findArtistsByName_then_expectArtist() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "seEed";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(1, artists.size());
    }

    @Test
    void given_partialSongTitle_when_findSongsByTitle_then_relevantSongs() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "Beautiful";

        // when
        List<Song> songs = productRepository.findSongsByTitle(songTitle);

        // then
        assertEquals(2, songs.size());
    }

    @Test
    void given_partialSongTitle_when_findAlbumsByTitle_then_relevantAlbums() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String songTitle = "il";

        // when
        Set <Album> albums = productRepository.findAlbumsBySongTitle(songTitle);

        // then
        assertEquals(7, albums.size());
    }

    @Test
    void given_partialArtist_when_findArtistsByName_then_relevantArtists() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String artistName = "ma";

        // when
        List<Artist> artists = productRepository.findArtistsByName(artistName);

        // then
        assertEquals(2, artists.size());
    }

    @Test
    void given_existingAlbum_when_findAlbumsByTitle_then_expectAlbums() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String albumTitle = "Bad";

        // when
        List<Album> albums = productRepository.findAlbumsByAlbumTitle(albumTitle);

        // then
        assertEquals(3, albums.size());
    }

    @Test
    void given_notExistingAlbum_when_findAlbumsByTitle_then_empty() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String albumTitle = "ERROR";

        // when
        List<Album> albums = productRepository.findAlbumsByAlbumTitle(albumTitle);

        // then
        assertEquals(0, albums.size());
    }

    @Test
    void given_album_when_findAlbumByTitleAndMedium_then_expectAlbum() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();

        final String title = "Bad";
        final MediumType mediumType = MediumType.CD;

        Album expectedAlbum = new Album(title,
                "",
                BigDecimal.TEN,
                8,
                mediumType,
                LocalDate.now(),
                new AlbumId(),
                "label1",
                new HashSet<>());

        // when
        Album actualAlbum = productRepository.findAlbumByAlbumTitleAndMedium(title, mediumType);

        // then
        assertEquals(expectedAlbum.getTitle(), actualAlbum.getTitle());
        assertEquals(expectedAlbum.getMediumType(), actualAlbum.getMediumType());
    }

    @Test
    void given_albumId_when_findAlbumByAlbumId_then_expectAlbum() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String albumId = "add33dcb-7e63-4242-b758-d828b45fa300";
        String expectedAlbumTitle = "Thriller";

        // when
        Optional<Album> album = productRepository.findAlbumByAlbumId(albumId);

        // then
        assertEquals(expectedAlbumTitle, album.get().getTitle());
    }

    @Test
    void given_noValidUUIDString_when_findAlbumByAlbumId_then_expectNull() {
        // given
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        String albumId = "noValidUUIDString";

        // when ... then
        assertThrows(IllegalArgumentException.class, () -> productRepository.findAlbumByAlbumId(albumId));
    }
}

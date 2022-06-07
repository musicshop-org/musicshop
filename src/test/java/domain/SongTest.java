package domain;

import org.junit.jupiter.api.Test;
import sharedrmi.domain.enums.MediumType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SongTest {

    @Test
    void given_song_when_toString_then_expect() {
        // given
        Song song = new Song(
                "SongTitle",
                BigDecimal.TEN,
                5,
                MediumType.CD,
                LocalDate.now(),
                "TestGenre",
                Collections.emptyList(),
                Collections.emptySet()
        );

        String expectedString = "Song{id=0, title='SongTitle', price=10, stock=5, mediumType=CD, releaseDate=" + LocalDate.now() + ", genre='TestGenre', artists=[], inAlbum=[]}";

        // when ... then
        assertEquals(expectedString, song.toString());
    }
}

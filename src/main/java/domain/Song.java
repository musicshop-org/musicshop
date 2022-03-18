package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
public class Song extends Product {

    private Long id;
    private final String genre;
    private final List<Artist> artists;

    @Builder
    public Song(String genre, List<Artist> artists) {
        this.genre = genre;
        this.artists = artists;
    }
}

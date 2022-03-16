package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class Song implements Product {

    private String title;
    private String length;
    private String genre;
    private LocalDate releaseDate;
    private List<Artist> artists;

    @Builder
    public Song(String title, String length, String genre, LocalDate releaseDate, List<Artist> artists) {
        this.title = title;
        this.length = length;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.artists = artists;
    }
}

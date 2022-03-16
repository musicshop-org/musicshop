package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Album implements Product {

    private String albumTitle;
    private int year;
    private String label;
    private List<Song> songs;

    @Builder
    public Album(String albumTitle, int year, String label, List<Song> songs) {
        this.albumTitle = albumTitle;
        this.year = year;
        this.label = label;
        this.songs = songs;
    }
}

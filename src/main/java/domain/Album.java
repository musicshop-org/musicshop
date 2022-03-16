package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
public class Album extends Product {

    private final String label;
    private final List<Song> songs;

    @Builder
    public Album(String label, List<Song> songs) {
        this.label = label;
        this.songs = songs;
    }
}

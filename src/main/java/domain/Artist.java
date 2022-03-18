package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Artist {

    private long id;
    private String name;
    private List<Song> songs;

    @Builder
    public Artist(String name) {
        this.name = name;
    }

}

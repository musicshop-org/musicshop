package domain;

import domain.valueobjects.AlbumId;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class Album extends Product {

    private final String label;
    private final AlbumId albumId;
    private final List<Song> songs;

    @Builder
    public Album(AlbumId albumId, String label, List<Song> songs) {
        this.label = label;
        this.albumId = albumId;
        this.songs = songs;
    }
}

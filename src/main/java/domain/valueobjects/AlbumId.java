package domain.valueobjects;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AlbumId {

    private final UUID albumId;

    public AlbumId() {
        this.albumId = UUID.randomUUID();
    }
}

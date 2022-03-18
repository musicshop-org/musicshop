package domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Artist {

    private Long id;
    private final String name;

    @Builder
    public Artist(String name) {
        this.name = name;
    }
}

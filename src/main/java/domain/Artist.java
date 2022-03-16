package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Artist {

    private String name;

    @Builder
    public Artist(String name) {
        this.name = name;
    }
}

package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class Artist {

    private long id;
    private String name;

    protected Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

}

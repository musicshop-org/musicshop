package domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class Artist {

    private Artist(){}

    private long id;
    private String name;

    public Artist(String name) {
        this.name = name;
    }

}

package domain;

import lombok.Getter;

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

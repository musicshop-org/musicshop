package domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Artist implements Serializable {

    private long id;
    private String name;

    protected Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

}

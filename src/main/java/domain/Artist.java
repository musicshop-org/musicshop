package domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Artist implements Serializable {

    @SuppressWarnings("unused")
    private long id;
    private String name;

    @SuppressWarnings("unused")
    protected Artist() {
    }

    public Artist(String name) {
        this.name = name;
    }

}

package domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Topic implements Serializable {

    @SuppressWarnings("unused")
    private long id;
    private String name;

    @SuppressWarnings("unused")
    protected Topic() {
    }

    public Topic(String name) {
        this.name = name;
    }
}

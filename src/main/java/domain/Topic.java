package domain;

import lombok.Getter;

@Getter
public class Topic {

    private long id;
    private String name;

    protected Topic () {}

    public Topic(String name) {
        this.name = name;
    }
}

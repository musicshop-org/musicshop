package domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Topic  implements Serializable {

    private long id;
    private String name;

    protected Topic () {}

    public Topic(String name) {
        this.name = name;
    }
}

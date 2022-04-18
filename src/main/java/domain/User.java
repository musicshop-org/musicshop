package domain;

import lombok.Getter;
import java.util.List;

@Getter
public class User {

    private long id;
    private String username;
    private List<Topic> topics;

    protected User () {}

    public User(String username, List<Topic> topics) {
        this.username = username;
        this.topics = topics;
    }
}

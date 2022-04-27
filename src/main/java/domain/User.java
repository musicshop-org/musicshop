package domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class User {

    private long id;
    private String username;
    private List<Topic> topics;
    private LocalDateTime lastViewed;

    protected User () {}

    public User(String username, List<Topic> topics) {
        this.username = username;
        this.topics = topics;
        this.lastViewed = LocalDateTime.MIN;
    }

    public void setLastViewed(LocalDateTime lastViewed){
        this.lastViewed = lastViewed;
    }
}

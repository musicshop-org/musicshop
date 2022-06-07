package domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class User implements Serializable {

    @SuppressWarnings("unused")
    private long id;
    private String username;
    private List<Topic> topics;
    private LocalDateTime lastViewed;

    @SuppressWarnings("unused")
    protected User() {
    }

    public User(String username, List<Topic> topics) {
        this.username = username;
        this.topics = topics;
        this.lastViewed = LocalDateTime.MIN;
    }

    public void setLastViewed(LocalDateTime lastViewed) {
        this.lastViewed = lastViewed;
    }
}

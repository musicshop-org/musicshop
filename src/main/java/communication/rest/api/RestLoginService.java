package communication.rest.api;

public interface RestLoginService {
    boolean checkCredentials(String username, String password);
}
package communication.rest.api;

import sharedrmi.domain.valueobjects.Role;

import java.util.List;

public interface RestLoginService {
    boolean checkCredentials(String username, String password);
    List<Role> getRole(String username);
}
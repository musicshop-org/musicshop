package communication.rest.api;

import sharedrmi.domain.valueobjects.Role;

import java.util.List;

public interface RestLoginService {
    boolean checkCredentials(String emailAddress, String password);
    List<Role> getRole(String emailAddress);
}
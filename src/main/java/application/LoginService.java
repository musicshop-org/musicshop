package application;

import domain.valueobjects.Role;

import java.util.List;

public interface LoginService {
    Boolean checkCredentials(String username, String password);
    List<Role> getRole(String username);
}

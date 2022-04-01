package application.api;

import domain.valueobjects.Role;

import javax.security.auth.login.FailedLoginException;
import java.util.List;

public interface LoginService {
    SessionFacade login(String username, String password) throws FailedLoginException;

    Boolean checkCredentials(String username, String password);

    List<Role> getRole(String username);
}

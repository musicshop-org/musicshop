package application.api;

import sharedrmi.domain.valueobjects.Role;

import javax.security.auth.login.FailedLoginException;
import java.rmi.RemoteException;
import java.util.List;

public interface LoginService {
    SessionFacade login(String username, String password) throws FailedLoginException, RemoteException;

    Boolean checkCredentials(String username, String password);

    List<Role> getRole(String username);
}

package communication.rmi.api;

import javax.security.auth.login.FailedLoginException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIControllerFactory extends Remote {

    RMIController createRMIController(String username, String password) throws FailedLoginException, RemoteException;
}

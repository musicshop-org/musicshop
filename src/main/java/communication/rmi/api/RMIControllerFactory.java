package communication.rmi.api;

import javax.security.auth.login.LoginException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIControllerFactory extends Remote {

    RMIController createRMIController(String username, String password) throws LoginException, RemoteException;
}

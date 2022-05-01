package communication.rmi;

import sharedrmi.communication.rmi.RMIControllerFactory;
import sharedrmi.communication.rmi.RMIController;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.security.auth.login.FailedLoginException;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RMIControllerFactoryImpl implements RMIControllerFactory {


    private RMIControllerFactoryImpl() {

    }

    @Override
    public RMIController createRMIController(String username, String password) throws FailedLoginException, RemoteException, AccessDeniedException {
        //RMIController rmiController = new RMIControllerImpl(username, password);
        return null;
    }
}

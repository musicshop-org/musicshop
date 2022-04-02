package communication.rmi;

import sharedrmi.communication.rmi.RMIControllerFactory;
import sharedrmi.communication.rmi.RMIController;

import javax.security.auth.login.FailedLoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIControllerFactoryImpl extends UnicastRemoteObject implements RMIControllerFactory {

    private static RMIControllerFactoryImpl instance;

    public static RMIControllerFactoryImpl getInstance() throws RemoteException {
        if (null == RMIControllerFactoryImpl.instance) {
            new RMIControllerFactoryImpl();
        }

        return RMIControllerFactoryImpl.instance;
    }

    private RMIControllerFactoryImpl() throws RemoteException {
        RMIControllerFactoryImpl.instance = this;
    }

    @Override
    public RMIController createRMIController(String username, String password) throws FailedLoginException, RemoteException {
        return new RMIControllerImpl(username, password);
    }
}
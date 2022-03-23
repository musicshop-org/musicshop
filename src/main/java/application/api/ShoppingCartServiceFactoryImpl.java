package application.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class ShoppingCartServiceFactoryImpl extends UnicastRemoteObject implements ShoppingCartServiceFactory {

    public ShoppingCartServiceFactoryImpl() throws RemoteException{
        super();
    }

    @Override
    public ShoppingCartService createShoppingCartService(UUID ownerId) throws RemoteException {
        return new ShoppingCartServiceImpl(ownerId);
    }
}

package application;

import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.api.ShoppingCartServiceFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class ShoppingCartServiceFactoryImpl extends UnicastRemoteObject implements ShoppingCartServiceFactory {

    public ShoppingCartServiceFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public ShoppingCartService createShoppingCartService(UUID ownerId) throws RemoteException {
        return new ShoppingCartServiceImpl(ownerId);
    }
}

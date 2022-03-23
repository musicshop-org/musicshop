package application.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ShoppingCartServiceFactory extends Remote {
    ShoppingCartService createShoppingCartService(UUID ownerId) throws RemoteException;
}

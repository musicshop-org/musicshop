package application.api;

import sharedrmi.application.dto.AlbumDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ShoppingCartService extends Remote {
    void addProductToCart(AlbumDTO album, int amount) throws RemoteException;
}

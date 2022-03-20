import infrastructure.ProductRepositoryImpl;

import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException {
        ProductRepositoryImpl albumRepository = new ProductRepositoryImpl();
        System.out.println(albumRepository.findAlbumsByTitle("Beautiful"));
    }
}
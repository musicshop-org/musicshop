import application.api.ShoppingCartServiceFactoryImpl;
import application.ProductServiceImpl;

import sharedrmi.application.api.ShoppingCartServiceFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {

        try {

            ProductServiceImpl productService = new ProductServiceImpl();

            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("rmi://localhost/ProductService", productService);

            ShoppingCartServiceFactory cartFactory = new ShoppingCartServiceFactoryImpl();
            Naming.rebind("rmi://localhost/CartFactory", cartFactory);

        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }

    }
}

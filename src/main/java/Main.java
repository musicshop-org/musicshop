import application.api.ShoppingCartServiceFactoryImpl;
import sharedrmi.application.api.ShoppingCartServiceFactory;
import application.ProductServiceImpl;
import domain.Album;
import infrastructure.ProductRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) throws RemoteException {

        try {
            ProductServiceImpl productService = new ProductServiceImpl();

            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("rmi://localhost/ProductService", productService);
			
            ShoppingCartServiceFactory cartFactory = new ShoppingCartServiceFactoryImpl();
			Naming.rebind("rmi://localhost/CartFactory", cartFactory);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

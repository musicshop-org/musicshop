import application.api.ShoppingCartServiceFactoryImpl;
import org.hibernate.SessionFactory;
import sharedrmi.application.api.ShoppingCartServiceFactory;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    private static SessionFactory sessionFactory;

    public static void main(String[] args){
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            ShoppingCartServiceFactory cartFactory = new ShoppingCartServiceFactoryImpl();

            Naming.rebind("rmi://localhost/shoppingCartServiceFactory",cartFactory);
            System.out.println("Server is ready!");
        }catch(Exception e){
            System.out.println("Following Error occured: "+e.toString());
            e.printStackTrace();
        }
    }
}

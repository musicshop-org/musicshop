import application.ProductServiceImpl;
import domain.Album;
import domain.repositories.ProductRepository;
import infrastructure.ProductRepositoryImpl;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws AlbumNotFoundException, NoPermissionException, NotEnoughStockException {
//        try {
//            System.setProperty("java.rmi.server.hostname", "localhost");
//            //System.setSecurityManager(new SecurityManager());
//
//
//            RMIControllerFactory rmiControllerFactory = RMIControllerFactoryImpl.getInstance();
//            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
//            Naming.rebind("rmi://localhost/RMIControllerFactory", rmiControllerFactory);
//
//            System.out.println("Listening on port " + Registry.REGISTRY_PORT);
//
//        } catch (RemoteException | MalformedURLException e) {
//            e.printStackTrace();
//        }
    }
}

import application.ProductServiceImpl;
import communication.rmi.RMIControllerFactoryImpl;
import domain.Product;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.communication.rmi.RMIControllerFactory;
import sharedrmi.domain.enums.MediumType;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Main {
    public static void main(String[] args) throws RemoteException {

        try {
            RMIControllerFactory rmiControllerFactory = RMIControllerFactoryImpl.getInstance();
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("rmi://localhost/RMIControllerFactory", rmiControllerFactory);

            System.out.println("Listening on port " + Registry.REGISTRY_PORT);

        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }

        ProductService productService = new ProductServiceImpl();
        AlbumDTO albumDTO = productService.findAlbumByAlbumTitleAndMedium("24K Magic", MediumType.VINYL);
    }
}

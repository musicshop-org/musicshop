
import communication.rmi.RMIControllerFactoryImpl;

import domain.Artist;
import domain.Invoice;
import domain.InvoiceLineItem;
import domain.repositories.InvoiceRepository;
import domain.repositories.ProductRepository;
import infrastructure.InvoiceRepositoryImpl;
import infrastructure.ProductRepositoryImpl;
import sharedrmi.communication.rmi.RMIControllerFactory;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

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
    }
}

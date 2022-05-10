
import application.InvoiceServiceImpl;
import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.Address;
import sharedrmi.domain.valueobjects.CustomerData;
import sharedrmi.domain.valueobjects.InvoiceId;

import javax.naming.NoPermissionException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

        InvoiceService invoiceService = new InvoiceServiceImpl();
        invoiceService.createInvoice(
                InvoiceDTO.builder()
                        .invoiceId(new InvoiceId(666777))
                        .invoiceLineItems(List.of(new InvoiceLineItemDTO(MediumType.CD, "Thriller", 1, BigDecimal.TEN, 0)))
                        .paymentMethod(PaymentMethod.CASH)
                        .date(LocalDate.now())
                        .customerData(
                                CustomerData.builder()
                                        .firstName("Fabian")
                                        .lastName("Egartner")
                                        .email("fabian.egartner@test.at")
                                        .address(Address.builder()
                                                .addressCountry("AT")
                                                .addressLocality("Lustenau")
                                                .postalCode("6890")
                                                .streetAddress("Teststrasse")
                                                .build())
                                        .build())
                        .build()
                );
    }
}

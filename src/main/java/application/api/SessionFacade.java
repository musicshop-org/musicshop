package application.api;

import sharedrmi.application.api.*;
import sharedrmi.application.dto.CustomerDTO;
import sharedrmi.domain.valueobjects.Role;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SessionFacade extends ProductService, ShoppingCartService, CustomerService, InvoiceService, MessageProducerService, Remote {

     List<Role> getRoles() throws RemoteException;
     String getUsername() throws RemoteException;
}

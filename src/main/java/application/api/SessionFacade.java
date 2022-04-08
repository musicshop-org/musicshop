package application.api;

import sharedrmi.application.api.InvoiceService;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.domain.valueobjects.Role;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SessionFacade extends ProductService, ShoppingCartService, InvoiceService, Remote {

     List<Role> getRoles() throws RemoteException;
     String getUsername() throws RemoteException;
}

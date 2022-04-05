package application.api;

import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.application.dto.CustomerDTO;
import sharedrmi.domain.valueobjects.Role;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SessionFacade extends ProductService, ShoppingCartService, Remote {

    List<CustomerDTO> findCustomersByName(String name);

    List<Role> getRoles() throws RemoteException;
     String getUsername() throws RemoteException;
}

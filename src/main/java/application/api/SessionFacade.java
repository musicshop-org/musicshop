package application.api;

import sharedrmi.application.api.*;
import sharedrmi.application.dto.CustomerDTO;
import sharedrmi.domain.valueobjects.Role;

import javax.ejb.Remote;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

@Remote
public interface SessionFacade extends Serializable, ProductService, ShoppingCartService, CustomerService, InvoiceService, MessageProducerService, UserService {

     List<Role> getRoles();
     String getUsername();
}

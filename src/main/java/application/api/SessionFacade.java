package application.api;

import domain.valueobjects.Role;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;

import java.util.List;

public interface SessionFacade extends ProductService, ShoppingCartService {

     List<Role> getRoles();
     String getUsername();
}

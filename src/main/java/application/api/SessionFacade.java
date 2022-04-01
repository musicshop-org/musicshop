package application.api;

import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;
import sharedrmi.domain.valueobjects.Role;

import java.util.List;

public interface SessionFacade extends ProductService, ShoppingCartService {

     List<Role> getRoles();
     String getUsername();
}

package communication.rmi.api;

import application.LoginService;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;

import java.rmi.Remote;

public interface RMIController extends ProductService, ShoppingCartService, Remote {

}

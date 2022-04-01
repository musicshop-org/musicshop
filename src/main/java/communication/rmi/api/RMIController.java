package communication.rmi.api;

import sharedrmi.application.api.ProductService;
import sharedrmi.application.api.ShoppingCartService;

import java.rmi.Remote;

public interface RMIController extends ProductService, ShoppingCartService, Remote {

}

package infrastructure;

import domain.Employee;
import domain.LineItem;
import domain.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartRepository {
    Optional<ShoppingCart> findShoppingCartByEmployee (Employee employee);
    void createShoppingCartForEmployee(Employee employee);
}

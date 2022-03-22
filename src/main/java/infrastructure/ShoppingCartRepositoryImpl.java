package infrastructure;

import domain.Employee;
import domain.LineItem;
import domain.ShoppingCart;
import domain.valueobjects.EmployeeId;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ShoppingCartRepositoryImpl implements ShoppingCartRepository{
    private static List<ShoppingCart> shoppingCarts = new LinkedList<ShoppingCart>();

    @Override
    public Optional<ShoppingCart> findShoppingCartByEmployee(Employee employee) {
        for (ShoppingCart cart: shoppingCarts) {
            if (cart.getEmployee().getEmployeeId().getEmployeeId().equals(employee.getEmployeeId().getEmployeeId())){
                return Optional.of(cart);
            }
        }
        return Optional.empty();
    }

    @Override
    public void createShoppingCartForEmployee(Employee employee) {
        shoppingCarts.add(new ShoppingCart(employee));
    }

}

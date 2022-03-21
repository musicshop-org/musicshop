package domain;

import domain.valueobjects.EmployeeId;
import lombok.Getter;

@Getter
public class Employee {

    private Employee(){};

    private long id;
    private EmployeeId employeeId;
    private String name;
    private String username;
    private ShoppingCart shoppingCart;

    public Employee(EmployeeId employeeId, String name, String username) {
        this.employeeId = employeeId;
        this.name = name;
        this.username = username;
        this.shoppingCart = new ShoppingCart();
    }

    public Employee(EmployeeId employeeId, String name, String username, ShoppingCart shoppingCart) {
        this.employeeId = employeeId;
        this.name = name;
        this.username = username;
        this.shoppingCart = shoppingCart;
    }
}

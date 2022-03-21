package domain;

import domain.valueobjects.EmployeeId;
import lombok.Getter;

@Getter
public class Employee {

    private Employee(){};

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
}

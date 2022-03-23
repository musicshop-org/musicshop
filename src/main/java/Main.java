import domain.*;
import sharedrmi.domain.enums.MediumType;
import domain.valueobjects.EmployeeId;
import infrastructure.ShoppingCartRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.util.*;

public class Main {
    private static SessionFactory sessionFactory;

    public static void main(String[] args){
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }catch (Exception e){
            e.printStackTrace();
        }

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<LineItem> lineItems = new LinkedList<>();
        lineItems.add(new LineItem(MediumType.CD, "24K Magic", 12, BigDecimal.valueOf(18)));
        lineItems.add(new LineItem(MediumType.CD,"BAM BAM", 20, BigDecimal.valueOf(36)));
        lineItems.add(new LineItem(MediumType.CD,"Thriller", 35, BigDecimal.valueOf(54)));

        List<LineItem> lineItems2 = new LinkedList<>();
        lineItems2.add(new LineItem(MediumType.CD,"24K Magic", 12, BigDecimal.valueOf(18)));
        lineItems2.add(new LineItem(MediumType.CD,"BAM BAM", 20, BigDecimal.valueOf(36)));
        lineItems2.add(new LineItem(MediumType.CD,"Thriller", 35, BigDecimal.valueOf(54)));
        lineItems2.add(new LineItem(MediumType.VINYL, "Thriller", 50, BigDecimal.valueOf(51)));

        Employee emp = new Employee(new EmployeeId(), "John", "j2022");
        Employee emp2 = new Employee(new EmployeeId(), "Karl", "k2022");

        session.persist(emp);
        session.persist(emp2);
        session.getTransaction().commit();
        session.close();

        ShoppingCartRepositoryImpl cartRepo = new ShoppingCartRepositoryImpl();
        cartRepo.createShoppingCart(emp.getEmployeeId().getEmployeeId());
        cartRepo.findShoppingCartByOwnerId(emp.getEmployeeId().getEmployeeId()).get().addLineItem(lineItems.get(0));

    }
}

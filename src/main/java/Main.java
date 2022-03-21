import domain.*;
import domain.enums.MediumType;
import domain.valueobjects.AlbumId;
import domain.valueobjects.EmployeeId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        lineItems.add(new LineItem("24K Magic", 1, BigDecimal.valueOf(18)));
        lineItems.add(new LineItem("BAM BAM", 2, BigDecimal.valueOf(36)));
        lineItems.add(new LineItem("Thriller", 3, BigDecimal.valueOf(54)));

        Employee emp = new Employee(new EmployeeId(), "John", "j2022");

//        Set<Song> songs = new HashSet<Song>();
//        for (int i = 0; i < 5; i++) {
//            String title = "test title " + i;
//            songs.add(new Song(title, BigDecimal.valueOf(3.00),99999, MediumType.DIGITAL, LocalDate.of(1990,03,13),"test genre", artists));
//        }
//        Album album = new Album("Test Album", BigDecimal.valueOf(30.00),10, MediumType.CD, LocalDate.of(1992,03,13),new AlbumId(),"Test label",songs);
//        session.persist(album);
//
//        album = new Album("Test Album 2", BigDecimal.valueOf(15.00),10, MediumType.CD, LocalDate.of(1992,03,13),new AlbumId(),"Test label 2",songs);
        session.persist(emp);
        session.getTransaction().commit();

//        List<Album> albums = session.createQuery("from Album",Album.class).list();
//        System.out.println(albums.get(0).toString());


        session.close();
    }
}
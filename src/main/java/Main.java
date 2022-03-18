import domain.Album;
import domain.Artist;
import domain.Song;
import domain.enums.MediumType;
import domain.valueobjects.AlbumId;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Main {
    private static SessionFactory sessionFactory;
    public static void main(String[] args){

        System.out.println("Hello World");
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }catch (Exception e){
            e.printStackTrace();
        }

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        /*
        List<Artist> artists = new LinkedList<Artist>();
        artists.add(new Artist("Jake"));

        List<Song> songs = new LinkedList<Song>();
        for (int i = 0; i < 5; i++) {
            String title = "test title " + i;
            songs.add(new Song(title, BigDecimal.valueOf(3.00),99999, MediumType.DIGITAL, LocalDate.of(1990,03,13),"test genre", artists));
        }
        Album album = new Album("Test Album", BigDecimal.valueOf(30.00),10, MediumType.CD, LocalDate.of(1992,03,13),new AlbumId(),"Test label",songs);
        session.persist(album);
        session.getTransaction().commit();

        List<Song> songs1 = session.createQuery("from Song",Song.class).list();
        Song song1 = songs1.get(0);
        System.out.println(song1);

         */
        session.close();
    }
}
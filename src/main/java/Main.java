import application.ProductServiceImpl;
import domain.Album;
import infrastructure.ProductRepositoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    private static SessionFactory sessionFactory;
    public static void main(String[] args) throws RemoteException {

        try {
            ProductServiceImpl productService = new ProductServiceImpl();

            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("rmi://localhost/ProductService", productService);

        } catch (Exception e){
            e.printStackTrace();
        }

    /*
        List<Artist> artists = new LinkedList<Artist>();
        artists.add(new Artist("Jake"));

        Set<Song> songs = new HashSet<Song>();
        for (int i = 0; i < 5; i++) {
            String title = "test title " + i;
            songs.add(new Song(title, BigDecimal.valueOf(3.00),99999, MediumType.DIGITAL, LocalDate.of(1990,03,13),"test genre", artists));
        }
        Album album = new Album("Test Album", BigDecimal.valueOf(30.00),10, MediumType.CD, LocalDate.of(1992,03,13),new AlbumId(),"Test label",songs);
        session.persist(album);

        album = new Album("Test Album 2", BigDecimal.valueOf(15.00),10, MediumType.CD, LocalDate.of(1992,03,13),new AlbumId(),"Test label 2",songs);
        session.persist(album);
        session.getTransaction().commit();
*/
    }
}
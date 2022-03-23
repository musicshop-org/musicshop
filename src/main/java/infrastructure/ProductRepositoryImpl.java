package infrastructure;

import domain.Album;
import domain.Song;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.rmi.RemoteException;
import java.util.*;

public class ProductRepositoryImpl implements ProductRepository {
    private SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public Set<Album> findAlbumsBySongTitle(String title) throws RemoteException {

        Set<Album> albums = new HashSet<>();
        Session session = sessionFactory.openSession();

        List<Song> songResults = session.createQuery("from Song where lower(title) = lower(:title)", Song.class).setParameter("title", title).list();

        for (Song songResult : songResults) {
            for (Album album : songResult.getInAlbum()) {
                albums.add(album);
            }
        }

        session.close();
        return albums;
    }

    @Override
    public List<Song> findSongsByTitle(String title) throws RemoteException {

        Session session = sessionFactory.openSession();
        List<Song> songResults = session.createQuery("from Song where lower(title) = lower(:title)", Song.class).setParameter("title", title).list();
        session.close();
        return songResults;
    }

    // TODO: implement methods
    @Override
    public List<String> findArtistsByName(String name) throws RemoteException {
        return null;
    }
}
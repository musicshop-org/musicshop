package infrastructure;

import domain.Album;
import domain.Artist;
import domain.Song;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.rmi.RemoteException;
import java.util.*;

public class ProductRepositoryImpl implements ProductRepository {
    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

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

        return albums;
    }

    @Override
    public List<Song> findSongsByTitle(String title) throws RemoteException {

        Session session = sessionFactory.openSession();
        List<Song> songResults = session.createQuery("from Song where lower(title) = lower(:title)", Song.class).setParameter("title", title).list();

        return songResults;
    }

    @Override
    public List<Artist> findArtistsByName(String name) throws RemoteException {

        Session session = sessionFactory.openSession();
        List<Artist> artistResults = session.createQuery("from Artist where lower(name) = lower(:name)", Artist.class).setParameter("name", name).list();

        return artistResults;
    }
}
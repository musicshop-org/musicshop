package infrastructure;

import domain.Album;
import domain.Artist;
import domain.Song;

import domain.repositories.ProductRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;

public class ProductRepositoryImpl implements ProductRepository {
    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public Set<Album> findAlbumsBySongTitle(String title) {

        Set<Album> albums = new HashSet<>();
        Session session = sessionFactory.openSession();
        title = "%"+title+"%";
        List<Song> songResults = session.createQuery("from Song where lower(title) LIKE lower(:title)", Song.class).setParameter("title", title).list();

        for (Song songResult : songResults) {
            for (Album album : songResult.getInAlbum()) {
                albums.add(album);
            }
        }

        return albums;
    }

    @Override
    public List<Song> findSongsByTitle(String title) {

        Session session = sessionFactory.openSession();
        title = "%"+title+"%";
        List<Song> songResults = session.createQuery("from Song where lower(title) LIKE lower(:title)", Song.class).setParameter("title", title).list();

        return songResults;
    }

    @Override
    public List<Artist> findArtistsByName(String name) {

        Session session = sessionFactory.openSession();
        name = "%"+name+"%";
        List<Artist> artistResults = session.createQuery("from Artist where lower(name) LIKE lower(:name)", Artist.class).setParameter("name", name).list();

        return artistResults;
    }
}
package infrastructure;

import domain.Artist;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public List<String> findAlbumsByTitle(String title) throws RemoteException {
        //List<Product> result = new ArrayList<>();
        List<String> result = new ArrayList<>();
        List<Integer> product_id = new ArrayList<>();
        List<Integer> albums_id = new ArrayList<>();
        List<Integer> album_songs_id = new ArrayList<>();
        List<Integer> artist_songs_id = new ArrayList<>();
        List<Artist> artists = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://10.0.40.162:5432/postgres";
            String user = "postgres";
            String password = "dbadmin!2020";

            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();

            String fetchquery = "select * from tbl_product where \"title\" like '" + title + "'";
            ResultSet rs = st.executeQuery(fetchquery);

            while (rs.next()) {
                if (rs.getString("title").toLowerCase().equals(title.toLowerCase())){
                    //result = new Song.SongBuilder(rs.getString("genre"), new List<Artist>());
                    product_id.add(Integer.valueOf(rs.getString("product_id")));
                    System.out.println("song_id = " + Integer.valueOf(rs.getString("product_id")));
                }
            }

            for (int i = 0; i < product_id.size(); i++) {
                int song_id = product_id.get(i);
                fetchquery = "select * from album_song  where \"song_id\" = '" +song_id+ "'";
                rs = st.executeQuery(fetchquery);
                while (rs.next()){
                    if (Integer.valueOf(rs.getString("song_id")) == product_id.get(i)){
                        albums_id.add(Integer.valueOf(rs.getString("album_id")));
                        System.out.println("album_id = " + Integer.valueOf(rs.getString("album_id")));
                    }
                }
            }

            for (int i = 0; i < albums_id.size(); i++) {
                int album_id = albums_id.get(i);
                int count = 0;
                fetchquery = "select * from album_song  where \"album_id\" = '" +album_id+ "'";
                rs = st.executeQuery(fetchquery);
                while (rs.next()){
                    if (Integer.valueOf(rs.getString("album_id")) == albums_id.get(i)){
                        album_songs_id.add(Integer.valueOf(rs.getString("song_id")));
                        System.out.println("album: " + album_id + " song_id: " + album_songs_id.get(count));
                        count++;
                    }
                }
                count = 0;
            }

            for (int i = 0; i < album_songs_id.size(); i++) {
                int album_song = album_songs_id.get(i);
                int count = 0;
                fetchquery = "select * from artist_song  where \"song_id\" = '" +album_song+ "'";
                rs = st.executeQuery(fetchquery);
                while (rs.next()){
                    if (Integer.valueOf(rs.getString("song_id")) == album_song){
                        artist_songs_id.add(Integer.valueOf(rs.getString("artist_id")));
                        System.out.println("artist: " + artist_songs_id.get(count));
                        count++;
                    }
                }
                count = 0;
            }

            for (int i = 0; i < artist_songs_id.size(); i++) {
                int artist_id = artist_songs_id.get(i);
                int count = 0;
                fetchquery = "select * from tbl_artist  where \"artist_id\" = '" +artist_id+ "'";
                rs = st.executeQuery(fetchquery);
                while (rs.next()){
                    if (Integer.valueOf(rs.getString("artist_id")) == artist_id){
                        artists.add(new Artist(rs.getString("name")));
                        System.out.println(artists.get(0).toString());
                    }
                }
            }

            st.close();
            con.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    return result;
    }

    // TODO: implement methods
    @Override
    public List<String> findSongsByTitle(String title) throws RemoteException {
        return null;
    }

    @Override
    public List<String> findArtistsByName(String name) throws RemoteException {
        return null;
    }
}
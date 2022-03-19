package infrastructure;

import domain.Album;
import domain.Artist;
import domain.Product;
import domain.Song;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class AlbumRepositoryImpl implements AlbumRepository {

    @Override
    public List<String> findAlbum(String musicName) throws SQLException, ClassNotFoundException {
        //List<Product> result = new ArrayList<>();
        List<String> result = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://10.0.40.162:5432/postgres";
            String user = "postgres";
            String password = "dbadmin!2020";

            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();

            String fetchquery = "select * from tbl_product";

            ResultSet rs = st.executeQuery(fetchquery);

            while (rs.next()) {
                if (rs.getString("title").toLowerCase().equals(musicName.toLowerCase())){
                    //result = new Song.SongBuilder(rs.getString("genre"), new List<Artist>());
                    result.add(rs.getString("title"));
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
}
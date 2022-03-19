package infrastructure;

import domain.Album;

import java.sql.ResultSet;
import java.util.List;
import java.sql.*;

public class AlbumRepositoryImpl implements AlbumRepository {

    @Override
    public List<Album> findAlbum(String musicName) throws SQLException, ClassNotFoundException {


        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://10.0.40.162:5432/postgres";
            String user = "postgres";
            String password = "dbadmin!2020";

            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();

            String fetchquery = "select * from product";

            ResultSet rs = st.executeQuery(fetchquery);


            while (rs.next()) {
                System.out.println(
                        rs.getString("title")
                );
            }
            st.close();
            con.close();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
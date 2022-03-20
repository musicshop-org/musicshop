package infrastructure;

import domain.Album;
import domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface AlbumRepository {

    List<String> findAlbum(String musicName) throws SQLException, ClassNotFoundException;

}

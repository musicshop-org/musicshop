package infrastructure;

import domain.Album;

import java.sql.SQLException;
import java.util.List;

public interface AlbumRepository {

    List<Album> findAlbum(String musicName) throws SQLException, ClassNotFoundException;

}

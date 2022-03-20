import infrastructure.AlbumRepositoryImpl;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AlbumRepositoryImpl albumRepository = new AlbumRepositoryImpl();
        System.out.println(albumRepository.findAlbum("Beautiful"));

    }
}
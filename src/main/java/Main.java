import application.ProductServiceImpl;
import domain.Album;
import domain.Product;
import domain.Song;
import domain.repositories.ProductRepository;
import infrastructure.ProductRepositoryImpl;
import sharedrmi.application.api.ProductService;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.SongDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
    }
}
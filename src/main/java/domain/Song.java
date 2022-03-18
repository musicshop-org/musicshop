package domain;

import domain.enums.MediumType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class Song extends Product {

    private Song(){
    }

    private String genre;
    private List<Artist> artists;

    public Song(String title, BigDecimal price, int stock, MediumType mediumType, LocalDate releaseDate, String genre, List<Artist> artists) {
        this.genre = genre;
        this.artists = artists;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.mediumType = mediumType;
        this.releaseDate = releaseDate;
    }
}

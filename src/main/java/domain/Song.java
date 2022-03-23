package domain;

import sharedrmi.domain.enums.MediumType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Song extends Product {

    private Song(){}

    private String genre;
    private List<Artist> artists;
    private Set<Album> inAlbum = new HashSet<Album>();

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

package domain;

import lombok.Getter;
import sharedrmi.domain.enums.MediumType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class Song extends Product {

    private String genre;
    private List<Artist> artists;
    private Set<Album> inAlbum = new HashSet<>();

    protected Song() {
    }

    public Song(String title, BigDecimal price, int stock, MediumType mediumType, LocalDate releaseDate, String genre, List<Artist> artists) {
        this.genre = genre;
        this.artists = artists;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.mediumType = mediumType;
        this.releaseDate = releaseDate;
    }

    public Song(String title, BigDecimal price, int stock, MediumType mediumType, LocalDate releaseDate, String genre, List<Artist> artists, Set<Album> inAlbum) {
        this.genre = genre;
        this.artists = artists;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.mediumType = mediumType;
        this.releaseDate = releaseDate;
        this.inAlbum = inAlbum;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", mediumType=" + mediumType +
                ", releaseDate=" + releaseDate +
                ", genre='" + genre + '\'' +
                ", artists=" + artists +
                ", inAlbum=" + inAlbum +
                '}';
    }
}

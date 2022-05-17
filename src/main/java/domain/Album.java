package domain;

import lombok.Getter;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Album extends Product {

    private String label;
    private String imageUrl;
    private AlbumId albumId;
    private Set<Song> songs = new HashSet<>();

    protected Album() {
    }

    public Album(String title, String imageUrl, BigDecimal price, int stock, MediumType mediumType, LocalDate releaseDate, AlbumId albumId, String label, Set<Song> songs) {
        this.label = label;
        this.imageUrl = imageUrl;
        this.albumId = albumId;
        this.songs = songs;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.mediumType = mediumType;
        this.releaseDate = releaseDate;
    }

    public void increaseStock(int increaseAmount){
        this.stock += increaseAmount;
    }

    public void decreaseStock(int decreaseAmount) { this.stock -= decreaseAmount; }
}

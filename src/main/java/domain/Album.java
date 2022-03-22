package domain;

import sharedrmi.application.dto.enums.MediumType;
import sharedrmi.application.dto.valueobjects.AlbumId;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Album extends Product {

    private Album(){}

    private String label;
    private AlbumId albumId;
    private Set<Song> songs= new HashSet<Song>();

    public Album(String title, BigDecimal price, int stock, MediumType mediumType, LocalDate releaseDate, AlbumId albumId, String label, Set<Song> songs) {
        this.label = label;
        this.albumId = albumId;
        this.songs = songs;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.mediumType = mediumType;
        this.releaseDate = releaseDate;
    }
}

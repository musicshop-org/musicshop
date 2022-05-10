//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sharedrmi.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import sharedrmi.domain.enums.MediumType;
import sharedrmi.domain.valueobjects.AlbumId;

public class AlbumDTOString implements Serializable {

    private final String title;
    private final BigDecimal price;
    private final int stock;
    private final MediumType mediumType;
    private final String releaseDate;
    private final AlbumId albumId;
    private final String label;

    public AlbumDTOString(String title, BigDecimal price, int stock, MediumType mediumType, String releaseDate, AlbumId albumId, String label) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.mediumType = mediumType;
        this.releaseDate = releaseDate;
        this.albumId = albumId;
        this.label = label;
    }

    public static AlbumDTO.AlbumDTOBuilder builder() {
        return new AlbumDTO.AlbumDTOBuilder();
    }

    public String getTitle() {
        return this.title;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public int getStock() {
        return this.stock;
    }

    public MediumType getMediumType() {
        return this.mediumType;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public AlbumId getAlbumId() {
        return this.albumId;
    }

    public String getLabel() {
        return this.label;
    }


    public String toString() {
        return "AlbumDTO.AlbumDTOBuilder(title=" + this.title + ", price=" + this.price + ", stock=" + this.stock + ", mediumType=" + this.mediumType + ", releaseDate=" + this.releaseDate + ", albumId=" + this.albumId + ", label=" + this.label + ")";
    }
}

package domain;

import sharedrmi.domain.enums.MediumType;
import org.junit.jupiter.api.Test;
import sharedrmi.domain.valueobjects.AlbumId;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlbumTest {

    @Test
    void given_album_when_increasestock_then_stockincreased () {

        // given
        final int givenStock = 8;
        final int increaseQuantity = 5;
        final int expectedQuantity = 13;

        Album album = new Album("label1",
                            BigDecimal.TEN,
                            givenStock,
                            MediumType.CD,
                            LocalDate.now(),
                            new AlbumId(),
                            "title1",
                            new HashSet<>());

        // when
        album.increaseStock(increaseQuantity);

        // then
        assertEquals(expectedQuantity, album.getStock());
    }

    @Test
    void given_album_when_decreasestock_then_stockdecreased () {

        // given
        final int givenStock = 8;
        final int decreaseQuantity = 5;
        final int expectedQuantity = 3;

        Album album = new Album("label1",
                BigDecimal.TEN,
                givenStock,
                MediumType.CD,
                LocalDate.now(),
                new AlbumId(),
                "title1",
                new HashSet<>());

        // when
        album.decreaseStock(decreaseQuantity);

        // then
        assertEquals(expectedQuantity, album.getStock());
    }
}

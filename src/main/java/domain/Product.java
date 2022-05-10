package domain;

import lombok.Getter;
import sharedrmi.domain.enums.MediumType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public abstract class Product implements Serializable {

    protected long id;
    protected String title;
    protected BigDecimal price;
    protected int stock;
    protected MediumType mediumType;
    protected LocalDate releaseDate;

}

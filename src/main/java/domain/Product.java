package domain;

import domain.enums.MediumType;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Product {

    protected long id;
    protected String title;
    protected BigDecimal price;
    protected int stock;
    protected MediumType mediumType;
    protected LocalDate releaseDate;
}

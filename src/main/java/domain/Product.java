package domain;


import sharedrmi.application.dto.enums.MediumType;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public abstract class Product {

    protected long id;
    protected String title;
    protected BigDecimal price;
    protected int stock;
    protected MediumType mediumType;
    protected LocalDate releaseDate;
}

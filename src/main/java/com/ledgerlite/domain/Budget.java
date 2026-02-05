package main.java.com.ledgerlite.domain;

import java.time.YearMonth;
import java.util.Objects;

public record Budget(YearMonth period, Category category, Money limit) {
    public Budget {
        Objects.requireNonNull(period,"period не может быть пустым");
        Objects.requireNonNull(category,"category не может быть пустым");
        Objects.requireNonNull(limit,"limit не может быть пустым");
    }

}

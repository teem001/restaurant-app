package com.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }


    public boolean isGreaterThan(Money money){
        return this.amount != null && this.amount.compareTo(money.getAmount())>0;
    }

    public Money add(Money money){
        return new Money(this.amount.add(money.getAmount()));
    }

    public Money subtract(Money money){
        return isGreaterThan(money) ?new Money(this.amount.subtract(money.getAmount())) : new Money(BigDecimal.ZERO);
    }

    private BigDecimal setScale(BigDecimal input){
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money multiply(int quantity) {
        return new Money(setScale(this.amount.multiply(new BigDecimal(quantity))));
    }
}

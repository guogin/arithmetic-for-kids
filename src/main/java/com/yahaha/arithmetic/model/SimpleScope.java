package com.yahaha.arithmetic.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleScope extends Scope {
    @Min(1)
    @Max(8)
    private int numberOfDigits;

    @NotNull
    private boolean carryOrBorrowEnabled;

    public SimpleScope(Operator operator, int numberOfQuestions, int numberOfDigits, boolean carryOrBorrowEnabled) {
        super(operator, numberOfQuestions);

        this.numberOfDigits = numberOfDigits;
        this.carryOrBorrowEnabled = carryOrBorrowEnabled;
    }
}

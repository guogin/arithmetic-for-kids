package com.yahaha.arithmetic.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SimpleScope extends Scope {
    @NotNull
    private Operator operator;

    @Min(1)
    @Max(1000)
    private int numberOfQuestions;

    @Min(1)
    @Max(8)
    private int numberOfDigits;

    @NotNull
    private boolean carryOrBorrow;
}

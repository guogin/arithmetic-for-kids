package com.yahaha.arithmetic.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Scope {
    @NotNull
    private Operator operator;

    @Min(1)
    private int numberOfQuestions;

    @Min(0)
    private int minLeftOperand;

    @Min(0)
    private int maxLeftOperand;

    @Min(0)
    private int minRightOperand;

    @Min(0)
    private int maxRightOperand;

    @Min(0)
    private int minAnswer;

    @Min(0)
    private int maxAnswer;
}
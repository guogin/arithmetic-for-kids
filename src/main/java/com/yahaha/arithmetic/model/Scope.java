package com.yahaha.arithmetic.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Scope {
    @NotNull
    private Operator operator;

    @Min(1)
    @Max(1000)
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
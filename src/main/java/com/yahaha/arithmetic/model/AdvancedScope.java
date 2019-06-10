package com.yahaha.arithmetic.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdvancedScope extends Scope {
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

    public AdvancedScope(Operator operator, int numberOfQuestions,
                         int minLeftOperand, int maxLeftOperand,
                         int minRightOperand, int maxRightOperand,
                         int minAnswer, int maxAnswer) {
        super(operator, numberOfQuestions);
        this.minLeftOperand = minLeftOperand;
        this.maxLeftOperand = maxLeftOperand;
        this.minRightOperand = minRightOperand;
        this.maxRightOperand = maxRightOperand;
        this.minAnswer = minAnswer;
        this.maxAnswer = maxAnswer;
    }
}
package com.yahaha.arithmetic.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Question {
    private Operator operator;
    private int leftOperand;
    private int rightOperand;

    public int getAnswer() {
        if (operator == Operator.PLUS) {
            return leftOperand + rightOperand;
        } else {
            return leftOperand - rightOperand;
        }
    }

    public String getExpression() {
        return leftOperand +
                (operator == Operator.PLUS ? " + " : " - ") +
                rightOperand +
                " = ";
    }
}

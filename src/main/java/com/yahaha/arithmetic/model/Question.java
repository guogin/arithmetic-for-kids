package com.yahaha.arithmetic.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public String toString() {
        return leftOperand +
                (operator == Operator.PLUS ? " + " : " - ") +
                rightOperand +
                " = ";
    }
}

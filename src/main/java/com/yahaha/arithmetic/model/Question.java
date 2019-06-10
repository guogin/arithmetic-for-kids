package com.yahaha.arithmetic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Question {
    @JsonIgnore
    private Operator operator;
    @JsonIgnore
    private int leftOperand;
    @JsonIgnore
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

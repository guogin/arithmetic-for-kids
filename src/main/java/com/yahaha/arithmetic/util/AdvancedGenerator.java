package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
public class AdvancedGenerator implements Generator {
    private Scope scope;

    public Scope getScope() {
        return scope;
    }

    public void setScope(@Valid Scope scope) {
        this.scope = scope;
    }

    @Override
    public List<Question> generateQuestions() throws InvalidScopeException {
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < getScope().getNumberOfQuestions(); ++i) {
            int leftOp = RandomUtil.randomWithRange(getScope().getMinLeftOperand(), getScope().getMaxLeftOperand());

            // Derive the boundary of the right operand
            int minRightOp = getScope().getOperator() == Operator.PLUS ?
                    getScope().getMinAnswer() - leftOp :
                    leftOp - getScope().getMaxAnswer();
            int maxRightOp = getScope().getOperator() == Operator.PLUS ?
                    getScope().getMaxAnswer() - leftOp :
                    leftOp - getScope().getMinAnswer();
            minRightOp = max(max(minRightOp, 0), getScope().getMinRightOperand());
            maxRightOp = min(max(maxRightOp, 0), getScope().getMaxRightOperand());

            if (minRightOp <= maxRightOp) {
                int rightOp = RandomUtil.randomWithRange(minRightOp, maxRightOp);

                Question question = new Question(getScope().getOperator(), leftOp, rightOp);
                questionList.add(question);
            } else { //cannot generateQuestions such a number
                throw new InvalidScopeException(getScope(), InvalidScopeException.DERIVED_MIN_GT_MAX);
            }
        }

        return questionList;
    }
}

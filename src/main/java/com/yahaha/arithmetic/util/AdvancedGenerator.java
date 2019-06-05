package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
public class AdvancedGenerator implements Generator {
    private AdvancedScope advancedScope;

    public AdvancedScope getAdvancedScope() {
        return advancedScope;
    }

    public void setAdvancedScope(@Valid AdvancedScope advancedScope) {
        this.advancedScope = advancedScope;
    }

    @Override
    public List<Question> generateQuestions() throws InvalidScopeException {
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < getAdvancedScope().getNumberOfQuestions(); ++i) {
            int leftOp = RandomUtil.randomWithRange(getAdvancedScope().getMinLeftOperand(), getAdvancedScope().getMaxLeftOperand());

            // Derive the boundary of the right operand
            int minRightOp = getAdvancedScope().getOperator() == Operator.PLUS ?
                    getAdvancedScope().getMinAnswer() - leftOp :
                    leftOp - getAdvancedScope().getMaxAnswer();
            int maxRightOp = getAdvancedScope().getOperator() == Operator.PLUS ?
                    getAdvancedScope().getMaxAnswer() - leftOp :
                    leftOp - getAdvancedScope().getMinAnswer();
            minRightOp = max(max(minRightOp, 0), getAdvancedScope().getMinRightOperand());
            maxRightOp = min(max(maxRightOp, 0), getAdvancedScope().getMaxRightOperand());

            if (minRightOp <= maxRightOp) {
                int rightOp = RandomUtil.randomWithRange(minRightOp, maxRightOp);

                Question question = new Question(getAdvancedScope().getOperator(), leftOp, rightOp);
                questionList.add(question);
            } else { //cannot generateQuestions such a number
                throw new InvalidScopeException(getAdvancedScope(), InvalidScopeException.DERIVED_MIN_GT_MAX);
            }
        }

        return questionList;
    }
}

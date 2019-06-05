package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.yahaha.arithmetic.util.RandomUtil.randomWithRange;
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
            // First generate one number
            int minLeftOp = getAdvancedScope().getMinLeftOperand();
            int maxLeftOp = getAdvancedScope().getMaxLeftOperand();
            int leftOp = randomWithRange(minLeftOp, maxLeftOp);

            // Then derive the boundary of the other number
            int minRightOp = getAdvancedScope().getOperator() == Operator.PLUS ?
                    getAdvancedScope().getMinAnswer() - leftOp :
                    leftOp - getAdvancedScope().getMaxAnswer();
            int maxRightOp = getAdvancedScope().getOperator() == Operator.PLUS ?
                    getAdvancedScope().getMaxAnswer() - leftOp :
                    leftOp - getAdvancedScope().getMinAnswer();
            minRightOp = max(minRightOp, getAdvancedScope().getMinRightOperand());
            maxRightOp = max(min(maxRightOp, getAdvancedScope().getMaxRightOperand()), 0);

            if (minRightOp <= maxRightOp) {
                int rightOp = randomWithRange(minRightOp, maxRightOp);

                Question question = new Question(getAdvancedScope().getOperator(), leftOp, rightOp);
                questionList.add(question);
            } else { //cannot generateQuestions such a number
                throw new InvalidScopeException(getAdvancedScope(), minRightOp, maxRightOp, InvalidScopeException.DERIVED_MIN_GT_MAX);
            }
        }

        return questionList;
    }
}

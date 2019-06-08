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
            int minLeftOp = getAdvancedScope().getOperator() == Operator.PLUS ?
                    getAdvancedScope().getMinAnswer() - getAdvancedScope().getMaxRightOperand() :
                    getAdvancedScope().getMinRightOperand() + getAdvancedScope().getMinAnswer();
            int maxLeftOp = getAdvancedScope().getOperator() == Operator.PLUS ?
                    getAdvancedScope().getMaxAnswer() - getAdvancedScope().getMinRightOperand() :
                    getAdvancedScope().getMaxRightOperand() + getAdvancedScope().getMaxAnswer();
            minLeftOp = max(minLeftOp, getAdvancedScope().getMinLeftOperand());
            maxLeftOp = max(min(maxLeftOp, getAdvancedScope().getMaxLeftOperand()), 0);

            if (minLeftOp > maxLeftOp) {
                throw new InvalidScopeException(InvalidScopeException.DERIVED_MIN_GT_MAX,
                        String.format("Can't generate the left operand because derived range is [%1$d, %2$d] where %1$d > %2$d. Details: scope = %3$s",
                                minLeftOp, maxLeftOp, getAdvancedScope().toString()));
            }
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
                throw new InvalidScopeException(InvalidScopeException.DERIVED_MIN_GT_MAX,
                        String.format("Can't generate the right operand because derived range is [%1$d, %2$d] where %1$d > %2$d. Details: left operand = %3$d, scope = %4$s",
                                minRightOp, maxRightOp, leftOp, getAdvancedScope().toString()));
            }
        }

        return questionList;
    }
}

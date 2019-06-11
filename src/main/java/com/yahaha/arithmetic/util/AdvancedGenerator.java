package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;

import java.util.ArrayList;
import java.util.List;

import static com.yahaha.arithmetic.util.RandomUtil.randomWithRange;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class AdvancedGenerator implements Generator {
    private AdvancedScope advancedScope;

    @Override
    public AdvancedScope getScope() {
        return advancedScope;
    }

    @Override
    public void setScope(Scope scope) {
        this.advancedScope = (AdvancedScope) scope;
    }

    @Override
    public List<Question> generateQuestions() throws InvalidScopeException {
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < getScope().getNumberOfQuestions(); ++i) {
            // First generate one number
            int minLeftOp = getScope().getOperator() == Operator.PLUS ?
                    getScope().getMinAnswer() - getScope().getMaxRightOperand() :
                    getScope().getMinRightOperand() + getScope().getMinAnswer();
            int maxLeftOp = getScope().getOperator() == Operator.PLUS ?
                    getScope().getMaxAnswer() - getScope().getMinRightOperand() :
                    getScope().getMaxRightOperand() + getScope().getMaxAnswer();
            minLeftOp = max(minLeftOp, getScope().getMinLeftOperand());
            maxLeftOp = max(min(maxLeftOp, getScope().getMaxLeftOperand()), 0);

            if (minLeftOp > maxLeftOp) {
                throw new InvalidScopeException(
                        String.format("Can't generate the left operand because derived range is [%1$d, %2$d] where %1$d > %2$d. Details: scope = %3$s", minLeftOp, maxLeftOp, getScope().toString()),
                        "derived.min.gt.max", getScope().getOperator() == Operator.PLUS ? "+" : "-", getScope().toString());
            }
            int leftOp = randomWithRange(minLeftOp, maxLeftOp);

            // Then derive the boundary of the other number
            int minRightOp = getScope().getOperator() == Operator.PLUS ?
                    getScope().getMinAnswer() - leftOp :
                    leftOp - getScope().getMaxAnswer();
            int maxRightOp = getScope().getOperator() == Operator.PLUS ?
                    getScope().getMaxAnswer() - leftOp :
                    leftOp - getScope().getMinAnswer();
            minRightOp = max(minRightOp, getScope().getMinRightOperand());
            maxRightOp = max(min(maxRightOp, getScope().getMaxRightOperand()), 0);

            if (minRightOp <= maxRightOp) {
                int rightOp = randomWithRange(minRightOp, maxRightOp);

                Question question = new Question(getScope().getOperator(), leftOp, rightOp);
                questionList.add(question);
            } else { //cannot generateQuestions such a number
                throw new InvalidScopeException(
                        String.format("Can't generate the right operand because derived range is [%1$d, %2$d] where %1$d > %2$d. Details: left operand = %3$d, scope = %4$s", minRightOp, maxRightOp, leftOp, getScope().toString()),
                        "derived.min.gt.max", getScope().getOperator() == Operator.PLUS ? "+" : "-", getScope().toString());
            }
        }

        return questionList;
    }
}

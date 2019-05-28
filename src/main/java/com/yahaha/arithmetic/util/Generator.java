package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
public class Generator {
    private Scope scope;

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public List<Question> generate() {
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < getScope().getNumberOfQuestions(); ++i) {
            int leftOp = RandomUtil.randomWithRange(getScope().getMinLeftOperand(), getScope().getMaxLeftOperand());

            int minRightOp, maxRightOp;
            if (getScope().getOperator() == Operator.PLUS) {
                minRightOp = getScope().getMinAnswer() - leftOp;
                maxRightOp = getScope().getMaxAnswer() - leftOp;
            } else {
                minRightOp = leftOp - getScope().getMaxAnswer();
                maxRightOp = leftOp - getScope().getMinAnswer();
            }
            minRightOp = max(minRightOp, getScope().getMinRightOperand());
            maxRightOp = min(maxRightOp, getScope().getMaxRightOperand());

            int rightOp = RandomUtil.randomWithRange(minRightOp, maxRightOp);

            Question question = new Question(getScope().getOperator(), leftOp, rightOp);
            questionList.add(question);
        }

        return questionList;
    }
}

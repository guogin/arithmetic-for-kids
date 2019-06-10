package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;
import com.yahaha.arithmetic.model.SimpleScope;

import java.util.ArrayList;
import java.util.List;

import static com.yahaha.arithmetic.util.NumberUtil.composeOf;
import static com.yahaha.arithmetic.util.RandomUtil.randomWithRange;

public class SimpleGenerator implements Generator {
    private SimpleScope simpleScope;

    @Override
    public SimpleScope getScope() {
        return simpleScope;
    }

    public void setScope(Scope scope) {
        this.simpleScope = (SimpleScope) scope;
    }

    @Override
    public List<Question> generateQuestions() throws InvalidScopeException {
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < getScope().getNumberOfQuestions(); ++i) {
            Integer[] leftDigits = new Integer[getScope().getNumberOfDigits()];
            Integer[] rightDigits = new Integer[getScope().getNumberOfDigits()];

            int carry = 0, borrow = 0; // 0 or 1
            boolean hasCarryOrBorrowOnTheRight = false;

            for (int j = getScope().getNumberOfDigits() - 1; j >= 0; --j) {
                if (getScope().isCarryOrBorrowEnabled()) {
                    if ((j == 0 && !hasCarryOrBorrowOnTheRight) || // if this is the left most digit however we didn't generate any carry or borrow operations on the right
                            (randomWithRange(0, 1) == 0)) {          // or let's flip a coin, 0 = we should generate carry or borrow operation on this digit
                        if (getScope().getOperator() == Operator.PLUS) {
                            leftDigits[j] = randomWithRange(1 - carry, 9); // if left digit is 0, then carry could not happen
                            rightDigits[j] = randomWithRange(10 - leftDigits[j] - carry, 9);

                            hasCarryOrBorrowOnTheRight = true;
                            carry = 1;
                        } else {
                            leftDigits[j] = randomWithRange(0, 9 - (1 - borrow)); // if left digit is 9, then borrow could not happen
                            rightDigits[j] = randomWithRange(leftDigits[j] + 1 - borrow, 9);

                            hasCarryOrBorrowOnTheRight = true;
                            borrow = 1;
                        }
                    } else {
                        leftDigits[j] = randomWithRange(0, 9);
                        rightDigits[j] = randomWithRange(0, 9);

                        boolean carryOrBorrowOnThisDigit;
                        if (getScope().getOperator() == Operator.PLUS){
                            carryOrBorrowOnThisDigit = (leftDigits[j] + rightDigits[j] + carry >= 10);
                            carry = carryOrBorrowOnThisDigit ? 1 : 0;
                        } else {
                            carryOrBorrowOnThisDigit = (leftDigits[j] - borrow - rightDigits[j] < 0);
                            borrow = carryOrBorrowOnThisDigit ? 1 : 0;
                        }

                        hasCarryOrBorrowOnTheRight = hasCarryOrBorrowOnTheRight || carryOrBorrowOnThisDigit;
                    }
                } else {
                    leftDigits[j] = randomWithRange(0, 9);

                    if (getScope().getOperator() == Operator.PLUS) {
                        rightDigits[j] = randomWithRange(0, 10 - leftDigits[j] - 1);
                    } else {
                        rightDigits[j] = randomWithRange(0, leftDigits[j]);
                    }
                }
            }

            int leftOperand = composeOf(leftDigits);
            int rightOperand = composeOf(rightDigits);
            Question question = new Question(getScope().getOperator(), leftOperand, rightOperand);
            questionList.add(question);
        }

        return questionList;
    }
}

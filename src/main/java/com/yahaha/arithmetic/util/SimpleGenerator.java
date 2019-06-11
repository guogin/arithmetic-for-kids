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
            InternalGenerator generator = createInternalGenerator(getScope().getOperator());

            Question question = generator.generateQuestion(getScope());

            questionList.add(question);
        }

        return questionList;
    }

    private InternalGenerator createInternalGenerator(Operator operator) {
        if (operator == Operator.PLUS) {
            return new InternalGeneratorForAddition();
        } else {
            return new InternalGeneratorForSubtraction();
        }
    }

    private interface InternalGenerator {
        Question generateQuestion(SimpleScope scope) throws InvalidScopeException;
    }

    private abstract class AbstractInternalGenerator implements InternalGenerator {
        protected boolean flipCoin() {
            return randomWithRange(0, 1) == 0;
        }
    }

    private class InternalGeneratorForAddition extends AbstractInternalGenerator {
        @Override
        public Question generateQuestion(SimpleScope scope) throws InvalidScopeException {
            Integer[] leftDigits = new Integer[scope.getNumberOfDigits()];
            Integer[] rightDigits = new Integer[scope.getNumberOfDigits()];

            int carry = 0; // 0 or 1
            boolean hasCarryTheRight = false;

            for (int i = scope.getNumberOfDigits() - 1; i >= 0; --i) {
                if (scope.isCarryOrBorrowEnabled()) {
                    if ((i == 0 && !hasCarryTheRight) ||  // if this is the left most digit however we didn't generate any carry operations on the right
                            flipCoin()) {                 // or let's flip a coin, 0 = we should generate carry operation on this digit

                        leftDigits[i] = randomWithRange(1 - carry, 9); // if left digit is 0, then carry could not happen
                        rightDigits[i] = randomWithRange(10 - leftDigits[i] - carry, 9);

                        hasCarryTheRight = true;
                        carry = 1;
                    } else {
                        leftDigits[i] = randomWithRange(0, 9);
                        rightDigits[i] = randomWithRange(0, 9);

                        boolean carryOnThisDigit;

                        carryOnThisDigit = (leftDigits[i] + rightDigits[i] + carry >= 10);
                        carry = carryOnThisDigit ? 1 : 0;


                        hasCarryTheRight = hasCarryTheRight || carryOnThisDigit;
                    }
                } else {
                    leftDigits[i] = randomWithRange(0, 9);
                    rightDigits[i] = randomWithRange(0, 10 - leftDigits[i] - 1);
                }
            }

            int leftOperand = composeOf(leftDigits);
            int rightOperand = composeOf(rightDigits);
            Question question = new Question(getScope().getOperator(), leftOperand, rightOperand);

            return question;
        }
    }

    private class InternalGeneratorForSubtraction extends AbstractInternalGenerator {
        @Override
        public Question generateQuestion(SimpleScope scope) throws InvalidScopeException {
            if (scope.isCarryOrBorrowEnabled() && scope.getNumberOfDigits() < 2) {
                throw new InvalidScopeException("Borrow is not possible if number of digits is 1", "borrow.is.not.possible");
            }

            Integer[] leftDigits = new Integer[scope.getNumberOfDigits()];
            Integer[] rightDigits = new Integer[scope.getNumberOfDigits()];

            int borrow = 0; // 0 or 1
            boolean hasBorrowOnTheRight = false;

            for (int i = scope.getNumberOfDigits() - 1; i >= 0; --i) {
                if (scope.isCarryOrBorrowEnabled()) {
                    if (i == 0) { // The left number must be greater or equal than the right number
                        rightDigits[i] = randomWithRange(0, 9 - borrow);
                        leftDigits[i] = randomWithRange(rightDigits[i] + borrow, 9);
                    } else if ((i == 1 && !hasBorrowOnTheRight) || // if this is the 2nd left most digit however we didn't generate any borrow operations on the right
                            flipCoin()) {                          // or let's flip a coin, 0 = we should generate borrow operation on this digit

                        leftDigits[i] = randomWithRange(0, 9 - (1 - borrow)); // if left digit is 9, then borrow could not happen
                        rightDigits[i] = randomWithRange(leftDigits[i] + 1 - borrow, 9);

                        hasBorrowOnTheRight = true;
                        borrow = 1;
                    } else {
                        leftDigits[i] = randomWithRange(0, 9);
                        rightDigits[i] = randomWithRange(0, 9);

                        boolean borrowOnThisDigit;

                        borrowOnThisDigit = (leftDigits[i] - borrow - rightDigits[i] < 0);
                        borrow = borrowOnThisDigit ? 1 : 0;

                        hasBorrowOnTheRight = hasBorrowOnTheRight || borrowOnThisDigit;
                    }
                } else {
                    leftDigits[i] = randomWithRange(0, 9);
                    rightDigits[i] = randomWithRange(0, leftDigits[i]);
                }
            }

            int leftOperand = composeOf(leftDigits);
            int rightOperand = composeOf(rightDigits);
            Question question = new Question(getScope().getOperator(), leftOperand, rightOperand);

            return question;
        }
    }
}

package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.SimpleScope;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleGeneratorTests {
    @Test
    public void generateAdditionWithCarry() throws Exception {
        SimpleScope simpleScope = new SimpleScope(Operator.PLUS, 1000, 5, true);

        SimpleGenerator generator = new SimpleGenerator();
        generator.setScope(simpleScope);

        List<Question> questionList = generator.generateQuestions();

        for (Question question : questionList) {
            assertTrue(NumberUtil.hasCarryOrBorrowOperation(question.getLeftOperand(), question.getRightOperand(), question.getOperator()));
        }
    }

    @Test
    public void generateSubtractionWithBorrow() throws Exception {
        SimpleScope simpleScope = new SimpleScope(Operator.MINUS, 1000, 5, true);

        SimpleGenerator generator = new SimpleGenerator();
        generator.setScope(simpleScope);

        List<Question> questionList = generator.generateQuestions();

        for (Question question : questionList) {
            assertTrue(question.getLeftOperand() >= question.getRightOperand());
            assertTrue(NumberUtil.hasCarryOrBorrowOperation(question.getLeftOperand(), question.getRightOperand(), question.getOperator()));
        }
    }

    @Test
    public void generateAdditionWithoutCarry() throws Exception {
        SimpleScope simpleScope = new SimpleScope(Operator.PLUS, 1000, 5, false);

        SimpleGenerator generator = new SimpleGenerator();
        generator.setScope(simpleScope);

        List<Question> questionList = generator.generateQuestions();

        for (Question question : questionList) {
            assertFalse(NumberUtil.hasCarryOrBorrowOperation(question.getLeftOperand(), question.getRightOperand(), question.getOperator()));
        }
    }

    @Test
    public void generateSubtractionWithoutBorrow() throws Exception {
        SimpleScope simpleScope = new SimpleScope(Operator.MINUS, 1000, 5, false);

        SimpleGenerator generator = new SimpleGenerator();
        generator.setScope(simpleScope);

        List<Question> questionList = generator.generateQuestions();

        for (Question question : questionList) {
            assertTrue(question.getLeftOperand() >= question.getRightOperand());
            assertFalse(NumberUtil.hasCarryOrBorrowOperation(question.getLeftOperand(), question.getRightOperand(), question.getOperator()));
        }
    }
}

package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class GeneratorTests {
    @Test
    public void generateAdditionQuestions() {
        Scope scope = new Scope(Operator.PLUS, 1000, 2, 9, 2, 9, 11, 18);

        Generator generator = new Generator();
        generator.setScope(scope);

        List<Question> questionList = generator.generate();

        for (Question question : questionList) {
            assertTrue(question.getLeftOperand() >= scope.getMinLeftOperand());
            assertTrue(question.getLeftOperand() <= scope.getMaxLeftOperand());
            assertTrue(question.getRightOperand() >= scope.getMinRightOperand());
            assertTrue(question.getRightOperand() <= scope.getMaxRightOperand());
            assertTrue(question.getAnswer() >= scope.getMinAnswer());
            assertTrue(question.getAnswer() <= scope.getMaxAnswer());
        }
    }

    @Test
    public void generateSubtractionQuestions() {
        Scope scope = new Scope(Operator.MINUS, 1000, 11, 18, 2, 9, 2, 9);

        Generator generator = new Generator();
        generator.setScope(scope);

        List<Question> questionList = generator.generate();

        for (Question question : questionList) {
            assertTrue(question.getLeftOperand() >= scope.getMinLeftOperand());
            assertTrue(question.getLeftOperand() <= scope.getMaxLeftOperand());
            assertTrue(question.getRightOperand() >= scope.getMinRightOperand());
            assertTrue(question.getRightOperand() <= scope.getMaxRightOperand());
            assertTrue(question.getAnswer() >= scope.getMinAnswer());
            assertTrue(question.getAnswer() <= scope.getMaxAnswer());
        }
    }
}

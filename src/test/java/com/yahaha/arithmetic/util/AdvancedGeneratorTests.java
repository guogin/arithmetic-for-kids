package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class AdvancedGeneratorTests {
    @Test
    public void generateAdditionQuestions() throws Exception {
        Scope scope = new Scope(Operator.PLUS, 1000, 2, 9999, 2, 9999, 100, 9999);

        AdvancedGenerator advancedGenerator = new AdvancedGenerator();
        advancedGenerator.setScope(scope);

        List<Question> questionList = advancedGenerator.generateQuestions();

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
    public void generateSubtractionQuestions() throws Exception {
        Scope scope = new Scope(Operator.MINUS, 1000, 200, 9999, 100, 9999, 100, 999);

        AdvancedGenerator advancedGenerator = new AdvancedGenerator();
        advancedGenerator.setScope(scope);

        List<Question> questionList = advancedGenerator.generateQuestions();

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

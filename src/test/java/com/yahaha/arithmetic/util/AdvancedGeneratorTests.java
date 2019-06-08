package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class AdvancedGeneratorTests {
    @Test
    public void generateAdditionQuestions() throws Exception {
        AdvancedScope advancedScope = new AdvancedScope(Operator.PLUS, 1000, 11, 9999, 11, 9999, 300, 9999);

        AdvancedGenerator advancedGenerator = new AdvancedGenerator();
        advancedGenerator.setAdvancedScope(advancedScope);

        List<Question> questionList = advancedGenerator.generateQuestions();

        for (Question question : questionList) {
            assertTrue(question.getLeftOperand() >= advancedScope.getMinLeftOperand());
            assertTrue(question.getLeftOperand() <= advancedScope.getMaxLeftOperand());
            assertTrue(question.getRightOperand() >= advancedScope.getMinRightOperand());
            assertTrue(question.getRightOperand() <= advancedScope.getMaxRightOperand());
            assertTrue(question.getAnswer() >= advancedScope.getMinAnswer());
            assertTrue(question.getAnswer() <= advancedScope.getMaxAnswer());
        }
    }

    @Test
    public void generateSubtractionQuestions() throws Exception {
        AdvancedScope advancedScope = new AdvancedScope(Operator.MINUS, 1000, 200, 9999, 100, 9999, 100, 999);

        AdvancedGenerator advancedGenerator = new AdvancedGenerator();
        advancedGenerator.setAdvancedScope(advancedScope);

        List<Question> questionList = advancedGenerator.generateQuestions();

        for (Question question : questionList) {
            assertTrue(question.getLeftOperand() >= advancedScope.getMinLeftOperand());
            assertTrue(question.getLeftOperand() <= advancedScope.getMaxLeftOperand());
            assertTrue(question.getRightOperand() >= advancedScope.getMinRightOperand());
            assertTrue(question.getRightOperand() <= advancedScope.getMaxRightOperand());
            assertTrue(question.getAnswer() >= advancedScope.getMinAnswer());
            assertTrue(question.getAnswer() <= advancedScope.getMaxAnswer());
        }
    }
}

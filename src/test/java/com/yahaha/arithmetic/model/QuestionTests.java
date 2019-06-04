package com.yahaha.arithmetic.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuestionTests {
    @Before
    public void setup() {
    }

    @Test
    public void stringify() {
        Question theQuestion = new Question(Operator.PLUS, 12, 9);
        assertEquals("12 + 9 = ", theQuestion.getExpression());
    }

    @Test
    public void hasAnswer() {
        Question theQuestion = new Question(Operator.MINUS, 15, 8);
        assertEquals(7, theQuestion.getAnswer());
    }
}

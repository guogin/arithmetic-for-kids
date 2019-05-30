package com.yahaha.arithmetic.controller;

import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;
import com.yahaha.arithmetic.util.Generator;
import com.yahaha.arithmetic.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PaperController.class)
public class PaperControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private Generator generator;

    @Before
    public void setup() {
        Mockito.reset(generator);
    }

    @Test
    public void givenQuestionList_whenGenerateQuestions_returnJsonArray() throws Exception {
        List<Question> questionList = Arrays.asList(
                new Question(Operator.PLUS, 8, 9),
                new Question(Operator.MINUS, 16, 7),
                new Question(Operator.PLUS, 6, 6)
        );

        given(generator.generate()).willReturn(questionList);

        mvc.perform(get("/api/generateQuestions?operator=MINUS&numberOfQuestions=10&minLeftOp=11&maxLeftOp=18&minRightOp=2&maxRightOp=9&minAnswer=2&maxAnswer=9"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].leftOperand", equalTo(6)))
                .andExpect(jsonPath("$[2].answer", equalTo(12)));
    }

    @Test
    public void givenQuestionList_whenGenerateTestPaper_returnPaperObject() throws Exception {
        List<Question> questionList1 = Arrays.asList(
                new Question(Operator.PLUS, 8, 9),
                new Question(Operator.PLUS, 6, 6)
        );

        List<Question> questionList2 = Arrays.asList(
                new Question(Operator.MINUS, 16, 7),
                new Question(Operator.MINUS, 12, 5)
        );

        List<Scope> scopes = Arrays.asList(
                new Scope(Operator.PLUS, 2, 2, 9, 2, 9, 11, 18),
                new Scope(Operator.MINUS, 1, 11, 18, 2, 9, 2, 9)
        );

        given(generator.generate())
                .willReturn(questionList1)
                .willReturn(questionList2);

        mvc.perform(post("/api/generateTestPaper")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.asJsonString(scopes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionList", hasSize(4)))
                .andExpect(jsonPath("$.questionList[*].rightOperand", containsInAnyOrder(9, 6, 7, 5)))
                .andExpect(jsonPath("$.questionList[*].answer", containsInAnyOrder(17, 12, 9, 7)));

    }
}

package com.yahaha.arithmetic.controller;

import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.util.AdvancedGenerator;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PaperController.class)
public class PaperControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AdvancedGenerator advancedGenerator;

    @Before
    public void setup() {
        Mockito.reset(advancedGenerator);
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

        List<AdvancedScope> advancedScopes = Arrays.asList(
                new AdvancedScope(Operator.PLUS, 2, 2, 9, 2, 9, 11, 18),
                new AdvancedScope(Operator.MINUS, 1, 11, 18, 2, 9, 2, 9)
        );

        given(advancedGenerator.generateQuestions())
                .willReturn(questionList1)
                .willReturn(questionList2);

        mvc.perform(post("/api/generateTestPaper")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.asJsonString(advancedScopes)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionList", hasSize(4)))
                .andExpect(jsonPath("$.questionList[*].rightOperand", containsInAnyOrder(9, 6, 7, 5)))
                .andExpect(jsonPath("$.questionList[*].answer", containsInAnyOrder(17, 12, 9, 7)));

    }
}

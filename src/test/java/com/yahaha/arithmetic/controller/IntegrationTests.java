package com.yahaha.arithmetic.controller;

import com.yahaha.arithmetic.ArithmeticApplication;
import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK, //default
        classes = ArithmeticApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class IntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void whenInvalidParameterIsProvided_thenShouldResponseWithErrorMessage() throws Exception {
        List<AdvancedScope> advancedScopes = Arrays.asList(
                new AdvancedScope(Operator.PLUS, 2, 2, 9, 100, 1, 11, 18)
        );

        mvc.perform(post("/api/generateTestPaper")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.asJsonString(advancedScopes)))
                .andExpect(status().isBadRequest());
    }
}

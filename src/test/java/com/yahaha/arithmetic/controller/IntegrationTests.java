package com.yahaha.arithmetic.controller;

import com.yahaha.arithmetic.ArithmeticApplication;
import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.SimpleScope;
import com.yahaha.arithmetic.model.UserSetting;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    public void whenCalled_thenShouldResponseWithTestPaper() throws Exception {
        UserSetting userSetting = new UserSetting();

        userSetting.setAdvancedScopes(Arrays.asList(
                new AdvancedScope(Operator.PLUS, 2, 2, 9, 2, 9, 11, 18),
                new AdvancedScope(Operator.MINUS, 2, 11, 18, 2, 9, 2, 9)
        ));

        mvc.perform(post("/api/generateTestPaper")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.asJsonString(userSetting)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionList", hasSize(4)))
                .andExpect(jsonPath("$.questionList[*].expression", containsInAnyOrder(
                        stringContainsInOrder(Arrays.asList("+", "=")),
                        stringContainsInOrder(Arrays.asList("+", "=")),
                        stringContainsInOrder(Arrays.asList("-", "=")),
                        stringContainsInOrder(Arrays.asList("-", "="))
                )));
    }

    @Test
    public void whenInvalidParameterIsProvided_thenShouldResponseWithErrorMessage() throws Exception {
        UserSetting userSetting = new UserSetting();

        userSetting.setAdvancedScopes(Arrays.asList(
                new AdvancedScope(Operator.PLUS, 2, 2, 9, 100, 1, 11, 18)
        ));
        userSetting.setSimpleScopes(Arrays.asList(
                new SimpleScope(Operator.PLUS, 2, 3, true)
        ));

        mvc.perform(post("/api/generateTestPaper")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.asJsonString(userSetting)))
                .andExpect(status().isBadRequest());
    }
}

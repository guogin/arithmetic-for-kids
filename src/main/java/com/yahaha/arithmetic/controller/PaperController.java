package com.yahaha.arithmetic.controller;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.TestPaper;
import com.yahaha.arithmetic.util.AdvancedGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@RestController
public class PaperController {
    @Autowired
    private AdvancedGenerator advancedGenerator;

    @Autowired
    private MessageSource messageSource;

    @PostMapping(path = "/api/generateTestPaper", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TestPaper generateTestPaper(@RequestBody List<AdvancedScope> advancedScopes, Locale locale) {
        TestPaper testPaper = new TestPaper();

        Iterator<AdvancedScope> iterator = advancedScopes.iterator();
        int index = 1; // count from 1

        while (iterator.hasNext()) {
            AdvancedScope advancedScope = iterator.next();

            validateScope(advancedScope, locale);
            advancedGenerator.setAdvancedScope(advancedScope);

            try {
                testPaper.addQuestions(advancedGenerator.generateQuestions());
            } catch (InvalidScopeException ex) {
                String errorMsg = messageSource.getMessage("invalid.scope.detail", new Object[]{ index }, locale);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
            }

            index++;
        }

        testPaper.shuffle();

        return testPaper;
    }

    private void validateScope(AdvancedScope advancedScope, Locale locale) {
        boolean isValid = true;
        String errorMsg = null;

        if (advancedScope.getOperator() == null) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.operator.message", null, locale);
        } else if (advancedScope.getNumberOfQuestions() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("negative.number.of.questions.message", new Object[]{ advancedScope.getNumberOfQuestions() }, locale);
        } else if (advancedScope.getNumberOfQuestions() > 1000) {
            isValid = false;
            errorMsg = messageSource.getMessage("too.many.questions", new Object[]{ advancedScope.getNumberOfQuestions() }, locale);
        } else if (advancedScope.getMinLeftOperand() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMinLeftOperand() }, locale);
        } else if (advancedScope.getMaxLeftOperand() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMaxLeftOperand() }, locale);
        } else if (advancedScope.getMinLeftOperand() > advancedScope.getMaxLeftOperand()) {
            isValid = false;
            errorMsg = messageSource.getMessage("min.is.greater.than.max", new Object[]{ advancedScope.getMinLeftOperand(), advancedScope.getMaxLeftOperand() }, locale);
        } else if (advancedScope.getMinRightOperand() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMinRightOperand() }, locale);
        } else if (advancedScope.getMaxRightOperand() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMaxRightOperand() }, locale);
        } else if (advancedScope.getMinRightOperand() > advancedScope.getMaxRightOperand()) {
            isValid = false;
            errorMsg = messageSource.getMessage("min.is.greater.than.max", new Object[]{ advancedScope.getMinRightOperand(), advancedScope.getMaxRightOperand() }, locale);
        } else if (advancedScope.getMinAnswer() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMinAnswer() }, locale);
        } else if (advancedScope.getMaxAnswer() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMaxAnswer() }, locale);
        } else if (advancedScope.getMinAnswer() > advancedScope.getMaxAnswer()) {
            isValid = false;
            errorMsg = messageSource.getMessage("min.is.greater.than.max", new Object[]{ advancedScope.getMinAnswer(), advancedScope.getMaxAnswer() }, locale);
        }

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}

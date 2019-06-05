package com.yahaha.arithmetic.controller;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;
import com.yahaha.arithmetic.model.TestPaper;
import com.yahaha.arithmetic.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@RestController
public class PaperController {
    @Autowired
    private Generator generator;

    @Autowired
    private MessageSource messageSource;

    @PostMapping(path = "/api/generateTestPaper", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TestPaper generateTestPaper(@RequestBody List<Scope> scopes, Locale locale) {
        TestPaper testPaper = new TestPaper();

        Iterator<Scope> iterator = scopes.iterator();
        int index = 1; // count from 1

        while (iterator.hasNext()) {
            Scope scope = iterator.next();

            validateScope(scope, locale);
            generator.setScope(scope);

            try {
                testPaper.addQuestions(generator.generate());
            } catch (InvalidScopeException ex) {
                String errorMsg = messageSource.getMessage("invalid.scope.detail", new Object[]{ index }, locale);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
            }

            index++;
        }

        testPaper.shuffle();

        return testPaper;
    }

    private void validateScope(Scope scope, Locale locale) {
        boolean isValid = true;
        String errorMsg = null;

        if (scope.getOperator() == null) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.operator.message", null, locale);
        } else if (scope.getNumberOfQuestions() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("negative.number.of.questions.message", new Object[]{ scope.getNumberOfQuestions() }, locale);
        } else if (scope.getNumberOfQuestions() > 1000) {
            isValid = false;
            errorMsg = messageSource.getMessage("too.many.questions", new Object[]{ scope.getNumberOfQuestions() }, locale);
        } else if (scope.getMinLeftOperand() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ scope.getMinLeftOperand() }, locale);
        } else if (scope.getMaxLeftOperand() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ scope.getMaxLeftOperand() }, locale);
        } else if (scope.getMinLeftOperand() > scope.getMaxLeftOperand()) {
            isValid = false;
            errorMsg = messageSource.getMessage("min.is.greater.than.max", new Object[]{ scope.getMinLeftOperand(), scope.getMaxLeftOperand() }, locale);
        } else if (scope.getMinRightOperand() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ scope.getMinRightOperand() }, locale);
        } else if (scope.getMaxRightOperand() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ scope.getMaxRightOperand() }, locale);
        } else if (scope.getMinRightOperand() > scope.getMaxRightOperand()) {
            isValid = false;
            errorMsg = messageSource.getMessage("min.is.greater.than.max", new Object[]{ scope.getMinRightOperand(), scope.getMaxRightOperand() }, locale);
        } else if (scope.getMinAnswer() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ scope.getMinAnswer() }, locale);
        } else if (scope.getMaxAnswer() < 0) {
            isValid = false;
            errorMsg = messageSource.getMessage("invalid.parameter", new Object[]{ scope.getMaxAnswer() }, locale);
        } else if (scope.getMinAnswer() > scope.getMaxAnswer()) {
            isValid = false;
            errorMsg = messageSource.getMessage("min.is.greater.than.max", new Object[]{ scope.getMinAnswer(), scope.getMaxAnswer() }, locale);
        }

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}

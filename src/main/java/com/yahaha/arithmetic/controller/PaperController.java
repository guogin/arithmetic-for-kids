package com.yahaha.arithmetic.controller;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.SimpleScope;
import com.yahaha.arithmetic.model.TestPaper;
import com.yahaha.arithmetic.model.UserSetting;
import com.yahaha.arithmetic.util.AdvancedGenerator;
import com.yahaha.arithmetic.util.Generator;
import com.yahaha.arithmetic.util.GeneratorFactory;
import com.yahaha.arithmetic.util.SimpleGenerator;
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
    private GeneratorFactory generatorFactory;

    @Autowired
    private MessageSource messageSource;

    @PostMapping(path = "/api/generateTestPaper", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TestPaper generateTestPaper(@RequestBody UserSetting userSetting, Locale locale) {
        TestPaper testPaper = new TestPaper();

        if (userSetting.getAdvancedScopes() != null) {
            for (AdvancedScope scope : userSetting.getAdvancedScopes()) {
                validateScope(scope, locale);

                Generator generator = generatorFactory.createGenerator(scope);

                try {
                    testPaper.addQuestions(generator.generateQuestions());
                } catch (InvalidScopeException ex) {
                    String errorMsg = messageSource.getMessage("invalid.scope.detail", new Object[]{ ex.getMessage() }, locale);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
                }

            }
        }

        if (userSetting.getSimpleScopes() != null) {
            for (SimpleScope scope : userSetting.getSimpleScopes()) {
                validateScope(scope, locale);

                Generator generator = generatorFactory.createGenerator(scope);

                try {
                    testPaper.addQuestions(generator.generateQuestions());
                } catch (InvalidScopeException ex) {
                    String errorMsg = messageSource.getMessage("invalid.scope.detail", new Object[]{ ex.getMessage() }, locale);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
                }
            }
        }

        testPaper.shuffle();

        return testPaper;
    }

    private void validateScope(AdvancedScope advancedScope, Locale locale) {
        boolean isValid = true;
        String message = null;

        if (advancedScope.getOperator() == null) {
            isValid = false;
            message = messageSource.getMessage("invalid.operator.message", null, locale);
        } else if (advancedScope.getNumberOfQuestions() < 0) {
            isValid = false;
            message = messageSource.getMessage("negative.number.of.questions.message", new Object[]{ advancedScope.getNumberOfQuestions() }, locale);
        } else if (advancedScope.getNumberOfQuestions() > 1000) {
            isValid = false;
            message = messageSource.getMessage("too.many.questions", new Object[]{ advancedScope.getNumberOfQuestions() }, locale);
        } else if (advancedScope.getMinLeftOperand() < 0) {
            isValid = false;
            message = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMinLeftOperand() }, locale);
        } else if (advancedScope.getMaxLeftOperand() < 0) {
            isValid = false;
            message = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMaxLeftOperand() }, locale);
        } else if (advancedScope.getMinLeftOperand() > advancedScope.getMaxLeftOperand()) {
            isValid = false;
            message = messageSource.getMessage("min.is.greater.than.max", new Object[]{ advancedScope.getMinLeftOperand(), advancedScope.getMaxLeftOperand() }, locale);
        } else if (advancedScope.getMinRightOperand() < 0) {
            isValid = false;
            message = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMinRightOperand() }, locale);
        } else if (advancedScope.getMaxRightOperand() < 0) {
            isValid = false;
            message = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMaxRightOperand() }, locale);
        } else if (advancedScope.getMinRightOperand() > advancedScope.getMaxRightOperand()) {
            isValid = false;
            message = messageSource.getMessage("min.is.greater.than.max", new Object[]{ advancedScope.getMinRightOperand(), advancedScope.getMaxRightOperand() }, locale);
        } else if (advancedScope.getMinAnswer() < 0) {
            isValid = false;
            message = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMinAnswer() }, locale);
        } else if (advancedScope.getMaxAnswer() < 0) {
            isValid = false;
            message = messageSource.getMessage("invalid.parameter", new Object[]{ advancedScope.getMaxAnswer() }, locale);
        } else if (advancedScope.getMinAnswer() > advancedScope.getMaxAnswer()) {
            isValid = false;
            message = messageSource.getMessage("min.is.greater.than.max", new Object[]{ advancedScope.getMinAnswer(), advancedScope.getMaxAnswer() }, locale);
        }

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private void validateScope(SimpleScope simpleScope, Locale locale) {
        boolean isValid = true;
        String message = null;

        if (simpleScope.getOperator() == null) {
            isValid = false;
            message = messageSource.getMessage("invalid.operator.message", null, locale);
        } else if (simpleScope.getNumberOfQuestions() < 0) {
            isValid = false;
            message = messageSource.getMessage("negative.number.of.questions.message", new Object[]{ simpleScope.getNumberOfQuestions() }, locale);
        } else if (simpleScope.getNumberOfQuestions() > 1000) {
            isValid = false;
            message = messageSource.getMessage("too.many.questions", new Object[]{ simpleScope.getNumberOfQuestions() }, locale);
        } else if (simpleScope.getNumberOfDigits() < 0 || simpleScope.getNumberOfDigits() > 8) {
            isValid = false;
            message = messageSource.getMessage("invalid.number.of.digits", new Object[]{ simpleScope.getNumberOfDigits() }, locale);
        }

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }
}

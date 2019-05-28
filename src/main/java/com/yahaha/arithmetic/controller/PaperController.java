package com.yahaha.arithmetic.controller;

import com.yahaha.arithmetic.model.Operator;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;
import com.yahaha.arithmetic.model.TestPaper;
import com.yahaha.arithmetic.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaperController {
    @Autowired
    private Generator generator;

    @GetMapping(path = "/api/generateQuestions")
    public List<Question> generateQuestions(@RequestParam Operator operator, @RequestParam int numberOfQuestions,
                                            @RequestParam int minLeftOp, @RequestParam int maxLeftOp,
                                            @RequestParam int minRightOp, @RequestParam int maxRightOp,
                                            @RequestParam int minAnswer, @RequestParam int maxAnswer) {
        generator.setScope(new Scope(
                operator, numberOfQuestions, minLeftOp, maxLeftOp, minRightOp, maxRightOp, minAnswer, maxAnswer
        ));
        return generator.generate();
    }

    @PostMapping(path = "/api/generateTestPaper", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TestPaper generateTestPaper(@RequestBody List<Scope> scopes) {
        TestPaper testPaper = new TestPaper();

        for (Scope scope : scopes) {
            generator.setScope(scope);
            testPaper.addQuestions(generator.generate());
        }
        testPaper.shuffle();

        return testPaper;
    }
}

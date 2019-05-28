package com.yahaha.arithmetic.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
public class TestPaper {
    private List<Question> questionList = new ArrayList<>();

    public void addQuestions(List<Question> questions) {
        questionList.addAll(questions);
    }

    public void shuffle() {
        Collections.shuffle(questionList);
    }
 }

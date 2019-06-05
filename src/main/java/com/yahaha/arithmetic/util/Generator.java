package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.Question;

import java.util.List;

public interface Generator {
    List<Question> generateQuestions() throws InvalidScopeException;
}

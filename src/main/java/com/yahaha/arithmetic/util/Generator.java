package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.Question;
import com.yahaha.arithmetic.model.Scope;

import java.util.List;

public interface Generator {
    void setScope(Scope scope);
    Scope getScope();
    List<Question> generateQuestions() throws InvalidScopeException;
}

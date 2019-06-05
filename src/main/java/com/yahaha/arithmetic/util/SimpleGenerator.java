package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.error.InvalidScopeException;
import com.yahaha.arithmetic.model.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleGenerator implements Generator {
    @Override
    public List<Question> generateQuestions() throws InvalidScopeException {
        return null;
    }
}

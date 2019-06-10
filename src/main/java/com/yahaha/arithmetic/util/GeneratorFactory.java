package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.model.AdvancedScope;
import com.yahaha.arithmetic.model.SimpleScope;
import org.springframework.stereotype.Service;

@Service
public class GeneratorFactory {
    private AdvancedGenerator advancedGenerator = new AdvancedGenerator();
    private SimpleGenerator simpleGenerator = new SimpleGenerator();

    public Generator createGenerator(AdvancedScope advancedScope) {
        advancedGenerator.setScope(advancedScope);
        return advancedGenerator;
    }

    public Generator createGenerator(SimpleScope simpleScope) {
        simpleGenerator.setScope(simpleScope);
        return simpleGenerator;
    }
}

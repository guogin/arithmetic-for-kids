package com.yahaha.arithmetic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scope {
    @NotNull
    private Operator operator;

    @Min(1)
    @Max(1000)
    private int numberOfQuestions;
}

package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.model.Operator;

import java.util.Deque;
import java.util.LinkedList;

import static java.lang.Math.max;
import static java.lang.Math.pow;

public class NumberUtil {
    public static Integer[] digitsOf(int number) {
        Deque<Integer> digits = new LinkedList<>();

        while (number > 0) {
            int digit = number % 10;

            digits.addFirst(digit);

            number = number / 10;
        }

        return digits.toArray(new Integer[0]);
    }

    public static int composeOf(Integer[] digits) {
        int value = 0;
        for (int i = digits.length - 1, j = 0; i >= 0; --i, ++j) {
            value += digits[i] * (int)pow(10, j);
        }
        return value;
    }

    public static Integer[] leftPadding(Integer[] digits, int length) {
        int diff = length - digits.length;
        if (diff <= 0) {
            return digits;
        }

        Integer[] result = new Integer[length];
        for (int i = 0; i < diff; ++i) {
            result[i] = 0; // padding
        }

        for (int i = 0; i < digits.length; ++i) {
            result[i+diff] = digits[i];
        }

        return result;
    }

    public static boolean hasCarryOrBorrowOperation(int leftOperand, int rightOperand, Operator operator) {
        Integer[] leftDigits = digitsOf(leftOperand);
        Integer[] rightDigits = digitsOf(rightOperand);

        int numberOfDigits = max(leftDigits.length, rightDigits.length);

        leftDigits = leftPadding(leftDigits, numberOfDigits);
        rightDigits = leftPadding(rightDigits, numberOfDigits);

        boolean hasCarryOrBorrow = false;

        for (int i = numberOfDigits - 1; i >= 0; --i) {
            int left = leftDigits[i], right = rightDigits[i];
            if ((operator == Operator.PLUS && left + right >= 10) ||
                    (operator == Operator.MINUS && left < right)) {
                hasCarryOrBorrow = true;
                break;
            }
        }

        return hasCarryOrBorrow;
    }
}

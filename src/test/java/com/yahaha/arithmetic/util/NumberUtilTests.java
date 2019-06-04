package com.yahaha.arithmetic.util;

import com.yahaha.arithmetic.model.Operator;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NumberUtilTests {
    @Test
    public void digitsOf() {
        Integer[] digits = NumberUtil.digitsOf(12345);
        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, digits);
    }

    @Test
    public void composeOf() {
        Integer[] digits = new Integer[] {0, 1, 2, 3, 4, 5};
        int number = NumberUtil.composeOf(digits);

        assertEquals(12345, number);
    }

    @Test
    public void leftPadding() {
        Integer[] digits = new Integer[] {1, 2, 3};

        digits = NumberUtil.leftPadding(digits, 5);

        assertArrayEquals(new Integer[] {0, 0, 1, 2, 3}, digits);
    }

    @Test
    public void leftPaddingShouldNotTruncateArray() {
        Integer[] digits = new Integer[] {1, 2, 3, 4, 5};

        digits = NumberUtil.leftPadding(digits, 3);

        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, digits);
    }

    @Test
    public void hasCarryOrBorrow() {
        assertEquals(true, NumberUtil.hasCarryOrBorrowOperation(139, 256, Operator.PLUS));
        assertEquals(false, NumberUtil.hasCarryOrBorrowOperation(234, 155, Operator.PLUS));
        assertEquals(true, NumberUtil.hasCarryOrBorrowOperation(99, 88, Operator.PLUS));

        assertEquals(true, NumberUtil.hasCarryOrBorrowOperation(246, 164, Operator.MINUS));
        assertEquals(false, NumberUtil.hasCarryOrBorrowOperation(375, 173, Operator.MINUS));
        assertEquals(true, NumberUtil.hasCarryOrBorrowOperation(100, 3, Operator.MINUS));
    }
}

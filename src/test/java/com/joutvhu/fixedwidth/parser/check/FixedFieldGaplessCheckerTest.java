package com.joutvhu.fixedwidth.parser.check;

import com.joutvhu.fixedwidth.parser.annotation.FixedField;
import com.joutvhu.fixedwidth.parser.annotation.FixedObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedFieldGaplessCheckerTest {

    private FixedFieldGaplessChecker checker;

    @BeforeEach
    void setup() {
        checker = new FixedFieldGaplessChecker();
    }

    @Test
    void hasGap() {
        List<Checker.Entry> entries = checker.inspect(Gap.class);

        assertEquals(2, entries.size());
    }

    @FixedObject
    public static class Gap {
        @FixedField(start = 2, length = 5)
        private String name;

        @FixedField(length = 10, start = 10)
        private int age;
    }
}

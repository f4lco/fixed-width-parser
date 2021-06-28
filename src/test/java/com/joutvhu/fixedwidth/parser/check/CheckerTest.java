package com.joutvhu.fixedwidth.parser.check;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckerTest {

    @Test
    void canDoit() {
        Checker.Result result = new Checker().check(FixedFieldGaplessCheckerTest.Gap.class);

        assertNotNull(result);
        assertEquals(2, result.getEntries().size());
    }
}

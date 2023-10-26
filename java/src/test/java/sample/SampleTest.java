package sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SampleTest {

    @Test
    void firstTest() {
        // globals to something -> quick way to set from template
        Sample.theFunctionToTest();
        // assert globals to sth, quick way to read and compare
        assertEquals(2, 1 + 1);
    }

}

package com.example.bestqr;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ExampleTest {

    @BeforeEach
    static void intiAll(){

    }

    @BeforeEach
    void init(){

    }

    @Test
    void succeedingTest(){

    }

    @Test
    void failingTest() {
        fail("失敗するテスト");
    }

    @Test
    @Disabled("demoo")
    void skippedTest() {
        //
    }

    @AfterEach
    void tearDown(){

    }

    @AfterAll
    static void tearDownAll(){

    }
}

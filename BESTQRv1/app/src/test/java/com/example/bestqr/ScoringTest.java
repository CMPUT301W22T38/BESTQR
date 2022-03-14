package com.example.bestqr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ScoringTest {

    @Test
    public void calculateHashTest() {
        String hex = QR_CODE.calculateHash("BFG5DGW54");
        assertEquals("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", hex);
        //add more test here
    }

    @Test
    public void calculateScoreTest(){
        int score = QR_CODE.calculateScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals(111, score);
        //add more test for scoring here
    }

}

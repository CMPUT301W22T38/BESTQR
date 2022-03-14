package com.example.bestqr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the calculation of the score.(content->hash, hash->score)
 */
public class ScoringTest {
    /**
     * test content->hash
     */
    @Test
    public void calculateHashTest() {
        String hex = QRCODE.calculateHash("BFG5DGW54");
        assertEquals("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", hex);
        //add more test here
    }

    /**
     * Test hash -> score
     */
    @Test
    public void calculateScoreTest(){
        int score = QRCODE.calculateScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals(111, score);
        //add more test for scoring here
    }
}

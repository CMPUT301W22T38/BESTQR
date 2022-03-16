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
        String hash= QRCODE.calculateHash("BFG5DGW54");
        assertEquals("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", hash);
        hash = QRCODE.calculateHash("bfg5dgw54");//same contents but lower cases
        assertEquals("373b65a3205259aa4d0417298fc1a09051ab2543b8ddba8a277957b7c63e64fb", hash);
        hash = QRCODE.calculateHash("ABCDEFGH"); //Alphabet only
        assertEquals("ca8c128e9d9e4a28ef24d0508aa20b5cf880604eacd8f65c0e366f7e0cc5fbcf", hash);
        hash = QRCODE.calculateHash("123456789"); //Number only
        assertEquals("6d78392a5886177fe5b86e585a0b695a2bcd01a05504b3c4e38bc8eeb21e8326", hash);

        //add more test here

    }

    /**
     * Test hash -> score
     */
    @Test
    public void calculateScoreTest(){
        long score = QRCODE.calculateScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals(111, score);
//        score = QRCODE.calculateScore("00000000000000000000000000000000000"); // test the big score
//        assertEquals(2.0e35, score);
//        score = QRCODE.calculateScore("0123456789abcdefghijklmnopqrstuvwxyz"); // test no repeated digit
//        assertEquals(35, score);
        //add more test for scoring here
    }
}

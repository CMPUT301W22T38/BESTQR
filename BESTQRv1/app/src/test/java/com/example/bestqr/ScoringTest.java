package com.example.bestqr;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.bestqr.utils.QRmethods;

/**
 * This class tests the calculation of the score.(content->hash, hash->score)
 */
public class ScoringTest {
    /**
     * QRmethods class calculateHash()
     * test content->hash
     */
    @Test
    @DisplayName("content -> hash")
    public void calculateHashTest() {
        String hash = QRmethods.calculateHash("BFG5DGW54");
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", hash);
        hash = QRmethods.calculateHash("bfg5dgw54");//same contents but lower cases
        assertEquals("c7637a1bf1c2db1930d57863fbebaa2a1be3e019d3f127955fdbdb4282d5a3ac", hash);
        hash = QRmethods.calculateHash("ABCDEFGH"); //Alphabet only
        assertEquals("9ac2197d9258257b1ae8463e4214e4cd0a578bc1517f2415928b91be4283fc48", hash);
        hash = QRmethods.calculateHash("123456789"); //Number only
        assertEquals("15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225", hash);

        //add more test here
    }

    /**
     * QRmethods class calculateScore()
     * Test hash -> score
     *
     */
    @Test
    @DisplayName("hash -> score")
    public void calculateScoreTest(){
        long score = QRmethods.calculateScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals(111, score);
        score = QRmethods.calculateScore("0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqr"); // test no repeated digit
        assertEquals(0, score);


    }
}

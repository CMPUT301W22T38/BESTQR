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
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", hash);
        hash = QRCODE.calculateHash("bfg5dgw54");//same contents but lower cases
        assertEquals("c7637a1bf1c2db1930d57863fbebaa2a1be3e019d3f127955fdbdb4282d5a3ac", hash);
        hash = QRCODE.calculateHash("ABCDEFGH"); //Alphabet only
        assertEquals("9ac2197d9258257b1ae8463e4214e4cd0a578bc1517f2415928b91be4283fc48", hash);
        hash = QRCODE.calculateHash("123456789"); //Number only
        assertEquals("15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225", hash);

        //add more test here
    }

//    @Test
//    public void byteToHexTest(){
//        byte[] hash = {'8','2','2','7','a','d','0','3','6','b','5','0','4','e','3','9','f','e', '2',
//                '9','3','9','3','c','e','1','7','0','9','0','8','b','e','2','b','1','e','a','6','3',
//                '6','5','5','4','4','8','8','f','a','8','6','d','e','5','d','9','d','6','c','d','2','c','3','2'};
//        String hexString = CameraActivity.bytesToHex(hash);
//        assertEquals("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6", hexString);
//    }


    /**
     * Test hash -> score
     */
    @Test
    public void calculateScoreTest(){
        long score = QRCODE.calculateScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals(111, score);
//        score = QRCODE.calculateScore("0000000000000000000000000000000000000000000000000000000000000000"); // test the largest score
//        assertEquals(2.0e64, score);
//        score = QRCODE.calculateScore("0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqr"); // test no repeated digit
//        assertEquals(64, score);
        //add more test for scoring here

    }
}

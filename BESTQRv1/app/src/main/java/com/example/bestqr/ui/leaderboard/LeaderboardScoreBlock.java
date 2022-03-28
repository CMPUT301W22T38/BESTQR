package com.example.bestqr.ui.leaderboard;

public class LeaderboardScoreBlock {
    private String androidID;
    private String username;
    private int highestScoring;
    private int totalNum;
    private int totalSumOfScores;
    private int rank;


    public LeaderboardScoreBlock(String androidID, String username, int highestScoring, int totalNum, int totalSumOfScores){
        this.username = username;
        this.androidID = androidID;
        this.highestScoring = highestScoring;
        this.totalNum = totalNum;
        this.totalSumOfScores = totalSumOfScores;
    }

//
//    public static Comparator<LeaderboardScoreBlock> highestScoringComparator = new Comparator<LeaderboardScoreBlock>() {
//
//        public int compare(LeaderboardScoreBlock s1, LeaderboardScoreBlock s2){
//            int s1_score = s1.getHighestScoring();
//            int s2_score = s2.getHighestScoring();
//
//            return s2_score - s1_score;
//        }
//    };
//
//    public static Comparator<LeaderboardScoreBlock> totalNumComparator = new Comparator<LeaderboardScoreBlock>() {
//        int rank = 0;
//        public int compare(LeaderboardScoreBlock s1, LeaderboardScoreBlock s2){
//            int s1_score = s1.getTotalNum();
//            int s2_score = s2.getTotalNum();
//
//            return s2_score - s1_score;
//        }
//    };
//
//    public static Comparator<LeaderboardScoreBlock> totalSumComparator = new Comparator<LeaderboardScoreBlock>() {
//
//        public int compare(LeaderboardScoreBlock s1, LeaderboardScoreBlock s2){
//            int s1_score = s1.getTotalSumOfScores();
//            int s2_score = s2.getTotalSumOfScores();
//            return s2_score - s1_score;
//        }
//    };

    public String getAndroidID() {
        return androidID;
    }

    public void setAndroidID(String user_hash) {
        this.androidID = user_hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHighestScoring() {
        return highestScoring;
    }

    public void setHighestScoring(int highestScoring) {
        this.highestScoring = highestScoring;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalSumOfScores() {
        return totalSumOfScores;
    }

    public void setTotalSumOfScores(int totalSumOfScores) {
        this.totalSumOfScores = totalSumOfScores;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

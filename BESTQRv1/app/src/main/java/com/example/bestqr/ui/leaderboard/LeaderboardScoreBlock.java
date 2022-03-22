package com.example.bestqr.ui.leaderboard;

import java.util.Comparator;

public class LeaderboardScoreBlock {
    private String userHash;
    private String username;
    private int highestScoring;
    private int totalNum;
    private int totalSumOfScores;

    public LeaderboardScoreBlock(String username, int totalSumOfScores, int totalNum, int rank) {

    }

    public LeaderboardScoreBlock(String userHash, String username, int highestScoring, int totalNum, int totalSumOfScores){
        this.username = username;
        this.userHash = userHash;
        this.highestScoring = highestScoring;
        this.totalNum = totalNum;
        this.totalSumOfScores = totalSumOfScores;
    }

    public static Comparator<LeaderboardScoreBlock> highestScoringComparator = new Comparator<LeaderboardScoreBlock>() {

        public int compare(LeaderboardScoreBlock s1, LeaderboardScoreBlock s2){
            int s1_score = s1.getHighestScoring();
            int s2_score = s2.getHighestScoring();
            return s2_score - s1_score;
        }
    };

    public static Comparator<LeaderboardScoreBlock> totalNumComparator = new Comparator<LeaderboardScoreBlock>() {

        public int compare(LeaderboardScoreBlock s1, LeaderboardScoreBlock s2){
            int s1_score = s1.getTotalNum();
            int s2_score = s2.getTotalNum();
            return s2_score - s1_score;
        }
    };

    public static Comparator<LeaderboardScoreBlock> totalSumComparator = new Comparator<LeaderboardScoreBlock>() {

        public int compare(LeaderboardScoreBlock s1, LeaderboardScoreBlock s2){
            int s1_score = s1.getTotalSumOfScores();
            int s2_score = s2.getTotalSumOfScores();
            return s2_score - s1_score;
        }
    };

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String user_hash) {
        this.userHash = user_hash;
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
}

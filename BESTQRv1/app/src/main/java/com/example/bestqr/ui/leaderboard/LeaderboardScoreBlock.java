package com.example.bestqr.ui.leaderboard;

import com.example.bestqr.models.Profile;

public class LeaderboardScoreBlock {
    private int highestScoring;
    private int totalNum;
    private int totalSumOfScores;
    private int rank;
    private String username;
    private String androidId;

//    private Profile profile;

//    public LeaderboardScoreBlock(Profile profile, int highestScoring, int totalNum, int totalSumOfScores){
//        this.profile = profile;
//        this.highestScoring = highestScoring;
//        this.totalNum = totalNum;
//        this.totalSumOfScores = totalSumOfScores;
//    }

    public LeaderboardScoreBlock(String androidid, String username, int totalNum, int totalsum, int highestScoring) {
        this.androidId = androidid;
        this.username = username;
        this.totalNum = totalNum;
        this.totalSumOfScores = totalsum;
        this.highestScoring = highestScoring;
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

    public String getUsername() {
        return username;
    }

//
//    public Profile getProfile() {
//        return profile;
//    }

//    public void setProfile(Profile profile) {
//        this.profile = profile;
//    }
    public String getAndroidId() {
        return androidId;
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

package com.example.bestqr.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestqr.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private ArrayList<LeaderboardScoreBlock> scoreBlocks;
    private int sortingMethod;
    public boolean scoresInitialized;

    public LeaderboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leaderboard fragment");
        this.scoreBlocks = new ArrayList<LeaderboardScoreBlock>();
        this.scoresInitialized = false;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public ArrayList<Integer> getUserScoreAndRank(String userHash){
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i = 0; i < this.scoreBlocks.size(); i++){
            LeaderboardScoreBlock s = this.scoreBlocks.get(i);
            if(s.getProfile().getAndroidId() == userHash){
                result.add(getScoreCurrentSorted(i));
                result.add(i);
            }
        }
        return result;
    }

    public int getSortingMethod() {
        return sortingMethod;
    }

    /**
     * This method will update the stored lists of usernames, scores, and ranks.
     * The leaderboard will need rankings for:
     * highest scoring unique qr code
     * total number of qr codes scanned
     * total sum of scores of qr codes scanned
     */
    public void updateScoreBlocks(Database db){
        this.scoreBlocks = db.get_all_scoring_types();
        this.scoresInitialized = true;
    }

    /**
     * This method sorts the list of LeaderboardScoreBlock objects,
     * according to the total sum of qr code scores for a user
     */
    public void sortScoresByTotalSum(){
//        Collections.sort(this.scoreBlocks, LeaderboardScoreBlock.totalSumComparator);
        Collections.sort(this.scoreBlocks, Comparator.comparing(LeaderboardScoreBlock::getTotalSumOfScores).reversed());
        this.sortingMethod = 0;
    }

    /**
     * This method sorts the list of LeaderboardScoreBlock objects,
     * according to the highest scoring qr code a user has scanned
     */
    public void sortScoresByHighestUnique(){
//        Collections.sort(this.scoreBlocks, LeaderboardScoreBlock.highestScoringComparator);
        Collections.sort(this.scoreBlocks, Comparator.comparing(LeaderboardScoreBlock::getHighestScoring).reversed());
        this.sortingMethod = 1;
    }

    /**
     * This method sorts the list of LeaderboardScoreBlock objects,
     * sorted to the total number of qr codes scanned by a user.
     */
    public void sortScoresByTotalScanned(){

//        Collections.sort(this.scoreBlocks, LeaderboardScoreBlock.totalNumComparator);
        Collections.sort(this.scoreBlocks, Comparator.comparing(LeaderboardScoreBlock::getTotalNum).reversed());
        this.sortingMethod = 2;
    }


    /**
     * This method returns the score value for an item, with the score corresponding to the current sorting method
     * @param position : the position of the item
     * @return the integer score value
     */
    public int getScoreCurrentSorted(int position){
        switch(this.sortingMethod) {
            case 1:
                return this.scoreBlocks.get(position).getHighestScoring();
            case 2:
                return this.scoreBlocks.get(position).getTotalNum();
            case 0:
                return this.scoreBlocks.get(position).getTotalSumOfScores();
        }
        return 0;
    }

    /**
     * This method returns a score block for a given position.
     * @param position
     * @return : LeaderboardScoreBlock (position is according to current sorting method)
     */
    public LeaderboardScoreBlock getBlock(int position){
        return scoreBlocks.get(position);
    }

    /**
     * getter for the LeaderboardScoreBlocks
     * Ensure to use one of the sortScores methods before using.
     * @return : list of LeaderboardScoreBlock objects, sorted according to whatever
     *           sort method was used last.
     */
    public ArrayList<LeaderboardScoreBlock> getScoreBlocks(){
        return this.scoreBlocks;
    }

}
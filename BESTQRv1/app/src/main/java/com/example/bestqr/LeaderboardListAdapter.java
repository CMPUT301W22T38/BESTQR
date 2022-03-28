package com.example.bestqr;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bestqr.ui.leaderboard.LeaderboardScoreBlock;
import com.example.bestqr.ui.leaderboard.LeaderboardViewModel;

import java.util.ArrayList;

public class LeaderboardListAdapter extends ArrayAdapter<LeaderboardScoreBlock> {
    private ArrayList<LeaderboardScoreBlock> scores = new ArrayList<LeaderboardScoreBlock>();
    private Context context;
    int sortingMethod;

    public LeaderboardListAdapter(@NonNull Context context, ArrayList<LeaderboardScoreBlock> scores, int sortingMethod) {
        super(context, R.layout.leaderboardlist_item);
        this.context = context;
        this.scores = scores;
        this.sortingMethod = sortingMethod;
    }
    @Override
    public int getCount(){
        return scores.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(this.context).inflate(R.layout.leaderboardlist_item, parent, false);
        }

        LeaderboardScoreBlock currentScoreBlock = scores.get(position);

        TextView userTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_username);
        userTextView.setText(scores.get(position).getUsername());

        TextView scoreTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_total_score);

        TextView rankTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_rank);
        rankTextView.setText(Integer.toString(position + 1));

        TextView scannednumTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_scanned_number);
        scannednumTextView.setText(String.valueOf(scores.get(position).getTotalNum()));

        switch(this.sortingMethod) {
            case 0:
                scoreTextView.setText(Integer.toString(this.scores.get(position).getTotalSumOfScores()));
                break;
            case 1:
                scoreTextView.setText(String.valueOf(this.scores.get(position).getHighestScoring()));
                break;
            case 2:
//                mViewholder.scoreTextView.setText(Integer.toString(this.scores.get(position).getTotalNum()));
                break;
        }
        return listItem;
    }

    public void setSortingMethod(int sortingMethod) {
        this.sortingMethod = sortingMethod;
    }

    static class ViewHolder{
        TextView rankTextView;
        TextView userTextView;
        TextView scoreTextView;
        TextView scannednumTextView;
    }
}
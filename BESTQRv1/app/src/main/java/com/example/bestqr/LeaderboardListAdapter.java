package com.example.bestqr;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bestqr.ui.leaderboard.LeaderboardScoreBlock;
import com.example.bestqr.ui.leaderboard.LeaderboardViewModel;

import java.util.ArrayList;

public class LeaderboardListAdapter extends ArrayAdapter<LeaderboardScoreBlock> {
    ArrayList<LeaderboardScoreBlock> scores;
    Context context;
    int sortingMethod;

    public LeaderboardListAdapter(@NonNull Context context, ArrayList<LeaderboardScoreBlock> scores, int sortingMethod) {
        super(context, R.layout.qrlist_item);
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
        ViewHolder mViewholder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.leaderboardlist_item, parent, false);
            mViewholder.userTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_username);
            mViewholder.scoreTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_total_score);
            mViewholder.rankTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_rank);
            mViewholder.scannednumTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_scanned_number);

            convertView.setTag(mViewholder);//for faster scrolling
        }else{
            mViewholder = (ViewHolder)convertView.getTag();
        }

        mViewholder.userTextView.setText(scores.get(position).getUsername());
        mViewholder.rankTextView.setText(Integer.toString(position + 1));
        mViewholder.scannednumTextView.setText(String.valueOf(scores.get(position).getTotalNum()));
        switch(this.sortingMethod) {
            case 0:
                mViewholder.scoreTextView.setText(Integer.toString(this.scores.get(position).getTotalSumOfScores()));
                break;
            case 1:
                mViewholder.scoreTextView.setText(String.valueOf(this.scores.get(position).getHighestScoring()));
                break;
            case 2:
//                mViewholder.scoreTextView.setText(Integer.toString(this.scores.get(position).getTotalNum()));
                break;
        }


        return convertView;
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
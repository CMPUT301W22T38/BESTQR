package com.example.bestqr.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bestqr.R;
import com.example.bestqr.ui.leaderboard.LeaderboardScoreBlock;
import com.example.bestqr.ui.leaderboard.LeaderboardViewModel;

import java.util.ArrayList;

public class LeaderboardListAdapter extends ArrayAdapter<LeaderboardScoreBlock> implements Filterable {
    private ArrayList<LeaderboardScoreBlock> scores;
    private ArrayList<LeaderboardScoreBlock> filteredScores;
    private Context context;
    int sortingMethod;
    private Filter filter;

    public LeaderboardListAdapter(@NonNull Context context, int resource, ArrayList<LeaderboardScoreBlock> scores, int sortingMethod) {
        super(context, resource);
        this.scores = new ArrayList<LeaderboardScoreBlock>(scores);
        this.filteredScores = new ArrayList<LeaderboardScoreBlock>();
        // this.filteredScores = new ArrayList<LeaderboardScoreBlock>(scores);
        // Assign "global" ranks so that we can display the correct rank value after searching
        for(int i = 0; i < this.scores.size(); i++){
            LeaderboardScoreBlock s = this.scores.get(i);
            s.setRank(i);
            this.filteredScores.add(s);
        }
        this.context = context;
        this.scores = scores;
        this.sortingMethod = sortingMethod;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public LeaderboardScoreBlock getItem(int position) {
        return filteredScores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return filteredScores.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(this.context).inflate(R.layout.leaderboardlist_item, parent, false);
        }

        LeaderboardScoreBlock currentScoreBlock = getItem(position);

        TextView userTextView = (TextView) listItem.findViewById(R.id.leaderboard_list_username);
        userTextView.setText(getItem(position).getUsername());

        TextView scoreTextView = (TextView) listItem.findViewById(R.id.leaderboard_list_total_score);

        TextView rankTextView = (TextView) listItem.findViewById(R.id.leaderboard_list_rank);
        rankTextView.setText(Integer.toString(currentScoreBlock.getRank() + 1));

        TextView scannednumTextView = (TextView) listItem.findViewById(R.id.leaderboard_list_scanned_number);
        scannednumTextView.setText(String.valueOf(getItem(position).getTotalNum()));

        switch (this.sortingMethod) {
            case 0:
                scoreTextView.setText(Integer.toString(getItem(position).getTotalSumOfScores()));
                break;
            case 1:
                scoreTextView.setText(String.valueOf(getItem(position).getHighestScoring()));
                break;
            case 2:
//                mViewholder.scoreTextView.setText(Integer.toString(this.scores.get(position).getTotalNum()));
                break;
        }

        return listItem;
    }



    @NonNull
    @Override
    public Filter getFilter() {
        if (this.filter == null) {
            this.filter = new ScoreBlockFilter();
        }
        return this.filter;
    }

    public void setSortingMethod(int sortingMethod) {
        this.sortingMethod = sortingMethod;
    }

    static class ViewHolder {
        TextView rankTextView;
        TextView userTextView;
        TextView scoreTextView;
        TextView scannednumTextView;
    }

    // Adapted from: https://stackoverflow.com/questions/8255322/no-results-with-custom-arrayadapter-filter
    // Answer: https://stackoverflow.com/a/8258457
    // Answer by Pim Reijersen
    // CC BY-SA 3.0
    private class ScoreBlockFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                Log.d("ListAdapter", "Null Search String provided");
                ArrayList<LeaderboardScoreBlock> list = new ArrayList<LeaderboardScoreBlock>(scores);
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<LeaderboardScoreBlock> newValues = new ArrayList<LeaderboardScoreBlock>();
                Log.d("ListAdapter", "Search str: " + charSequence);
                for (LeaderboardScoreBlock s : scores) {
                    if (s.getUsername().contains(charSequence)) {
                        Log.d("ListAdapter", "username match: " + s.getUsername());
                        newValues.add(s);
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
            }
            return results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            filteredScores = (ArrayList<LeaderboardScoreBlock>) results.values;
            notifyDataSetChanged();
        }
    }

}
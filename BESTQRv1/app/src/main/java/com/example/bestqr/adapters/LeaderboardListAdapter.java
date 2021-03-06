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

import java.io.Serializable;
import java.util.ArrayList;

public class LeaderboardListAdapter extends ArrayAdapter<LeaderboardScoreBlock> implements Filterable, Serializable {
    private ArrayList<LeaderboardScoreBlock> scores;
    // List that changes depending on which usernames match a given search term.
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


    /**
     * Get an item from the currently-shown list, with the correct position regardless
     * of whether the list is currently filtered, or not.
     * @param position : integer position of item in filtered/unfiltered list.
     * @return LeaderboardScoreBlock : item at position
     */
    @Nullable
    @Override
    public LeaderboardScoreBlock getItem(int position) {
        return filteredScores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the number of items in the currently-shown list, accounting for if it is filtered or not.
     * @return int : Number of items in currently-shown list.
     */
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

        TextView rankTextView = (TextView) listItem.findViewById(R.id.leaderboard_list_rank);
        rankTextView.setText(Integer.toString(currentScoreBlock.getRank() + 1));

        TextView scannednumTextView = (TextView) listItem.findViewById(R.id.leaderboard_list_scanned_number);
        scannednumTextView.setText(String.valueOf(getItem(position).getTotalNum()));

        TextView scoreTextView = (TextView) listItem.findViewById(R.id.leaderboard_list_total_score);

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
            // If search term is empty, then simply copy full array to filtered array.
            if (charSequence == null || charSequence.length() == 0) {
                ArrayList<LeaderboardScoreBlock> list = new ArrayList<LeaderboardScoreBlock>(scores);
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<LeaderboardScoreBlock> newValues = new ArrayList<LeaderboardScoreBlock>();
                // Iterate through all scoreBlocks, adding them if their username matches the search term.
                for (LeaderboardScoreBlock s : scores) {
                    if (s.getUsername().contains(charSequence)) {
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
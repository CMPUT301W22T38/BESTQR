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

import java.util.ArrayList;

public class LeaderboardListAdapter extends ArrayAdapter<String> {
    ArrayList<String> usernames;
    ArrayList<Integer> scores;
    ArrayList<Integer> ranks;

    Context mContext;
    public LeaderboardListAdapter(@NonNull Context context, ArrayList<String> usernames, ArrayList<Integer> scores, ArrayList<Integer> ranks) {
        super(context, R.layout.qrlist_item);
        this.usernames = usernames;
        this.scores = scores;
        this.ranks = ranks;
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
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.leaderboardlist_item, parent, false);
            mViewholder.userTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_user);
            mViewholder.scoreTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_score);
            mViewholder.rankTextView = (TextView) convertView.findViewById(R.id.leaderboard_list_rank);
            convertView.setTag(mViewholder);//for faster scrolling
        }else{
            mViewholder = (ViewHolder)convertView.getTag();
        }

        //mViewholder.QRimage.setImageBitmap(Pictures.get(position));
        //mViewholder.userTextView.setText(usernames.get(position));
        //mViewholder.userTextView.setText();
        //mViewholder.userTextView.setText();

        return convertView;
    }

    static class ViewHolder{
        TextView userTextView;
        TextView scoreTextView;
        TextView rankTextView;
    }
}

public class LeaderboardListItem(){


}
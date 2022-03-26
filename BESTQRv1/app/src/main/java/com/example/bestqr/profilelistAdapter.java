package com.example.bestqr;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class profilelistAdapter extends ArrayAdapter<String> {
    ArrayList<Integer> scores;
    ArrayList<Bitmap> Pictures;
    ArrayList<String> timestamps;


    Context mContext;
    public profilelistAdapter(@NonNull Context context, ArrayList<Integer> qrScores,
                         ArrayList<String> qrTimestamps, ArrayList<Bitmap> qrPictures) {
        super(context, R.layout.profilelist_item);
        this.scores = qrScores;
        this.timestamps = qrTimestamps;
        this.Pictures = qrPictures;

        this.mContext = context;

    }

    @Override
    public int getCount(){
        return scores.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewholder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.profilelist_item, parent, false);


            convertView.setTag(mViewholder);//for faster scrolling
        } else {
            mViewholder = (ViewHolder) convertView.getTag();
        }
        //set value for image view and text view

        mViewholder.QRimage = (ImageView) convertView.findViewById(R.id.imageView);
        mViewholder.Score = (TextView) convertView.findViewById(R.id.profile_score);
        mViewholder.Timestamp = (TextView) convertView.findViewById(R.id.profile_timestamp);

        mViewholder.QRimage.setImageBitmap(Pictures.get(position));
        mViewholder.Score.setText(Integer.toString(scores.get(position)));
        mViewholder.Timestamp.setText(String.valueOf(timestamps.get(position)));


        return convertView;
    }






    static class ViewHolder{
        ImageView QRimage;
        TextView Score;
        TextView Timestamp;
        CheckBox Checkbox;

    }
}
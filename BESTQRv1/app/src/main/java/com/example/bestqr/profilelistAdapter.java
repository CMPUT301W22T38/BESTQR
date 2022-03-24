package com.example.bestqr;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Profile muserProfile;
    public profilelistAdapter(@NonNull Context context, ArrayList<Integer> qrScores,
                              ArrayList<String> qrTimestamps, ArrayList<Bitmap> qrPictures,Profile userProfile) {
        super(context, R.layout.profilelist_item);
        this.scores = qrScores;
        this.timestamps = qrTimestamps;
        this.Pictures = qrPictures;
        this.mContext = context;
        this.muserProfile = userProfile;

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
        mViewholder.Score = (TextView) convertView.findViewById(R.id.textView1);
        mViewholder.delete_button = (Button)convertView.findViewById(R.id.profile_deleteqr);
        mViewholder.Timestamp = (TextView) convertView.findViewById(R.id.textView2);

        mViewholder.QRimage.setImageBitmap(Pictures.get(position));
        mViewholder.Score.setText(Integer.toString(scores.get(position)));
        mViewholder.Timestamp.setText(String.valueOf(timestamps.get(position)));

        mViewholder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                muserProfile.removeCodebyPosition(position);
            }
        });

        return convertView;
    }






    static class ViewHolder{
        ImageView QRimage;
        TextView Score;
        TextView Timestamp;
        Button delete_button;

    }
}

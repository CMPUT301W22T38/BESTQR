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

public class Adapter extends ArrayAdapter<String> {
    ArrayList<Integer> scores;
    ArrayList<Bitmap> Pictures;
    Context mContext;
    public Adapter(@NonNull Context context, ArrayList<Integer> qrScores, ArrayList<Bitmap> qrPictures) {
        super(context, R.layout.qrlist_item);
        this.scores = qrScores;
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
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.qrlist_item, parent, false);
            mViewholder.QRimage = (ImageView) convertView.findViewById(R.id.imageView);
            mViewholder.Score = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(mViewholder);//for faster scrolling
        }else{
            mViewholder = (ViewHolder)convertView.getTag();
        }
        //set value for image view and text view
        mViewholder.QRimage.setImageBitmap(Pictures.get(position));
        mViewholder.Score.setText(Integer.toString(scores.get(position)));

        return convertView;
    }

    static class ViewHolder{
        ImageView QRimage;
        TextView Score;
    }
}

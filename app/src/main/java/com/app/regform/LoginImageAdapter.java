package com.app.regform;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class LoginImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Bitmap> imageChunks;
    private int imageWidth, imageHeight;

    //constructor
    public LoginImageAdapter(Context context, ArrayList<Bitmap> images) {
        mContext = context;
        imageChunks = images;
        imageWidth = images.get(0).getWidth();
        imageHeight = images.get(0).getHeight();
    }

    @Override
    public int getCount() {
        return imageChunks.size();
    }

    @Override
    public Object getItem(int position) {
        return imageChunks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView image;
        if (convertView == null) {
            image = new ImageView(mContext);

            image.setLayoutParams(new GridView.LayoutParams(imageWidth - 30, imageHeight - 30));
            image.setPadding(2, 2, 2, 2);
        } else {
            image = (ImageView) convertView;
        }
        image.setImageBitmap(imageChunks.get(position));
        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                ((PassMatrixActivity) mContext).selectedPassword(position);

            }
        });
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setBackground(int position, ImageView image){

        for (int i =0; i<imageChunks.size(); i++){
            if (i != position){
                image.setBackgroundColor(mContext.getColor(R.color.white));
            }else {
                image.setBackgroundColor(mContext.getColor(R.color.white));
            }
        }

    }
}

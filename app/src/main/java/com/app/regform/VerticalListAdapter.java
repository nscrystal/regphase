package com.app.regform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.Arrays;
import java.util.Collections;

public class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.ViewHolder> {

    private Context mContext;
    private String[] array;
    private int imageHeight;
    private Boolean selected = false;
    private int selection1pos, selection2pos;

    public VerticalListAdapter(Context context, String[] array, int imageHeight) {
        this.mContext = context;
        this.array = array;
        this.imageHeight = imageHeight;

        Collections.shuffle(Arrays.asList(array));
        ((PassMatrixActivity)mContext).saveVerticalArray(array);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.vertical_list_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.item_TV.setText(array[position]);
        holder.item_CV.setMinimumHeight(imageHeight);

        holder.item_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapItems(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView item_TV;
        MaterialCardView item_CV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            item_TV = mView.findViewById(R.id.verticalListItem_TV);
            item_CV = mView.findViewById(R.id.verticalListItem_CV);

        }
    }

    private void swapItems(int position){
        if (!selected){
            selection1pos = position;
            selected = true;
        }else {
            selection2pos = position;
            Collections.swap(Arrays.asList(array),selection1pos,selection2pos);
            notifyDataSetChanged();
            selected = false;
//            Log.d("ARRAY", String.valueOf(Arrays.asList(array)));
            ((PassMatrixActivity)mContext).saveVerticalArray(array);
        }
    }

}
package com.example.srivatsan.gen.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.srivatsan.gen.R;
import com.example.srivatsan.gen.displayElement;

import org.json.JSONObject;

/**
 * Created by srivatsan on 10/5/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private String[] mDataset;
    private String mDataType;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.sent_text);
            mImageView = (ImageView) v.findViewById(R.id.sent_image);
        }
    }

    public RecyclerViewAdapter(String[] myDataset, String dataType) {
        mDataset = myDataset; mDataType = dataType;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_data, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject obj = new JSONObject(displayElement.prefs.getString((position + 1) + "", "{}"));
            if (obj.getString("type").equals("text")) {
                holder.mTextView.setText(obj.getString("content"));
                holder.mImageView.getLayoutParams().height=0;
                holder.mImageView.getLayoutParams().width=0;
            }
            else {
                holder.mTextView.getLayoutParams().height=0;
                holder.mTextView.getLayoutParams().width=0;
                //TODO : set image resource
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

package com.example.srivatsan.gen.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.srivatsan.gen.R;
import com.example.srivatsan.gen.displayElement;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by srivatsan on 10/5/15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private String[] mDataset;
    private String mDataType;
    public Context mContext;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.sent_text);
            mImageView = (ImageView) v.findViewById(R.id.sent_image);
        }
    }

    public RecyclerViewAdapter(String[] myDataset, String dataType, Context context) {
        mDataset = myDataset; mDataType = dataType;
        mContext = context;
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
            displayElement.prefs = mContext.getSharedPreferences("ChatHistory", mContext.MODE_PRIVATE);
            JSONObject obj = new JSONObject(displayElement.prefs.getString((position + 1) + "", "{}"));
            Log.i("pos", position+"");
            if (obj.getString("type").equals("text")) {
                holder.mTextView.setText(obj.getString("content"));
                holder.mImageView.getLayoutParams().height=0;
                holder.mImageView.getLayoutParams().width=0;
            }
            else {
                String path = obj.getString("content");
                holder.mTextView.getLayoutParams().height=0;
                holder.mTextView.getLayoutParams().width=0;
                holder.mImageView.getLayoutParams().height=500;
               /* holder.mImageView.getLayoutParams().width=100;*/
                File imgFile = new File("/sdcard/" + path);
                Log.i("img", path);
                if(imgFile.exists()) {
                        Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        holder.mImageView.setImageBitmap(bmp);
                }
                //TODO : set image resource
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        displayElement.prefs = mContext.getSharedPreferences("ChatHistory", mContext.MODE_PRIVATE);

        return displayElement.prefs.getInt("no_of_chats",0);
    }
}

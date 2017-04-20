package com.philips.platform.appframework.testmicroappfw.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;

import java.util.ArrayList;

/**
 * Created by philips on 10/02/17.
 */

public class CoCoAdapter extends RecyclerView.Adapter<CoCoAdapter.ChapterViewHolder> {

    private ArrayList<CommonComponent> commonComponentsList;

    private Context context;

    public CoCoAdapter(Context context, ArrayList<CommonComponent> commonComponentsList) {
        this.commonComponentsList = commonComponentsList;
        this.context = context;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_listitem, parent, false);
        return new ChapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        CommonComponent chapter = commonComponentsList.get(position);
        holder.chapterTextView.setText(chapter.getCocoName());
    }

    @Override
    public int getItemCount() {
        if (commonComponentsList != null) {
            return commonComponentsList.size();
        }
        return 0;
    }

    protected CommonComponent getItemAtPosition(int i){
        return commonComponentsList.get(i);
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {
        public TextView chapterTextView;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            chapterTextView = (TextView) itemView.findViewById(R.id.chapter_textview);
        }
    }
}

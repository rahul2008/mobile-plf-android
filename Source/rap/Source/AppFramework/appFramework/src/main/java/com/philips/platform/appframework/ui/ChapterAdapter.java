/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.ui;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.models.Chapter;

import java.util.ArrayList;

/**
 * Created by philips on 10/02/17.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private ArrayList<Chapter> chaptersList;

    private Context context;

    private ChapterListCallback chapterListCallback;

    public interface ChapterListCallback{
        void onChapterItemClicked(Chapter chapter);
    }

    public ChapterAdapter(Context context, ArrayList<Chapter> chaptersList,ChapterListCallback chapterListCallback) {
        this.chaptersList = chaptersList;
        this.context = context;
        this.chapterListCallback=chapterListCallback;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_listitem, parent, false);
        return new ChapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        final Chapter chapter = chaptersList.get(position);
        holder.chapterTextView.setText(chapter.getChapterName());
        holder.itemView.setTag(chapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chapter c=(Chapter) v.getTag();
                chapterListCallback.onChapterItemClicked(c);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (chaptersList != null) {
            return chaptersList.size();
        }
        return 0;
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {
        public TextView chapterTextView;
        public View itemView;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            chapterTextView = (TextView) itemView.findViewById(R.id.chapter_textview);
        }
    }
}

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.models.CommonComponent;

import java.util.ArrayList;

/**
 * Created by philips on 10/02/17.
 */

public class CoCoAdapter extends RecyclerView.Adapter<CoCoAdapter.ChapterViewHolder> {

    private ArrayList<CommonComponent> commonComponentsList;

    private Context context;

    private COCOListPresenter cocoListPresenter;

    public CoCoAdapter(Context context, ArrayList<CommonComponent> commonComponentsList, COCOListPresenter cocoListPresenter) {
        this.commonComponentsList = commonComponentsList;
        this.context = context;
        this.cocoListPresenter=cocoListPresenter;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_listitem, parent, false);
        return new ChapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        final CommonComponent chapter = commonComponentsList.get(position);
        holder.chapterTextView.setText(chapter.getCocoName());
        holder.cocoListItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cocoListPresenter.onEvent(chapter.getCocoName());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (commonComponentsList != null) {
            return commonComponentsList.size();
        }
        return 0;
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {
        public TextView chapterTextView;
        public LinearLayout cocoListItemLayout;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            chapterTextView = (TextView) itemView.findViewById(R.id.chapter_textview);
            cocoListItemLayout=(LinearLayout)itemView.findViewById(R.id.chapter_item_linear_layout);
        }
    }
}

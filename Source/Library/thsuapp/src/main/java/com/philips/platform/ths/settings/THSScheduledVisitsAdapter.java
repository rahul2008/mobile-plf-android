/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.practice.PracticeRecyclerViewAdapter;

public class THSScheduledVisitsAdapter extends RecyclerView.Adapter<PracticeRecyclerViewAdapter.CustomViewHolder> {
    @Override
    public PracticeRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PracticeRecyclerViewAdapter.CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView logo;
        protected TextView label;
        protected RelativeLayout relativeLayout;

        public CustomViewHolder(View view) {
            super(view);
            this.logo = (ImageView) view.findViewById(R.id.pth_practice_logo);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.pth_practice_row_layout);
            this.label = (TextView) view.findViewById(R.id.pth_practice_name);

        }
    }
}

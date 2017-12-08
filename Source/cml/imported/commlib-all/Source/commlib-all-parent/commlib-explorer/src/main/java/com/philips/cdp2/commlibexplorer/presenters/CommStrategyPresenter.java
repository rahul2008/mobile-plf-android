/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.presenters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp2.commlibexplorer.strategy.CommStrategy;

import nl.rwslinkman.presentable.Presenter;

public class CommStrategyPresenter implements Presenter<CommStrategy, CommStrategyPresenter.ViewHolder> {
    @Override
    public CommStrategyPresenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.textView = (TextView) v.findViewById(android.R.id.text1);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, CommStrategy item) {
        String strategyName = item.getType().value();
        viewHolder.textView.setText(strategyName);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

/*
 * © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlibexplorer.presenters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp2.commlibexplorer.R;

import nl.rwslinkman.presentable.Presenter;

abstract class TwoLinePresenter<T> implements Presenter<T, TwoLinePresenter.ViewHolder> {

    @Override
    public TwoLinePresenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.two_line_listitem, parent, false);

        TwoLinePresenter.ViewHolder viewHolder = new TwoLinePresenter.ViewHolder(v);
        viewHolder.titleView = (TextView) v.findViewById(R.id.item_title);
        viewHolder.subtitleView = (TextView) v.findViewById(R.id.item_subtitle);

        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView subtitleView;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

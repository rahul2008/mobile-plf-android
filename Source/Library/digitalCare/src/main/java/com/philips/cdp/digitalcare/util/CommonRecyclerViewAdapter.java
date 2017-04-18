package com.philips.cdp.digitalcare.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by arbin on 11/04/2017.
 */

public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter {

    private final ArrayList<T> mItems;
    private final int mItemLayoutId;

    public CommonRecyclerViewAdapter(ArrayList<T> items, int itemLayoutId) {
        this.mItems = items;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new CommonRecyclerViewHolder(v);
    }

    public abstract void bindData(RecyclerView.ViewHolder holder, T item);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final T item = mItems.get(position);
        bindData(holder,item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}


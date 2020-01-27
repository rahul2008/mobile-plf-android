package com.philips.cdp.digitalcare.util;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arbin on 11/04/2017.
 */

public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  List<T> mItems;
    private final int mItemLayoutId;

    public CommonRecyclerViewAdapter(List<T> items, int itemLayoutId) {
        this.mItems = items;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new CommonRecyclerViewHolder(v);
    }

    public abstract void bindData(RecyclerView.ViewHolder holder, T item);

    public void swap(List<T> data){
        mItems = new ArrayList<>(data);
        notifyDataSetChanged();
    }

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


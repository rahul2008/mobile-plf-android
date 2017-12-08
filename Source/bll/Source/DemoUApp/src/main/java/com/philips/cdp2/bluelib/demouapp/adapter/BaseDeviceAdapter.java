/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp2.bluelib.demouapp.R;

import java.util.List;

public abstract class BaseDeviceAdapter<T> extends RecyclerView.Adapter<BaseDeviceAdapter.DeviceViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    protected final List<T> mItems;

    public BaseDeviceAdapter(List<T> items) {
        mItems = items;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bll_listitem_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public abstract T getItem(int position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View itemView);

        void onItemLongClick(int position, View itemView);
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final TextView rssiView;
        public final TextView nameView;
        public final TextView addressView;

        public DeviceViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            rssiView = (TextView) view.findViewById(R.id.bll_rssi);
            nameView = (TextView) view.findViewById(R.id.bll_name);
            addressView = (TextView) view.findViewById(R.id.bll_address);
        }

        @Override
        public void onClick(View v) {
            OnItemClickListener listener = BaseDeviceAdapter.this.mOnItemClickListener;
            if (listener == null) {
                return;
            }
            listener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            OnItemClickListener listener = BaseDeviceAdapter.this.mOnItemClickListener;
            if (listener == null) {
                return false;
            }
            listener.onItemLongClick(getAdapterPosition(), v);

            return true;
        }
    }
}

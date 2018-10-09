/*
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.homewificonnection;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ews.R;
import com.philips.platform.ews.databinding.EwsWifiNodeItemBinding;
import com.philips.cdp.dicommclient.networknode.WiFiNode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SelectWiFiAdapter extends RecyclerView.Adapter<SelectWiFiAdapter.BaseViewHolder> {

    public interface OnWifiNodeSelectListener {
        void onWifiNodeSelected(@NonNull String selectedSSID);
    }

    @NonNull
    private List<WiFiNode> wifiList;
    @Nullable
    private OnWifiNodeSelectListener onWifiNodeSelectListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    @Inject
    public SelectWiFiAdapter() {
        this.wifiList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EwsWifiNodeItemBinding viewBinder = DataBindingUtil.inflate(layoutInflater, R.layout.ews_wifi_node_item, parent, false);
        return new BaseViewHolder(viewBinder);
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(position);
    }

    public void removeOnWifiNodeSelectListener() {
        onWifiNodeSelectListener = null;
    }

    public void setOnWifiNodeSelectListener(@Nullable final OnWifiNodeSelectListener onWifiNodeSelectListener) {
        this.onWifiNodeSelectListener = onWifiNodeSelectListener;
    }

    public void setWifiList(@NonNull final List<WiFiNode> wifiList) {
        this.wifiList.clear();
        this.wifiList.addAll(wifiList);
        selectedPosition = RecyclerView.NO_POSITION;
        wrapNotifyDataSetChanged();
    }

    void wrapNotifyDataSetChanged() {
        notifyDataSetChanged();
    }

    void wrapNotifyItemChanged(final int position) {
        notifyItemChanged(position);
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        private final EwsWifiNodeItemBinding viewBinder;

        BaseViewHolder(EwsWifiNodeItemBinding viewBinder) {
            super(viewBinder.getRoot());
            this.viewBinder = viewBinder;
        }

        public void bind(final int position) {
            final WiFiNode wiFiNode = wifiList.get(position);
            viewBinder.setWifiNode(wiFiNode);
            if (selectedPosition == position) {
                //viewBinder.getRoot().setBackgroundResource(R.color.color_panel);
                viewBinder.wifiSelectedView.setVisibility(View.VISIBLE);
                viewBinder.ssidLabelView.setSelected(true);
            } else {
                viewBinder.wifiSelectedView.setVisibility(View.INVISIBLE);
                viewBinder.ssidLabelView.setSelected(false);
                //viewBinder.getRoot().setBackgroundResource(R.color.white);
            }

            viewBinder.getRoot().setOnClickListener(v -> {
                wrapNotifyItemChanged(selectedPosition);
                selectedPosition = getLayoutPosition();
                wrapNotifyItemChanged(selectedPosition);

                if (onWifiNodeSelectListener != null) {
                    onWifiNodeSelectListener.onWifiNodeSelected(wiFiNode.SSID);
                }
            });
            viewBinder.executePendingBindings();
        }
    }
}

package com.example.cdpp.bluelibreferenceapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNDevice;

import java.util.List;

public class DeviceListAdapter
        extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {

    private static final String TAG = "DeviceListAdapter";

    private final List<SHNDevice> mItems;

    public DeviceListAdapter(List<SHNDevice> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_available_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder, position: " + position);

        final SHNDevice device = mItems.get(position);

        holder.mItem = device;
        holder.mNameView.setText(device.getName());
        holder.mAddressView.setText(device.getAddress());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReferenceApplication.get().setSelectedDevice(holder.mItem);

                Context context = v.getContext();
                Intent intent = new Intent(context, DeviceDetailActivity.class);
                intent.putExtra(DeviceDetailActivity.ARG_ITEM_ID, position);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SHNDevice mItem;

        public final View mView;
        public final TextView mNameView;
        public final TextView mAddressView;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mAddressView = (TextView) view.findViewById(R.id.address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}

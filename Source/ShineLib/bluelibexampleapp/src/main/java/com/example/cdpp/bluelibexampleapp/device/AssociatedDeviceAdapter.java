package com.example.cdpp.bluelibexampleapp.device;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleApplication;
import com.example.cdpp.bluelibexampleapp.R;
import com.philips.pins.shinelib.SHNDevice;

import java.util.List;

public class AssociatedDeviceAdapter extends RecyclerView.Adapter<AssociatedDeviceAdapter.ViewHolder> {

    private final List<SHNDevice> mItems;

    public AssociatedDeviceAdapter(List<SHNDevice> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SHNDevice device = mItems.get(position);

        holder.addressView.setText(device.getAddress());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlueLibExampleApplication.get().setSelectedDevice(device);

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

        public final View view;

        public final TextView nameView;
        public final TextView addressView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;

            nameView = (TextView) view.findViewById(R.id.name);
            addressView = (TextView) view.findViewById(R.id.address);
        }
    }
}

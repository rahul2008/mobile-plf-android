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
import com.philips.pins.shinelib.SHNDeviceFoundInfo;

import java.util.List;
import java.util.Locale;

public class DeviceFoundInfoAdapter
        extends RecyclerView.Adapter<DeviceFoundInfoAdapter.ViewHolder> {

    private final List<SHNDeviceFoundInfo> mItems;

    public DeviceFoundInfoAdapter(List<SHNDeviceFoundInfo> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SHNDeviceFoundInfo deviceFoundInfo = mItems.get(position);

        final SHNDevice device = deviceFoundInfo.getShnDevice();

        holder.rssiView.setText(String.format(Locale.US, holder.view.getContext().getString(R.string.device_detail_device_rssi), deviceFoundInfo.getRssi()));
        holder.rssiView.setVisibility(View.VISIBLE);
        holder.nameView.setText(device.getName() == null ? holder.view.getContext().getString(R.string.unknown) : device.getName());
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

        public final TextView rssiView;
        public final TextView nameView;
        public final TextView addressView;

        public ViewHolder(View view) {
            super(view);

            this.view = view;

            rssiView = (TextView) view.findViewById(R.id.rssi);
            nameView = (TextView) view.findViewById(R.id.name);
            addressView = (TextView) view.findViewById(R.id.address);
        }
    }
}

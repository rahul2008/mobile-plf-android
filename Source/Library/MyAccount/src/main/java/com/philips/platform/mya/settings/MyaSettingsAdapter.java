package com.philips.platform.mya.settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

import java.util.Map;

/**
 * @author Aidan Follestad (afollestad)
 */
class MyaSettingsAdapter extends RecyclerView.Adapter<MyaSettingsAdapter.SettingsViewHolder> {

    private final static int DOUBLE_VIEW = 0;
    private final static int SINGLE_VIEW = 1;

    private Map<String, SettingsModel> settingsList;
    private View.OnClickListener onClickListener;

    MyaSettingsAdapter(Map<String, SettingsModel> settingsList) {
        this.settingsList = settingsList;
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    @Override
    public MyaSettingsAdapter.SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = 0;
        switch (viewType) {
            case DOUBLE_VIEW:
                layoutRes = R.layout.mya_double_item_layout;
                break;
            case SINGLE_VIEW:
                layoutRes = R.layout.mya_single_item_layout;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        view.setOnClickListener(onClickListener);
        UIDHelper.injectCalligraphyFonts();
        return new SettingsViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        String key = (String) settingsList.keySet().toArray()[position];
        SettingsModel settingsModel = settingsList.get(key);
        if (settingsModel.getItemCount() == 2) {
            return DOUBLE_VIEW;
        } else {
            return SINGLE_VIEW;
        }
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        getItemViewType(position);
        String key = (String) settingsList.keySet().toArray()[position];
        SettingsModel settingsModel = settingsList.get(key);
        holder.settingTitle.setText((settingsModel != null && settingsModel.getFirstItem() != null) ? settingsModel.getFirstItem() : key);
        if (holder.settingValue != null && settingsModel != null && settingsModel.getItemCount() == 2) {
            holder.settingValue.setText(settingsModel.getSecondItem());
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {
        Label settingTitle, settingValue;
        SettingsViewHolder(View view) {
            super(view);
            settingTitle = view.findViewById(R.id.item_title);
            settingValue = view.findViewById(R.id.second_item);
        }
    }
}
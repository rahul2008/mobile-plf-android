package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

public class UIPickerAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private int resID;

    public UIPickerAdapter(Context context, int resource, String[] states) {
        super(context, resource, states);
        resID = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Label view;
        if (convertView == null) {
            view = (Label) inflater.inflate(resID, parent, false);
        } else {
            view = (Label) convertView;
        }
        view.setText(getItem(position));
        return view;
    }
}
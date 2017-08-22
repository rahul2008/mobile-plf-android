package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;

import java.util.List;

public class UIPickerAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private int resID;
    private final List<String> states;
    private Drawable countArrow;

    public UIPickerAdapter(Context context, int resource, List<String> states) {
        super(context, resource, states);
        resID = resource;
        this.states = states;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.iap_uipicker_item_text, null);
            TextView textView = (TextView) v.findViewById(R.id.item_text);
            textView.setText(states.get(position));
        }

        return v;
    }

    @Override
    public int getCount() {
        return states.size();
    }
}

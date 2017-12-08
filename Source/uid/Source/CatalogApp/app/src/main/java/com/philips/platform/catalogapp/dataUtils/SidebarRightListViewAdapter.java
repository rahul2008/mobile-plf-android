/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.dataUtils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Label;

public class SidebarRightListViewAdapter extends ArrayAdapter<String> {

    private int resID;
    private LayoutInflater inflater;

    public SidebarRightListViewAdapter(@NonNull Context context, @LayoutRes int resource, String[] values, boolean isNavigationContext) {
        super(context, resource, values);
        resID = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(isNavigationContext)
            inflater = inflater.cloneInContext(UIDHelper.getNavigationThemedContext(context));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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


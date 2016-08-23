/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.bluelibtestapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter<T> extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<T> items;
    private final int customListLayoutId;
    private final Callbacks<T> callbacks;

    public CustomAdapter(List<T> items, int customListLayoutId, Callbacks<T> callbacks, Context context) {
        this.items = items;
        this.customListLayoutId = customListLayoutId;
        this.callbacks = callbacks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // extends BaseAdapter
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<Integer, View> viewDescriptors;

        if (convertView == null) {
            convertView = inflater.inflate(customListLayoutId, parent, false);

            viewDescriptors = new HashMap<>();
            for (int key : callbacks.getViewIds()) {
                viewDescriptors.put(key, convertView.findViewById(key));
            }

            convertView.setTag(viewDescriptors);
        }

        viewDescriptors = (Map<Integer, View>) convertView.getTag();

        T item = getItem(position);
        callbacks.setupView(item, viewDescriptors);

        return convertView;
    }

    public interface Callbacks<T> {
        int[] getViewIds();

        void setupView(T item, Map<Integer, View> viewDescriptors);
    }
}

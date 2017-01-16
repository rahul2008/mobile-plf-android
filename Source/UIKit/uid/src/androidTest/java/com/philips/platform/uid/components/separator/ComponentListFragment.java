/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components.separator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.platform.uid.drawable.SeparatorDrawable;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class ComponentListFragment extends Fragment {
    private HashMap<Integer, String> itemsMap = new HashMap<Integer, String>();
    ListView listView;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.philips.platform.uid.test.R.layout.layout_component_list, container, false);
        listView = (ListView) view.findViewById(com.philips.platform.uid.test.R.id.componentList);
        recyclerView = (RecyclerView) view.findViewById(com.philips.platform.uid.test.R.id.recyclerviewSeparatorItems);
        View divider = view.findViewById(com.philips.platform.uid.test.R.id.uid_test_separator);
        divider.setBackground(new SeparatorDrawable(getContext()));
        setListItems();
        setRecyclerItems();
        return view;
    }

    private void setRecyclerItems() {
        recyclerView.addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
        recyclerView.setAdapter(new SeparatorAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TestViewHolder(TextView rowView) {
            super(rowView);
            textView = rowView;
        }
    }

    private void setListItems() {
        String[] strings = getDemoItems().values().toArray(new String[1]);
        SeparatorDrawable separatorDrawable = new SeparatorDrawable(getContext());
        listView.setDivider(separatorDrawable);
        listView.setDividerHeight(separatorDrawable.getHeight());

        listView.setAdapter(new ArrayAdapter<>(this.getContext(), com.philips.platform.uid.test.R.layout.list_text, strings));
    }

    private Map<Integer, String> getDemoItems() {
        itemsMap = new LinkedHashMap<Integer, String>();
        itemsMap.put(0, "Button");
        itemsMap.put(1, "Textbox");
        itemsMap.put(2, "Settings");
        itemsMap.put(3, "Alert Dialog");
        itemsMap.put(4, "Progress bar");
        itemsMap.put(5, "label");
        itemsMap.put(6, "Separator");
        return sortMap(itemsMap);
    }

    private Map<Integer, String> sortMap(final HashMap<Integer, String> map) {
        TreeMap<Integer, String> sortedMap = new TreeMap<>(new IntegerComparator(map));

        sortedMap.putAll(map);

        return sortedMap;
    }

    private int getKeyFromValue(String value) {
        for (Map.Entry<Integer, String> entry : itemsMap.entrySet()) {
            if (entry.getValue().equals(value))
                return entry.getKey();
        }
        return 0;
    }

    private static class IntegerComparator implements Comparator<Integer> {
        private final HashMap<Integer, String> map;

        public IntegerComparator(final HashMap<Integer, String> map) {
            this.map = map;
        }

        @Override
        public int compare(final Integer key1, final Integer key2) {
            return map.get(key1).compareTo(map.get(key2));
        }
    }

    private static class SeparatorAdapter extends RecyclerView.Adapter {
        @Override
        public TestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            TextView textView = new TextView(parent.getContext());

            final TestViewHolder holder = new TestViewHolder(textView);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((TestViewHolder) holder).textView.setText("Hello");
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
package com.philips.cdp.uikit.hamburger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.R;

import java.util.ArrayList;

public class HamburgerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HamburgerItem> hamburgerItems;

    public HamburgerAdapter(Context context, ArrayList<HamburgerItem> hamburgerItems) {
        this.context = context;
        this.hamburgerItems = hamburgerItems;
    }

    @Override
    public int getCount() {
        return hamburgerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return hamburgerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        imgIcon.setImageResource(hamburgerItems.get(position).getIcon());
        txtTitle.setText(hamburgerItems.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if (hamburgerItems.get(position).getCounterVisibility()) {
            txtCount.setText(hamburgerItems.get(position).getCount());
        } else {
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }

        return convertView;
    }
}

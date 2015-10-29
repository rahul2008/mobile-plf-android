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
            convertView = mInflater.inflate(R.layout.uikit_drawer_list_item, null);
        }
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.list_icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.hamburger_title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.list_counter);
        setValuesToViews(position, imgIcon, txtTitle, txtCount);

        return convertView;
    }

    private void setValuesToViews(final int position, final ImageView imgIcon, final TextView txtTitle, final TextView txtCount) {
        int icon = hamburgerItems.get(position).getIcon();
        setImageView(imgIcon, icon);
        txtTitle.setText(hamburgerItems.get(position).getTitle());
        String count = hamburgerItems.get(position).getCount();
        setTextView(txtCount, count);
    }

    private void setTextView(final TextView txtCount, final String count) {
        if (count != null && !count.equals("0")) {
            txtCount.setText(count);
        } else {
            txtCount.setVisibility(View.GONE);
        }
    }

    private void setImageView(final ImageView imgIcon, final int icon) {
        if (icon != 0) {
            imgIcon.setImageResource(icon);
        }
        else{
            imgIcon.setVisibility(View.GONE);
        }
    }
}

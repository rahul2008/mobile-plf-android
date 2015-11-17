package com.philips.cdp.ui.catalog.CustomListView;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ListViewWithOptions extends BaseAdapter{

    public Activity activity;
    private LayoutInflater inflater=null;
    public ListViewWithOptions(Activity activity){
        this.activity = activity;
        inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(final int position) {
        return position;
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.uikit_listview_with_options_custom_layout, null);

        ImageView image = (ImageView) vi.findViewById(R.id.image);
        TextView name = (TextView) vi.findViewById(R.id.text1Name);
        TextView value = (TextView) vi.findViewById(R.id.text2value);
        TextView from = (TextView) vi.findViewById(R.id.from);

        image.setImageResource(R.drawable.image);
        //image.setColorFilter(Color.GREEN);
        name.setText("DiamondClean");
        value.setText("€209,99*");
        from.setText("from");
        return vi;
    }
}

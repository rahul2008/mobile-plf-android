package com.philips.cdp.ui.catalog.CustomListView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PuiSwitch;

/**
 * Created by 310213373 on 11/18/2015.
 */




/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ListViewWithIcons extends BaseAdapter {

    public Activity activity;
    private LayoutInflater inflater=null;
    public ListViewWithIcons(Activity activity){
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
            vi = inflater.inflate(R.layout.uikit_list_with_icons, null);

        ImageView image = (ImageView) vi.findViewById(R.id.image);
        TextView name = (TextView) vi.findViewById(R.id.text);
        PuiSwitch value = (PuiSwitch) vi.findViewById(R.id.switch_button);
        //TextView from = (TextView) vi.findViewById(R.id.from);

        image.setImageResource(R.drawable.maps_carrot);
        //image.setColorFilter(Color.GREEN);
       // name.setText("DiamondClean");
      //  value.setText("€209,99*");
      //  from.setText("from");
        return vi;
    }
}

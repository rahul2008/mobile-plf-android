/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.shamanland.fonticon.FontIconTextView;

public class ListViewWithoutIcons extends BaseAdapter {
    public Activity activity;
    Bundle saveBundle = new Bundle();
    private LayoutInflater inflater = null;

    public ListViewWithoutIcons(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 4;
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
        View vi = convertView;

        if (convertView == null)
            vi = inflater.inflate(R.layout.uikit_listview_without_icons, null);


        TextView name = (TextView) vi.findViewById(R.id.ifo);
        PuiSwitch value = (PuiSwitch) vi.findViewById(R.id.switch_button);
        TextView number = (TextView) vi.findViewById(R.id.numberwithouticon);
        TextView on_off = (TextView) vi.findViewById(R.id.medium);
        FontIconTextView arrow = (FontIconTextView) vi.findViewById(R.id.arrowwithouticons);
        TextView description = (TextView) vi.findViewById(R.id.text_description_without_icons);

        if (position == 0) {
            //name.setVisibility(View.VISIBLE);
            name.setText("Version ");

            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            //  arrow.setVisibility(View.GONE);
            number.setVisibility(View.VISIBLE);
            number.setText("1.1.4");
            on_off.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
        }

        if (position == 1) {
            name.setText("Geofence Zone ");

            value.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            //  arrow.setVisibility(View.GONE);
            on_off.setVisibility(View.VISIBLE);
            on_off.setText("Medium");
            arrow.setVisibility(View.VISIBLE);
        }
        if (position == 2) {
            name.setText("Enable Analytics ");
            value.setVisibility(View.VISIBLE);
            setSwitchState(value, "s1");

            value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    saveBundle.putBoolean("s1", ((PuiSwitch) v).isChecked());
                }
            });
            description.setVisibility(View.VISIBLE);
            description.setText("By enabling analytics, usage data is sent to us anonymously so we can continue to improve this Philips app.");
            //  mBadge.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.GONE);
        }
        if (position == 3) {
            name.setText("Language ");

            // value.setVisibility(View.VISIBLE);

            //  arrow.setVisibility(View.GONE);
            on_off.setVisibility(View.VISIBLE);
            on_off.setText("Default");
            arrow.setVisibility(View.VISIBLE);


        }


        //image.setColorFilter(Color.GREEN);
        // name.setText("DiamondClean");
        //  value.setText("€209,99*");
        //  from.setText("from");
        return vi;
    }

    public Bundle getSavedBundle() {
        return saveBundle;
    }

    public void setSavedBundle(Bundle bundle) {
        saveBundle = bundle;
    }

    private void setSwitchState(CompoundButton toggleSwitch, String code) {
        if (saveBundle.containsKey(code)) {
            toggleSwitch.setChecked(saveBundle.getBoolean(code));
        }
    }
}

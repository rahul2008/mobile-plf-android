package com.philips.cdp.ui.catalog.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.TabFragmentListicon;
import com.philips.cdp.uikit.customviews.PhilipsBadgeView;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.shamanland.fonticon.FontIconTextView;


import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by 310213373 on 11/18/2015.
 */




/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ListViewWithIcons extends BaseAdapter {

    public Context activity;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    Bundle saveBundle = new Bundle();

    public ListViewWithIcons(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activity = context;
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        int rowType = getItemViewType(position);

        if(rowType == TYPE_SEPARATOR){
            return false;
        }
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);


        holder = new ViewHolder();
        switch (rowType) {
            case TYPE_ITEM:
                try {
                    convertView = mInflater.inflate(R.layout.uikit_list_with_icons, null);
                }catch (Exception e){
                    e.printStackTrace();
                }
                holder.textView = (TextView) convertView.findViewById(R.id.text);
                holder.mImage=(ImageView)convertView.findViewById(R.id.image);
                holder.mBadge=(PhilipsBadgeView)convertView.findViewById(R.id.notification_badge);
                holder.arrow= (FontIconTextView) convertView.findViewById(R.id.arrow);
                holder.name=(TextView) convertView.findViewById(R.id.off_on);
                holder.value=(PuiSwitch)convertView.findViewById(R.id.switch_button);
                holder.done = (TextView) convertView.findViewById(R.id.textdownnoicon);



                if(position==1)
                {

                    holder.mImage.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_gear));
                    holder.mImage.setVisibility(View.VISIBLE);
                    holder.value.setVisibility(View.VISIBLE);

                    setSwitchState(holder.value,"s1");
                    holder.textView.setText(mData.get(position));
                    holder.value.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            saveBundle.putBoolean("s1", ((PuiSwitch) v).isChecked());
                        }
                    });

                }
                if(position==2)
                {
                    holder.mImage.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_gear));
                    holder.mImage.setVisibility(View.VISIBLE);
                    holder.value.setVisibility(View.VISIBLE);
//                    holder.value.setActivated(TabFragmentListicon.switch2);
                    setSwitchState(holder.value,"s2");
                    holder.value.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            saveBundle.putBoolean("s2", ((PuiSwitch) v).isChecked());
                        }
                    });
                    holder.textView.setText(mData.get(position));
                }
                if(position==3)
                {
                    holder.mImage.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_gear));
                    holder.name.setVisibility(View.VISIBLE);
                    holder.name.setText("Off");
                    holder.arrow.setVisibility(View.VISIBLE);
                    holder.mImage.setVisibility(View.VISIBLE);

                    holder.textView.setText(mData.get(position));

                }

                if(position==4)
                {
                    holder.name.setVisibility(View.VISIBLE);
                    holder.name.setText("On");
                    holder.arrow.setVisibility(View.VISIBLE);
                    holder.mImage.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_gear));
                    holder.mImage.setVisibility(View.VISIBLE);
                    holder.textView.setText(mData.get(position));

                }

                if(position==5)
                {
                    holder.mImage.setImageDrawable(VectorDrawable.create(activity, com.philips.cdp.uikit.R.drawable.uikit_gear));
                    holder.arrow.setVisibility(View.VISIBLE);
                    holder.mImage.setVisibility(View.VISIBLE);
                    holder.textView.setText(mData.get(position));

                }
                if(position==7)
                {

                    holder.arrow.setVisibility(View.VISIBLE);

                    holder.mBadge.setVisibility(View.VISIBLE);
                    holder.mBadge.setText("22");
                    holder.done.setVisibility(View.VISIBLE);
                    holder.done.setText(mData.get(position));
                    holder.textView.setVisibility(View.GONE);

                }
                if(position==8)
                {
                    holder.arrow.setVisibility(View.VISIBLE);
                    holder.done.setVisibility(View.VISIBLE);
                    holder.done.setText(mData.get(position));
                    holder.textView.setVisibility(View.GONE);

                }
                if(position==9)
                {
                    holder.mBadge.setVisibility(View.VISIBLE);
                    holder.mBadge.setText("3");
                    holder.arrow.setVisibility(View.VISIBLE);
                    holder.done.setVisibility(View.VISIBLE);
                    holder.done.setText(mData.get(position));
                    holder.textView.setVisibility(View.GONE);
                }



                break;
            case TYPE_SEPARATOR:
                convertView = mInflater.inflate(R.layout.uikit_list_sectionheader, null);
                holder.textViewH = (TextView) convertView.findViewById(R.id.sectionheader);
                holder.textViewH.setText(mData.get(position));
                break;
        }
        convertView.setTag(holder);

        holder = (ViewHolder) convertView.getTag();



        return convertView;
    }


    public static class ViewHolder {
        public TextView textView;
        public TextView done;
        public  TextView textViewH;
        ImageView mImage;
        TextView name;
        PuiSwitch value;
        ;
        FontIconTextView arrow;
        PhilipsBadgeView mBadge ;
    }

    public Bundle getSavedBundle() {
        return saveBundle;
    }

    public void setSavedBundle(Bundle bundle) {
        saveBundle = bundle;
    }

    private void setSwitchState(CompoundButton toggleSwitch, String code) {
        if(saveBundle.containsKey(code)) {
            toggleSwitch.setChecked(saveBundle.getBoolean(code));
        }
    }
}

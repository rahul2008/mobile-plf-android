package com.philips.cdp.ui.catalog.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PhilipsBadgeView;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.shamanland.fonticon.FontIconView;

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

    public Activity activity;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public ListViewWithIcons(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public boolean isEnabled(int position) {
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
                    holder.arrow= (FontIconView) convertView.findViewById(R.id.arrow);
                    holder.name=(TextView) convertView.findViewById(R.id.off_on);
                    holder.value=(PuiSwitch)convertView.findViewById(R.id.switch_button);

                    holder.textView.setText(mData.get(position));

                    if(position==1)
                    {
                        holder.mImage.setImageResource(R.drawable.call);
                        holder.mImage.setVisibility(View.VISIBLE);
                        holder.value.setVisibility(View.VISIBLE);

                    }
                    if(position==2)
                    {
                        holder.mImage.setImageResource(R.drawable.maps_carrot);
                        holder.mImage.setVisibility(View.VISIBLE);
                        holder.value.setVisibility(View.VISIBLE);

                    }
                    if(position==3)
                    {
                        holder.mImage.setImageResource(R.drawable.mail);
                        holder.name.setVisibility(View.VISIBLE);
                        holder.name.setText("Off");
                        holder.arrow.setVisibility(View.VISIBLE);
                        holder.mImage.setVisibility(View.VISIBLE);


                    }

                    if(position==4)
                    {
                        holder.name.setVisibility(View.VISIBLE);
                        holder.name.setText("On");
                        holder.arrow.setVisibility(View.VISIBLE);
                        holder.mImage.setImageResource(R.drawable.gear);
                        holder.mImage.setVisibility(View.VISIBLE);


                    }

                    if(position==5)
                    {
                        holder.mImage.setImageResource(R.drawable.alarm);
                        holder.arrow.setVisibility(View.VISIBLE);
                        holder.mImage.setVisibility(View.VISIBLE);


                    }
                    if(position==7)
                    {

                        holder.arrow.setVisibility(View.VISIBLE);

                        holder.mBadge.setVisibility(View.VISIBLE);
                        holder.mBadge.setText("22");


                    }
                    if(position==8)
                    {
                        holder.arrow.setVisibility(View.VISIBLE);



                    }
                    if(position==9)
                    {
                        holder.mBadge.setVisibility(View.VISIBLE);
                        holder.mBadge.setText("3");
                        holder.arrow.setVisibility(View.VISIBLE);


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
        public  TextView textViewH;
        ImageView mImage;
        TextView name;
        PuiSwitch value;
        ;
        FontIconView arrow;


        PhilipsBadgeView mBadge ;
    }

}
   /* @Override
    public int getCount() {
        return 11;
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
            vi = inflater.inflate(R.layout.uikit_list_with_icons, null);


        ImageView image = (ImageView) vi.findViewById(R.id.image);
        TextView name = (TextView) vi.findViewById(R.id.text);
        PuiSwitch value = (PuiSwitch) vi.findViewById(R.id.switch_button);
        TextView on_off = (TextView) vi.findViewById(R.id.off_on);
        FontIconView arrow = (FontIconView) vi.findViewById(R.id.arrow);


        PhilipsBadgeView mBadge = (PhilipsBadgeView) vi.findViewById(R.id.notification_badge);

        if (position == 1) {
            value.setVisibility(View.VISIBLE);
            mBadge.setVisibility(View.GONE);
            arrow.setVisibility(View.GONE);
            on_off.setVisibility(View.GONE);

        }
        if (position == 2) {
            mBadge.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);

        }

        if (position == 3) {
            arrow.setVisibility(View.VISIBLE);
        }
        if (position == 4) {
            on_off.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);
        }
        if (position == 5) {
            mBadge.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);
        }

        image.setImageResource(R.drawable.maps_carrot);
        //image.setColorFilter(Color.GREEN);
        // name.setText("DiamondClean");
        //  value.setText("€209,99*");
        //  from.setText("from");

        if (position == 0) {
            {
                if (convertView == null)
                    vi = inflater.inflate(R.layout.uikit_list_sectionheader, null);


                name = (TextView) vi.findViewById(R.id.sectionheader);
                name.setText("Title Pellendia");
            }


        }
        return vi;
    }
*/
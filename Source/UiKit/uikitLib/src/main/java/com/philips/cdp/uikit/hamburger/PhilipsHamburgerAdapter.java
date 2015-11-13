package com.philips.cdp.uikit.hamburger;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.ArrayList;

public class PhilipsHamburgerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HamburgerItem> hamburgerItems;
    private int totalCount = 0;

    public PhilipsHamburgerAdapter(Context context, ArrayList<HamburgerItem> hamburgerItems) {
        this.context = context;
        this.hamburgerItems = hamburgerItems;
        calculateCount();
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
        ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.uikit_drawer_list_item, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.imgIcon = (VectorDrawableImageView) convertView.findViewById(R.id.hamburger_list_icon);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.hamburger_item_text);
            viewHolder.txtCount = (TextView) convertView.findViewById(R.id.list_counter);
            convertView.setTag(viewHolder);
            addStates(convertView);
            addStatesToText(viewHolder.txtTitle);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        setValuesToViews(position, viewHolder.imgIcon, viewHolder.txtTitle, viewHolder.txtCount);
        return convertView;
    }

    private void setValuesToViews(final int position, final ImageView imgIcon, final TextView txtTitle, final TextView txtCount) {
        int icon = hamburgerItems.get(position).getIcon();
        setImageView(imgIcon, icon, txtTitle);
        txtTitle.setText(hamburgerItems.get(position).getTitle());
        int count = hamburgerItems.get(position).getCount();
        setTextView(txtCount, count);
    }

    private void setTextView(final TextView txtCount, final int count) {
        if (count > 0) {
            txtCount.setText(String.valueOf(count));
        } else {
            txtCount.setVisibility(View.GONE);
        }
    }

    private void setImageView(final ImageView imgIcon, final int icon, TextView txtTitle) {
        if (icon > 0) {
            if (imgIcon instanceof VectorDrawableImageView)
                imgIcon.setImageDrawable(VectorDrawable.create(context, icon));
            else
                imgIcon.setImageResource(icon);

        } else {
            imgIcon.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) txtTitle.getLayoutParams();
            layoutParams.leftMargin = (int) context.getResources().getDimension(R.dimen.uikit_hamburger_item_title_left_margin);
            txtTitle.setLayoutParams(layoutParams);
        }
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.setBackgroundDrawable(): sticking with deprecated API for now
    private void addStates(View convertView) {
        StateListDrawable states = new StateListDrawable();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor});
        states.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(typedArray.getColor(0, -1)));
        convertView.setBackgroundDrawable(states);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void addStatesToText(TextView txtTitle) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.veryLightColor});
        int[][] states = new int[][]{new int[]{android.R.attr.state_activated}, new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_activated}};
        int[] colors = new int[]{context.getResources().getColor(android.R.color.white), context.getResources().getColor(android.R.color.white), typedArray.getColor(0, -1)};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        txtTitle.setTextColor(colorStateList);
    }

    public int getCounterValue() {
        return totalCount;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        totalCount = 0;
        calculateCount();
    }

    public void calculateCount() {
        for (HamburgerItem hamburgerItem : hamburgerItems) {
            totalCount += hamburgerItem.getCount();
        }
    }

    static class ViewHolderItem {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtCount;
    }
}

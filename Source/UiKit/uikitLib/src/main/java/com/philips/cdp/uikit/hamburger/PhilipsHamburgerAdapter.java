package com.philips.cdp.uikit.hamburger;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;

import java.util.ArrayList;

public class PhilipsHamburgerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HamburgerItem> hamburgerItems;
    private int totalCount = 0;
    private int disabledColor;
    private int brightColor;
    private int selectedIndex;
    private int baseColor;

    public PhilipsHamburgerAdapter(Context context, ArrayList<HamburgerItem> hamburgerItems) {
        this.context = context;
        this.hamburgerItems = hamburgerItems;
        calculateCount();
    }

    public void setSelectedIndex(int ind) {
        selectedIndex = ind;
        notifyDataSetChanged();
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
        HamburgerItem hamburgerItem = hamburgerItems.get(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            setColors();
            if (hamburgerItem.isParent()) {
                convertView = mInflater.inflate(R.layout.uikit_hamburger_list_group, parent, false);
                convertView.setSelected(false);
                setGroupLayoutAlpha(convertView);
            } else {
                convertView = mInflater.inflate(R.layout.uikit_drawer_list_item, parent, false);
            }

            viewHolder = new ViewHolderItem();
            initializeViews(convertView, viewHolder);
            convertView.setTag(viewHolder);
            validateBottomDivider(hamburgerItem, viewHolder.bottomDivider);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        if (position == 0)
            viewHolder.transparentView.setVisibility(View.VISIBLE);

        setValuesToViews(position, viewHolder.imgIcon, viewHolder.txtTitle, viewHolder.txtCount, hamburgerItem, viewHolder.parentView);
        return convertView;
    }

    private void initializeViews(View convertView, ViewHolderItem viewHolder) {
        viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.hamburger_item_text);
        viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.hamburger_list_icon);
        viewHolder.txtCount = (TextView) convertView.findViewById(R.id.list_counter);
        viewHolder.bottomDivider = convertView.findViewById(R.id.divider_bottom);
        viewHolder.transparentView = convertView.findViewById(R.id.transparentView);
        viewHolder.parentView = (RelativeLayout) convertView.findViewById(R.id.hamburger_parent);
    }

    private void setColors() {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.veryLightColor, R.attr.brightColor, R.attr.baseColor});
        disabledColor = typedArray.getColor(0, -1);
        brightColor = typedArray.getColor(1, -1);
        baseColor = typedArray.getColor(2, -1);
        typedArray.recycle();
    }

    private void validateBottomDivider(HamburgerItem hamburgerItem, View bottomDivider) {
        if (hamburgerItem.isLastChild())
            bottomDivider.setVisibility(View.INVISIBLE);
    }


    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to setBackgroundDrawable(): sticking with deprecated API for now
    private void setGroupLayoutAlpha(View convertView) {
        convertView.setBackgroundColor(adjustAlpha(baseColor, 0.5f));
    }


    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private void setValuesToViews(final int position, final ImageView imgIcon, TextView txtTitle, final TextView txtCount, HamburgerItem hamburgerItem, View convertView) {
        if (!hamburgerItem.isParent()) {
            Drawable icon = hamburgerItems.get(position).getIcon();
            int count = hamburgerItems.get(position).getCount();
            setCounterView(txtCount, count);
            handleSelector(position, imgIcon, txtTitle, convertView, icon);
        }
        txtTitle.setText(hamburgerItems.get(position).getTitle());
    }

    private void handleSelector(int position, ImageView imgIcon, TextView txtTitle, View convertView, Drawable icon) {
        if (selectedIndex != -1 && position == selectedIndex) {
            setTextColor(txtTitle, Color.WHITE);
            setImageView(imgIcon, icon, txtTitle, Color.WHITE);
            convertView.setBackgroundColor(brightColor);
        } else {
            setTextColor(txtTitle, disabledColor);
            setImageView(imgIcon, icon, txtTitle, disabledColor);
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void setCounterView(final TextView txtCount, final int count) {
        if (count > 0) {
            txtCount.setText(String.valueOf(count));
        } else {
            txtCount.setVisibility(View.GONE);
        }
    }

    private void setImageView(final ImageView imgIcon, final Drawable icon, TextView txtTitle, int color) {
        if (icon != null) {
            imgIcon.setImageDrawable(icon);
            imgIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        } else {
            imgIcon.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) txtTitle.getLayoutParams();
            layoutParams.leftMargin = (int) context.getResources().getDimension(R.dimen.uikit_hamburger_item_icon_left_margin);
            txtTitle.setLayoutParams(layoutParams);
        }
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void setTextColor(TextView txtTitle, int color) {
        txtTitle.setTextColor(color);
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
        View bottomDivider;
        View transparentView;
        RelativeLayout parentView;
    }
}

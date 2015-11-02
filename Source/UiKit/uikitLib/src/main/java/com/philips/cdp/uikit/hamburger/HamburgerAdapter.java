package com.philips.cdp.uikit.hamburger;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

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
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        setValuesToViews(position, viewHolder.imgIcon, viewHolder.txtTitle, viewHolder.txtCount);

        return convertView;
    }

    private void setValuesToViews(final int position, final VectorDrawableImageView imgIcon, final TextView txtTitle, final TextView txtCount) {
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

    private void setImageView(final VectorDrawableImageView imgIcon, final int icon) {
        if (icon > 0) {
            imgIcon.setImageDrawable(VectorDrawable.create(context, icon));
        }
        else{
            imgIcon.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private void addStates(View convertView) {
        StateListDrawable states = new StateListDrawable();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor, R.attr.baseColor});
        states.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(typedArray.getColor(0, -1)));
        states.addState(new int[]{},
                new ColorDrawable(typedArray.getColor(1, -1)));
        convertView.setBackgroundDrawable(states);
    }

    static class ViewHolderItem {
        VectorDrawableImageView imgIcon;
        TextView txtTitle;
        TextView txtCount;
    }

}

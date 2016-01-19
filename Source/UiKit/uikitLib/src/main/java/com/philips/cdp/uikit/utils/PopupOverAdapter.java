package com.philips.cdp.uikit.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.uikit.R;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PopupOverAdapter extends ArrayAdapter<RowItem> {

    Context context;


    public PopupOverAdapter(final Context context, final int resource) {
        super(context, resource);
        this.context = context;
    }

    public PopupOverAdapter(final Context context, final int resource, final int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;
    }

   /* public PopupOverAdapter(final Context context, final int resource, final Object[] objects) {
        super(context, resource, objects);
        this.context = context;
    }*/

   /* public PopupOverAdapter(final Context context, final int resource, final int textViewResourceId, final Object[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
    }*/

    public PopupOverAdapter(final Context context, final int resource, final List objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public PopupOverAdapter(final Context context, final int resource, final int textViewResourceId, final List objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.uikit_simple_list_image_text, null);
            holder = new ViewHolder();


            holder.txtDesc = (TextView) convertView.findViewById(R.id.listtextview);
            holder.imageView = (ImageView) convertView.findViewById(R.id.listimageview);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        if (rowItem.getImageId() != 0) {

            holder.imageView.setImageResource(rowItem.getImageId());

        } else if (rowItem.getDrawable() != null) {
//            rowItem.getDrawable().mutate().setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP));
            //Drawable d = rowItem.getDrawable().mutate();
            //d.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP));
            holder.imageView.setImageDrawable(rowItem.getDrawable());

        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        if (rowItem.getStringId() != 0) {
            holder.txtDesc.setText(rowItem.getStringId());
        } else {
            holder.txtDesc.setText(rowItem.getDesc());
        }

        return convertView;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtDesc;
    }


}

package com.philips.cdp.prxsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.philips.cdp.prxclient.datamodels.support.RichText;

import java.util.List;

/**
 * Created by 310243577 on 1/4/2017.
 */

public class SupportModelAdapter extends ArrayAdapter<RichText> {

    public SupportModelAdapter(Context context, List<RichText> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RichText text = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.detailsitem, parent, false);
        }
        // Lookup view for data population
        TextView tvId = (TextView) convertView.findViewById(R.id.details);
        //  ListView tvTags = (ListView) convertView.findViewById(R.id.tags);
        // Populate the data into the template view using the data object
        if (text != null)
            tvId.setText(text.getType());

        // Return the completed view to render on screen
        return convertView;
    }

}

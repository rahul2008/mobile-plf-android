package com.philips.platform.aildemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.philips.platform.appinfra.contentloader.ContentInterface;
import com.philips.platform.appinfra.demo.R;

import java.util.List;

/**
 * Created by 310243577 on 11/22/2016.
 */

public class ContentListAdapter extends ArrayAdapter<ContentInterface> {

    public ContentListAdapter(Context context, List<ContentInterface> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ContentInterface article = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contentarticle_item, parent, false);
        }
        // Lookup view for data population
        TextView tvId = (TextView) convertView.findViewById(R.id.textView_article_ID);
        TextView tvTags = (TextView) convertView.findViewById(R.id.textView_article_tags);
      //  ListView tvTags = (ListView) convertView.findViewById(R.id.tags);
        // Populate the data into the template view using the data object
        tvId.setText("ID: "+article.getId());
        tvTags.setText("Tag(s): "+article.getTags());

        // Return the completed view to render on screen
        return convertView;
    }

}

package com.philips.cl.di.dev.pa.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.utils.Fonts;
import com.philips.cl.di.dev.pa.view.ListViewItem;

/**
 * Adapter to create the left menu. Each element's resources are stored in
 * com.philips.cl.di.dev.pa.customviews.ListViewItem.
 *  
 * @author 310150437
 *
 */
public class ListItemAdapter extends ArrayAdapter<ListViewItem> {
	
	public ListItemAdapter(Context context, List<ListViewItem> items) {
		super(context, R.layout.list_item, items);
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		 if (view == null)
		 {
			 LayoutInflater inflater = 
					 (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 view = inflater.inflate(R.layout.list_item, parent, false);
		 }
		 
		 ListViewItem item = getItem(position);
		 ImageView iv = (ImageView) view.findViewById(R.id.list_image);
		 iv.setImageResource(item.getImageId());
		 Context context = iv.getContext();
		 TextView tv = (TextView) view.findViewById(R.id.list_text);
		 tv.setTextColor(Color.rgb(70, 133, 255));
		 tv.setTypeface(Fonts.getGillsans(context));
		 tv.setText(item.getTextId());
		 
		 return view;
	}
}


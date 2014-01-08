package com.philips.cl.di.dev.pa.customviews.adapters;

import java.util.List;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.customviews.ListViewItem;
import com.philips.cl.di.dev.pa.util.Fonts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter to create the left menu. Each element's resources are stored in
 * com.philips.cl.di.dev.pa.customviews.ListViewItem.
 *  
 * @author 310150437
 *
 */
public class ListItemAdapter extends ArrayAdapter<ListViewItem> {
	
	private Context context;

	public ListItemAdapter(Context context, List<ListViewItem> items) {
		super(context, R.layout.list_item, items);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		 if (view == null)
		 {
			 LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 view = inflater.inflate(R.layout.list_item, parent, false);
		 }
		 
		 ListViewItem item = getItem(position);
		 ImageView iv = (ImageView) view.findViewById(R.id.list_image);
		 iv.setImageResource(item.getImageId());
		 Context context = iv.getContext();
		 TextView tv = (TextView) view.findViewById(R.id.list_text);
		 tv.setTypeface(Fonts.getGillsansLight(context));
		 tv.setText(item.getTextId());
		 
		 return view;
	}
}


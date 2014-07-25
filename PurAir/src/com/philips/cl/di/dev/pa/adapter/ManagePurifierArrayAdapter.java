package com.philips.cl.di.dev.pa.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.util.UpdateListener;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ManagePurifierArrayAdapter extends ArrayAdapter<PurAirDevice> {
	private List<PurAirDevice> purifiers;
	private Context context;
	private int resource;
	private HashMap<String, Boolean> selectedItems;
	private UpdateListener listener;

	public ManagePurifierArrayAdapter(Context context, int resource, List<PurAirDevice> purifiers,
			ListView listView, HashMap<String, Boolean> selectedItems, UpdateListener listener) {
		super(context, resource, purifiers);
		this.purifiers = purifiers;
		this.context = context;
		this.resource = resource;
		this.selectedItems = selectedItems;
		this.listener = listener;
		listView.setOnItemClickListener(managePurifierItemClickListener);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resource, null);
		
		ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
		ImageView arrowImg = (ImageView) view.findViewById(R.id.list_item_right_arrow);
		FontTextView purifierNameTxt = (FontTextView) view.findViewById(R.id.list_item_name);
		
		deleteSign.setVisibility(View.VISIBLE);
		
		final PurAirDevice purifier = purifiers.get(position);
		final String purifierName = purifiers.get(position).getName();
		final String usn = purifiers.get(position).getUsn();

		purifierNameTxt.setText(purifierName);
		purifierNameTxt.setTag(usn);
		
		arrowImg.setImageResource(R.drawable.arrow_blue);
		
		FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
		
		if (selectedItems.containsKey(usn) && selectedItems.get(usn)) {
			delete.setVisibility(View.VISIBLE);
			deleteSign.setImageResource(R.drawable.delete_t2b);
			arrowImg.setVisibility(View.INVISIBLE);
		} else {
			delete.setVisibility(View.GONE);
			deleteSign.setImageResource(R.drawable.delete_l2r);
			arrowImg.setVisibility(View.VISIBLE);
		}
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (selectedItems.containsKey(usn)) {
					selectedItems.remove(usn);
				}
				
				listener.onUpdate(purifier, selectedItems);
			}
		});
		
		return view;
	}
	
	private OnItemClickListener managePurifierItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			
			//For demo mode
			if (PurAirApplication.isDemoModeEnable()) return;
			
			ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
			ImageView arrowImg = (ImageView) view.findViewById(R.id.list_item_right_arrow);
			FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
			
			final String usn = purifiers.get(position).getUsn();
			
			if(delete.getVisibility() == View.GONE) {
				delete.setVisibility(View.VISIBLE);
				deleteSign.setImageResource(R.drawable.delete_t2b);
				arrowImg.setVisibility(View.INVISIBLE);
				selectedItems.put(usn, true);
			} else {
				delete.setVisibility(View.GONE);
				deleteSign.setImageResource(R.drawable.delete_l2r);
				arrowImg.setVisibility(View.VISIBLE);
				selectedItems.put(usn, false);
			}
		}
	};

}

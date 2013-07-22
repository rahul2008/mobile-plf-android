package com.philips.cl.di.dev.pa.screens.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.utils.Utils;

/**
 * The Class MenuListAdapter.
 * This class will be used to populate the left menu .
 */
public class MenuListAdapter extends ArrayAdapter<String> {

	/** The context. */
	Context context;
	
	/** The List for  image names. */
	List<String> alImageNames;
	
	/** The List  names. */
	List<String> alNames;

	/**
	 * Instantiates a new menu list adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param alImageNames the List image names
	 * @param alNames the List names
	 */
	public MenuListAdapter(Context context, int textViewResourceId,
			List<String> alImageNames, List<String> alNames) {
		super(context, textViewResourceId, alImageNames);
		this.context = context;
		this.alImageNames = alImageNames;
		this.alNames = alNames;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		PlaceHolder holder = new PlaceHolder();
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.menu_list_item, parent, false);
			ImageView iv = (ImageView) v.findViewById(R.id.ivIcon);
			TextView tv = (TextView) v.findViewById(R.id.tvLabel);
			holder.ivIcon = iv;
			holder.tvLabel = tv;
			v.setTag(holder);
		} else{
			holder = (PlaceHolder) v.getTag();
		}
		holder.tvLabel.setText(alNames.get(position));
		holder.tvLabel.setTypeface(Utils.getTypeFace(context));
		int resid = context.getResources().getIdentifier(
				alImageNames.get(position), AppConstants.DRAWABLE,
				context.getPackageName());
		holder.ivIcon.setBackgroundResource(resid);
		return v;
	}

	/**
	 * The Class PlaceHolder.
	 * This class will act as a view holder class.
	 */
	private static class PlaceHolder {
		/** The iv icon. */
		public ImageView ivIcon;
		/** The tv label. */
		public TextView tvLabel;
	}

}

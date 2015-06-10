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

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.util.DashboardUpdateListener;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ManagePurifierArrayAdapter extends ArrayAdapter<DICommAppliance> {

	private List<DICommAppliance> purifiers;
	private Context context;
	private int resource;
	private HashMap<String, Boolean> selectedItems;
	private DashboardUpdateListener listener;
    private String edit;

	public ManagePurifierArrayAdapter(Context context, int resource,
			List<DICommAppliance> purifiers, String edit,
			HashMap<String, Boolean> selectedItems, DashboardUpdateListener listener) {
		super(context, resource, purifiers);
		this.purifiers = purifiers;
		this.context = context;
		this.resource = resource;
		this.selectedItems = selectedItems;
        this.edit = edit;
		this.listener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resource, null);

		ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
		ImageView arrowImg = (ImageView) view.findViewById(R.id.list_item_right_arrow);
        arrowImg.setVisibility(View.INVISIBLE);
		FontTextView purifierNameTxt = (FontTextView) view.findViewById(R.id.list_item_name);
        FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);

        deleteSign.setClickable(false);
        deleteSign.setFocusable(false);

        setEditableIconVisibility(deleteSign);

		final AirPurifier purifier = (AirPurifier) purifiers.get(position);
		final String purifierName = purifier.getName();
		final String usn = purifier.getNetworkNode().getCppId();

		purifierNameTxt.setText(purifierName);
		purifierNameTxt.setTag(usn);
		
		if (position == 0) {
			deleteSign.setVisibility(View.VISIBLE);
			deleteSign.setImageResource(R.drawable.white_plus);
			return view;
		}

        setDeleteIconVisibility(deleteSign, delete, usn);

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

    private void setEditableIconVisibility(ImageView deleteSign) {
        if (context.getString(R.string.done).equals(edit)) {
            deleteSign.setVisibility(View.VISIBLE);
        } else {
            deleteSign.setVisibility(View.GONE);
        }
    }

    private void setDeleteIconVisibility(ImageView deleteSign, FontTextView delete, String usn) {
        if (selectedItems.containsKey(usn) && selectedItems.get(usn)) {
            setResources(deleteSign, delete, View.VISIBLE, R.drawable.red_cross);
        } else {
            setResources(deleteSign, delete, View.GONE, R.drawable.white_cross);
        }
    }

	public OnItemClickListener managePurifierItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {

			// For demo mode
			if (PurAirApplication.isDemoModeEnable()) return;
			
			if (position == 0) {
				listener.onItemClickGoToAddPurifier();
				return;
			}
			
            FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
            if (context.getString(R.string.done).equals(edit)) {
                ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
                deleteSign.setClickable(false);
                deleteSign.setFocusable(false);
                ImageView arrowImg = (ImageView) view.findViewById(R.id.list_item_right_arrow);
                arrowImg.setVisibility(View.INVISIBLE);
                AirPurifier purifier = (AirPurifier) purifiers.get(position);
                final String usn = purifier.getNetworkNode().getCppId();
                addToDeleteMap(deleteSign, delete, usn);
            } else {
                delete.setVisibility(View.GONE);
                listener.onItemClickGoToPage(position - 1);
            }
		}
	};

    private void addToDeleteMap(ImageView deleteSign, FontTextView delete, String usn) {
        if (delete.getVisibility() == View.GONE) {
            setResources(deleteSign, delete, View.VISIBLE, R.drawable.red_cross);
            selectedItems.put(usn, true);
        } else {
            setResources(deleteSign, delete, View.GONE, R.drawable.white_cross);
            selectedItems.put(usn, false);
        }
    }

    private void setResources(ImageView deleteSign, FontTextView delete, int visibility, int icon) {
        delete.setVisibility(visibility);
        deleteSign.setImageResource(icon);
    }

}

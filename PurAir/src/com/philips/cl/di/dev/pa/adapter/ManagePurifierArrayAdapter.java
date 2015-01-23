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
import com.philips.cl.di.dev.pa.util.DashboardUpdateListener;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ManagePurifierArrayAdapter extends ArrayAdapter<PurAirDevice> {
	private List<PurAirDevice> purifiers;
	private Context context;
	private int resource;
	private HashMap<String, Boolean> selectedItems;
	private DashboardUpdateListener listener;
    private boolean isEditable;

	public ManagePurifierArrayAdapter(Context context, int resource,
			List<PurAirDevice> purifiers, ListView listView, boolean isEditable,
			HashMap<String, Boolean> selectedItems, DashboardUpdateListener listener) {
		super(context, resource, purifiers);
		this.purifiers = purifiers;
		this.context = context;
		this.resource = resource;
		this.selectedItems = selectedItems;
        this.isEditable = isEditable;
		this.listener = listener;
		listView.setOnItemClickListener(managePurifierItemClickListener);
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

		final PurAirDevice purifier = purifiers.get(position);
		final String purifierName = purifiers.get(position).getName();
		final String usn = purifiers.get(position).getUsn();

		purifierNameTxt.setText(purifierName);
		purifierNameTxt.setTag(usn);

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
        if (isEditable) {
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

	private OnItemClickListener managePurifierItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {

			// For demo mode
			if (PurAirApplication.isDemoModeEnable()) return;
            FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
            if (isEditable) {
                ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
                deleteSign.setClickable(false);
                deleteSign.setFocusable(false);
                ImageView arrowImg = (ImageView) view.findViewById(R.id.list_item_right_arrow);
                arrowImg.setVisibility(View.INVISIBLE);
                PurAirDevice purifier = purifiers.get(position);
                final String usn = purifier.getUsn();
                addToDeleteMap(deleteSign, delete, usn);
            } else {
                delete.setVisibility(View.GONE);
                listener.onItemClickGoToPage(position);
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

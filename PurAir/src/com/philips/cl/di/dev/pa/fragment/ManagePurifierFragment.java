package com.philips.cl.di.dev.pa.fragment;

import java.util.Hashtable;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ManagePurifierFragment extends BaseFragment {
	
	private ManagePurifierAdapter adapter;
	private Cursor cursor;
	private PurifierDatabase database;
	private Hashtable<String, Boolean> selectedItemHashtable;
	private ListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new PurifierDatabase();
		selectedItemHashtable = new Hashtable<String, Boolean>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.manage_purifier, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ImageView addPurifier = (ImageView) getView().findViewById(R.id.manage_pur_add_img);
		addPurifier.setOnClickListener(addPurifierClickEvent);
		
		listView = (ListView) getView().findViewById(R.id.manage_pur_list);
		listView.setOnItemClickListener(managePurifierItemClickListener);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadDataFromDatabase();
	}
	
	private void loadDataFromDatabase() {
		cursor = database.getAllPurifiers();
		adapter = new ManagePurifierAdapter(getActivity(), cursor, false);
		listView.setAdapter(adapter);
	}
	
	private OnClickListener addPurifierClickEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//TODO newly discovered purifier list
		}
	};
	
	private OnItemClickListener managePurifierItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
			ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
			ImageView arrowImg = (ImageView) view.findViewById(R.id.list_item_right_arrow);
			FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
			
			Cursor cursor = (Cursor) adapter.getItem(position);
			cursor.moveToPosition(position);
			
			final String usn = cursor.getString(cursor.getColumnIndexOrThrow(AppConstants.KEY_AIRPUR_USN));
			
			if(delete.getVisibility() == View.GONE) {
				delete.setVisibility(View.VISIBLE);
				deleteSign.setImageResource(R.drawable.delete_t2b);
				arrowImg.setVisibility(View.INVISIBLE);
				selectedItemHashtable.put(usn, true);
			} else {
				delete.setVisibility(View.GONE);
				deleteSign.setImageResource(R.drawable.delete_l2r);
				arrowImg.setVisibility(View.VISIBLE);
				selectedItemHashtable.put(usn, false);
			}
		}
	};
	
	private class ManagePurifierAdapter extends CursorAdapter {

		public ManagePurifierAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@Override
		public void bindView(View view, Context ctx, Cursor cursor) {
			ImageView deleteSign = (ImageView) view.findViewById(R.id.list_item_delete);
			ImageView arrowImg = (ImageView) view.findViewById(R.id.list_item_right_arrow);
			FontTextView purifierNameTxt = (FontTextView) view.findViewById(R.id.list_item_name);
			
			deleteSign.setVisibility(View.VISIBLE);
			
			final String id = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_ID));
			final String purifierName = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_DEVICE_NAME));
			final String usn = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_USN));
			final String cppId = cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AIRPUR_CPP_ID));
			
			ALog.i(ALog.MANAGE_PUR, "id= " + id + "; purifierName= " + purifierName
					+ "; usn=" + usn + "; cppId=" + cppId);

			purifierNameTxt.setText(purifierName);
			purifierNameTxt.setTag(usn);
			
			arrowImg.setImageResource(R.drawable.arrow_blue);
			
			FontTextView delete = (FontTextView) view.findViewById(R.id.list_item_right_text);
			
			if (selectedItemHashtable.containsKey(usn) && selectedItemHashtable.get(usn)) {
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
					int effectedRow = database.deletePurifier(usn);
					if (effectedRow > 0) {
						loadDataFromDatabase();
					}
				}
			});
		}

		@Override
		public View newView(Context ctx, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View view = inflater.inflate(R.layout.simple_list_item, parent, false);
			
			return view;
		}
	}
}

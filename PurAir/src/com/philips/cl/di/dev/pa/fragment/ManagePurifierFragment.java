package com.philips.cl.di.dev.pa.fragment;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.adapter.ManagePurifierArrayAdapter;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.UpdateListener;

public class ManagePurifierFragment extends BaseFragment implements UpdateListener {
	
	private ManagePurifierArrayAdapter arrayAdapter;
	private PurifierDatabase database;
	private ListView listView;
	private List<PurAirDevice> purifiers;
	private HashMap<String, Boolean> selectedItems;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new PurifierDatabase();
		selectedItems = new HashMap<String, Boolean>();
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
//		listView.setOnItemClickListener(managePurifierItemClickListener);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadDataFromDatabase();
	}
	
	private void loadDataFromDatabase() {
		purifiers = database.getAllPurifiers(ConnectionState.DISCONNECTED);
		if (arrayAdapter != null) arrayAdapter = null;//For GarbageCollection
		arrayAdapter = new ManagePurifierArrayAdapter(
				getActivity(), R.layout.simple_list_item, purifiers, listView, selectedItems, this);
		listView.setAdapter(arrayAdapter);
	}
	
	private OnClickListener addPurifierClickEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.manage_pur_add_img) {
				((MainActivity) getActivity()).showFragment(new StartFlowChooseFragment());
			}
		}
	};

	@Override
	public void onUpdate(String id, HashMap<String, Boolean> selectedItems) {
		this.selectedItems = selectedItems;
		ALog.i(ALog.MANAGE_PUR, "ManagePurifier$onUpdate usn= " + id);
		int effectedRow = database.deletePurifier(id);
		if (effectedRow > 0) {
			if (selectedItems.containsKey(id)) {
				selectedItems.remove(id);
			}
			loadDataFromDatabase();
			DiscoveryManager.getInstance().updateStoreDevices();
		}
	}
}

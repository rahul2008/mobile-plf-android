package com.philips.cl.di.dev.pa.fragment;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.adapter.ManagePurifierArrayAdapter;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.cpp.PairingListener;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice.PAIRED_STATUS;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.UpdateListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class ManagePurifierFragment extends BaseFragment implements
		UpdateListener, PairingListener {

	private ManagePurifierArrayAdapter arrayAdapter;
	private PurifierDatabase database;
	private ListView listView;
	private List<PurAirDevice> purifiers;
	private HashMap<String, Boolean> selectedItems;

	// private ProgressDialogFragment progressDialogFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new PurifierDatabase();
		selectedItems = new HashMap<String, Boolean>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.manage_purifier, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// progressDialogFragment = ProgressDialogFragment.newInstance("");

		ImageView addPurifier = (ImageView) getView().findViewById(
				R.id.manage_pur_add_img);
		addPurifier.setOnClickListener(addPurifierClickEvent);

		listView = (ListView) getView().findViewById(R.id.manage_pur_list);
		// listView.setOnItemClickListener(managePurifierItemClickListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadDataFromDatabase();
	}

	private void loadDataFromDatabase() {
		// purifiers = database.getAllPurifiers(ConnectionState.DISCONNECTED);
		purifiers = DiscoveryManager.getInstance().getStoreDevices();
		if (arrayAdapter != null)
			arrayAdapter = null;// For GarbageCollection
		arrayAdapter = new ManagePurifierArrayAdapter(getActivity(),
				R.layout.simple_list_item, purifiers, listView, selectedItems,
				this);
		listView.setAdapter(arrayAdapter);

		if (purifiers.isEmpty()) {
			PurifierManager.getInstance().removeCurrentPurifier();
		}
	}

	private OnClickListener addPurifierClickEvent = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// For demo mode
			if (PurAirApplication.isDemoModeEnable() || Utils.getAppFirstUse())
				return;

			if (v.getId() == R.id.manage_pur_add_img) {
				List<PurAirDevice> storePurifiers = DiscoveryManager
						.getInstance().updateStoreDevices();
				if (storePurifiers.size() >= AppConstants.MAX_PURIFIER_LIMIT) {
					showAlertDialog("",
							getString(R.string.max_purifier_reached));
				} else {
					((MainActivity) getActivity())
							.showFragment(new StartFlowChooseFragment());
				}
			}
		}
	};

	private void showAlertDialog(String title, String message) {
		if (getActivity() == null)
			return;
		try {
			FragmentTransaction fragTransaction = getActivity()
					.getSupportFragmentManager().beginTransaction();

			Fragment prevFrag = getActivity().getSupportFragmentManager()
					.findFragmentByTag("max_purifier_reached");
			if (prevFrag != null) {
				fragTransaction.remove(prevFrag);
			}

			fragTransaction.add(
					DownloadAlerDialogFragement.newInstance(title, message),
					"max_purifier_reached").commitAllowingStateLoss();
		} catch (IllegalStateException e) {
			ALog.e(ALog.MANAGE_PUR, e.getMessage());
		}
	}

	@Override
	public void onUpdate(PurAirDevice purifier,
			HashMap<String, Boolean> selectedItems) {
		this.selectedItems = selectedItems;

		if (purifier == null)
			return;

		if (purifier.getPairedStatus() == PAIRED_STATUS.PAIRED) {
			// Remove pairing
			PairingHandler pairingHandler = new PairingHandler(this, purifier);
			pairingHandler.initializeRelationshipRemoval();
		}

		int effectedRow = database.deletePurifier(purifier.getUsn());
		if (effectedRow > 0) {
			if (selectedItems.containsKey(purifier.getUsn())) {
				selectedItems.remove(purifier.getUsn());
			}
			// Updates store device from DB
			DiscoveryManager.getInstance().updateStoreDevices();
			// List<PurAirDevice> purifiers =
			// DiscoveryManager.getInstance().updateStoreDevices();
			// At least one page exists in IndoorDashboard
			PurifierManager.getInstance().setCurrentIndoorViewPagerPosition(0);
			loadDataFromDatabase();
		}

		if (purifier.getConnectionState() != ConnectionState.CONNECTED_LOCALLY) {
			DiscoveryManager.getInstance().removeFromDiscoveredList(
					purifier.getEui64());
		} else {
			DiscoveryManager.getInstance().updatePairingStatus(
					purifier.getEui64(), PAIRED_STATUS.NOT_PAIRED);
		}

		PurAirDevice currentPurifier = PurifierManager.getInstance()
				.getCurrentPurifier();
		if (currentPurifier == null)
			return;
		if (currentPurifier.getEui64().equals(purifier.getEui64())) {
			PurifierManager.getInstance().removeCurrentPurifier();
		}
	}

	@Override
	public void onPairingSuccess() {
		// TODO
	}

	@Override
	public void onPairingFailed(PurAirDevice purifier) {
		// TODO
	}
}

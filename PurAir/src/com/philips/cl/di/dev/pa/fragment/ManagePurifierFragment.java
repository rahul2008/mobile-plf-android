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
import android.widget.ImageButton;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.adapter.ManagePurifierArrayAdapter;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.PairingHandler;
import com.philips.cl.di.dev.pa.cpp.PairingListener;
import com.philips.cl.di.dev.pa.dashboard.HomeFragment;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.outdoorlocations.UpdateMyPurifierListener;
import com.philips.cl.di.dev.pa.purifier.PurifierDatabase;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.DashboardUpdateListener;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.dicomm.communication.NullStrategy;

public class ManagePurifierFragment extends BaseFragment implements
        DashboardUpdateListener, PairingListener, OnClickListener {

	private ManagePurifierArrayAdapter arrayAdapter;
	private PurifierDatabase database;
	private ListView listView;
	private List<AirPurifier> purifiers;
	private HashMap<String, Boolean> selectedItems;
    private FontTextView editTV;

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
		MetricsTracker.trackPage(TrackPageConstants.MANAGE_PURIFIERS);
        initView();
        HomeFragment homeFragment = (HomeFragment) getParentFragment();
        if (homeFragment != null) {
			homeFragment.setUpdateMyPurifiersListner(new UpdateMyPurifierListener() {

				@Override
				public void onUpdate() {
					if (getString(R.string.done).equals(editTV.getText().toString())) {
						editTV.setText(getString(R.string.edit));
						saveLastPageCurrentPage();
						loadDataFromDatabase();
			            if (!selectedItems.isEmpty()) selectedItems.clear();
					}
				}
			});
		}
	}

	private void saveLastPageCurrentPage() {
		int size = DiscoveryManager.getInstance().getStoreDevices().size() + 1;
		AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(size);
	}

    private void initView(){
        editTV = (FontTextView) getView().findViewById(R.id.manage_purifier_edit_tv);
        editTV.setText(getString(R.string.edit));
        editTV.setOnClickListener(editPurifierClickEvent);
        listView = (ListView) getView().findViewById(R.id.manage_pur_list);
        ImageButton infoButton = (ImageButton) getView().findViewById(R.id.manage_purifier_info_img_btn);
        infoButton.setOnClickListener(this);
    }

	@Override
	public void onResume() {
		super.onResume();
		loadDataFromDatabase();
	}

	@Override
	public void onStop() {
		HomeFragment homeFragment = (HomeFragment) getParentFragment();
        if (homeFragment != null) {
			homeFragment.setUpdateMyPurifiersListner(null);
        }
		super.onStop();
	}

	private void loadDataFromDatabase() {
		purifiers = DiscoveryManager.getInstance().getStoreDevices();

        NullStrategy communicationStrategy = new NullStrategy();
        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(0);
        networkNode.setCppId("");
        networkNode.setIpAddress("");
        networkNode.setName(getString(R.string.add_purifier));
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
        
        AirPurifier addPurifierDevice = new AirPurifier(networkNode, communicationStrategy, "");
		
		purifiers.add(0, addPurifierDevice);
        AirPurifierManager.getInstance().setCurrentIndoorViewPagerPosition(AirPurifierManager.getInstance().getCurrentIndoorViewPagerPosition());
		if (arrayAdapter != null) arrayAdapter = null;// For GarbageCollection
		arrayAdapter = new ManagePurifierArrayAdapter(getActivity(),
				R.layout.simple_list_item, purifiers, editTV.getText().toString(), selectedItems, this);
		listView.setOnItemClickListener(arrayAdapter.managePurifierItemClickListener);
		listView.setAdapter(arrayAdapter);

		if (purifiers.isEmpty()) {
			AirPurifierManager.getInstance().removeCurrentPurifier();
		}
	}

	private OnClickListener editPurifierClickEvent = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// For demo mode
			if (PurAirApplication.isDemoModeEnable()) return;

			if (v.getId() == R.id.manage_purifier_edit_tv) {
				if (getString(R.string.edit).equals(editTV.getText().toString())) {
					editTV.setText(getString(R.string.done));
				} else {
					editTV.setText(getString(R.string.edit));
				}
				saveLastPageCurrentPage();
				loadDataFromDatabase();
	            if (!selectedItems.isEmpty()) selectedItems.clear();
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
			ALog.e(ALog.MANAGE_PUR, "Error: " + e.getMessage());
		}
	}

    @Override
    public void onItemClickGoToPage(int position) {
        HomeFragment homeFragment = (HomeFragment) getParentFragment();
        if (homeFragment != null) {
            homeFragment.gotoIndoorViewPage(position);
        }
    }

    @Override
	public void onUpdate(AirPurifier purifier,	HashMap<String, Boolean> selectedItems) {
		this.selectedItems = selectedItems;

		if (purifier == null)	return;

		if (purifier.getNetworkNode().getPairedState() == NetworkNode.PAIRED_STATUS.PAIRED) {
			// Remove pairing
			PairingHandler pairingHandler = new PairingHandler(this, purifier);
			pairingHandler.initializeRelationshipRemoval();
		}

        setCurrentPage(purifier);
        removePurifierFromDiscoveredList(purifier);
        removeCurrentPurifier(purifier);
        reloadViewPager();

	}

    private void reloadViewPager() {
        HomeFragment homeFragment = (HomeFragment) getParentFragment();
        if (homeFragment != null) {
            homeFragment.notifyIndoorPager();
        }
    }

    private void setCurrentPage(AirPurifier purifier) {
        int effectedRow = database.deletePurifier(purifier.getUsn());
        if (effectedRow > 0) {
            if (selectedItems.containsKey(purifier.getUsn())) {
                selectedItems.remove(purifier.getUsn());
            }
            // Updates store device from DB
            DiscoveryManager.getInstance().updateStoreDevices();
            saveLastPageCurrentPage();
            loadDataFromDatabase();
        }
    }

    private void removePurifierFromDiscoveredList(AirPurifier purifier) {
        if (purifier.getNetworkNode().getConnectionState() != ConnectionState.CONNECTED_LOCALLY) {
            DiscoveryManager.getInstance().removeFromDiscoveredList(purifier.getNetworkNode().getCppId());
        } else {
            DiscoveryManager.getInstance().updatePairingStatus(
                    purifier.getNetworkNode().getCppId(), NetworkNode.PAIRED_STATUS.NOT_PAIRED);
        }
    }

    private void removeCurrentPurifier(AirPurifier purifier){
        AirPurifier currentPurifier = AirPurifierManager.getInstance().getCurrentPurifier();
        if (currentPurifier == null) return;
        if (currentPurifier.getNetworkNode().getCppId().equals(purifier.getNetworkNode().getCppId())) {
            AirPurifierManager.getInstance().removeCurrentPurifier();
        }
    }

	@Override
	public void onPairingSuccess(NetworkNode networkNode) {/**NOP*/}

	@Override
	public void onPairingFailed(NetworkNode networkNode) {/**NOP*/}

	@Override
	public void onItemClickGoToAddPurifier() {
		List<AirPurifier> storePurifiers = DiscoveryManager.getInstance().updateStoreDevices();
		if (storePurifiers.size() >= AppConstants.MAX_PURIFIER_LIMIT) {
			showAlertDialog("",	getString(R.string.max_purifier_reached));
		} else {
			((MainActivity) getActivity()).showFragment(new StartFlowChooseFragment());
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.manage_purifier_info_img_btn) {
			MainActivity activity = (MainActivity) getActivity();
			if (activity != null) {
				activity.showFragment(new AboutFragment());
			}
		}
	}
}

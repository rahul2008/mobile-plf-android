package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.firmware.FirmwarePortInfo.FirmwareState;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareDownloadFragment extends BaseFragment implements AirPurifierEventListener {

	private ProgressBar progressBar;
	private FontTextView progressPercent;
	private Thread timerThread;
	private boolean downloaded;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.downloading_firmware, null);
		initViews(view);
		downloaded = false;
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_DOWNLOAD);
		FirmwareUpdateActivity.setCancelled(false);
		return view;
	}

	private void initViews(View view) {
		progressBar = (ProgressBar) view.findViewById(R.id.downloading_progressbar);
		progressPercent = (FontTextView) view.findViewById(R.id.progressbar_increasestatus);
		progressPercent.setText("0%");
		FontTextView downloadFirmwareTv = (FontTextView) view.findViewById(R.id.downloading_firmware_for_purifier_msg);
		downloadFirmwareTv.setText(getString(R.string.downloading_firmware_for_purifier_msg, ((FirmwareUpdateActivity) getActivity()).getPurifierName())) ;
		FontTextView currentVersion = (FontTextView) view.findViewById(R.id.current_version_number);
		currentVersion.setText(getString(R.string.firmware_current_version) + " " + ((FirmwareUpdateActivity) getActivity()).getCurrentVersion());
		FontTextView upgradeVersion = (FontTextView) view.findViewById(R.id.upgrade_version_number);
		upgradeVersion.setText(getString(R.string.firmware_new_version) + " " + ((FirmwareUpdateActivity) getActivity()).getUpgradeVersion());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		timerThread = new Thread(timerRunnable);
		timerThread.start();
		
		PurifierManager.getInstance().addAirPurifierEventListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		timerThread = null;
		
		PurifierManager.getInstance().removeAirPurifierEventListener(this);
	}

	private static int counter = 0;

	public static void setCounter(int counter) {
		FirmwareDownloadFragment.counter = counter;
	}

	Runnable timerRunnable = new Runnable() {
		public void run() {
			while(counter < 60 && !FirmwareUpdateActivity.isCancelled() && !downloaded) {
				ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$counter " + counter + " FirmwareUpdateActivity.isCancelled() " +FirmwareUpdateActivity.isCancelled());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				counter++;
			}
			if(counter >= 60) {
				ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$COUNT > 60 call failed fragment counter " + counter );
				((FirmwareUpdateActivity) getActivity()).setFirmwareUpdateJsonParams(FirmwareConstants.STATE, FirmwareConstants.CANCEL);
				if(((FirmwareUpdateActivity) getActivity()).getDownloadFailedCount() >= 3) {
					FirmwareUpdateActivity.setCancelled(true);
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.firmware_container, new FirmwareFailedSupportFragment(), FirmwareFailedSupportFragment.class.getSimpleName())
					.commit();
				} else {
					FirmwareUpdateActivity.setCancelled(true);
					int failCount = ((FirmwareUpdateActivity) getActivity()).getDownloadFailedCount();
					ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$failCount " + failCount);
					((FirmwareUpdateActivity) getActivity()).setDownloadFailedCount(++failCount);
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.firmware_container, new FirmwareDownloadFailedFragment(), FirmwareDownloadFailedFragment.class.getSimpleName())
					.commit();
				}
			}
		}
	};
	
	public void showNextFragment() {
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.firmware_container, new FirmwareInstallFragment(), FirmwareInstallFragment.class.getSimpleName())
		.commit();
	}

	@Override
	public void onAirPurifierEventReceived(AirPortInfo airPurifierEvent) {
		// NOP
	}

	@Override
	public void onFirmwareEventReceived(final FirmwarePortInfo firmwareEventDto) {
		ALog.d(ALog.FIRMWARE, "FirmwareDownloadFragment$firmwareEventReceived progress " + firmwareEventDto.getProgress());
		setCounter(0);
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				progressPercent.setText(firmwareEventDto.getProgress() + "%");
				progressBar.setProgress(firmwareEventDto.getProgress());
			}
		});
		
		if(firmwareEventDto.getProgress() == 100 && firmwareEventDto.getState() == FirmwareState.READY) {
			downloaded = true;
			showNextFragment();
		}
	}
}

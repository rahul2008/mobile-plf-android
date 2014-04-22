package com.philips.cl.di.dev.pa.firmware;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.datamodel.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.firmware.FirmwareConstants.FragmentID;
import com.philips.cl.di.dev.pa.firmware.FirmwareEventDto.FirmwareState;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.purifier.AirPurifierController;
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
		progressBar = (ProgressBar) view.findViewById(R.id.downloading_progressbar);
		FontTextView downloadFirmwareTv = (FontTextView) view.findViewById(R.id.downloading_firmware_for_purifier_msg);
		downloadFirmwareTv.setText(getString(R.string.downloading_firmware_for_purifier_msg, ((FirmwareUpdateActivity) getActivity()).getPurifierName())) ;
		
		downloaded = false;
		progressPercent = (FontTextView) view.findViewById(R.id.progressbar_increasestatus);
		progressPercent.setText("0%");
		((FirmwareUpdateActivity) getActivity()).setActionBar(FragmentID.FIRMWARE_DOWNLOAD);
		FirmwareUpdateActivity.setCancelled(false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		timerThread = new Thread(timerRunnable);
		timerThread.start();
		
		AirPurifierController.getInstance().addAirPurifierEventListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		timerThread = null;
		
		AirPurifierController.getInstance().removeAirPurifierEventListener(this);
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
				((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally(FirmwareConstants.STATE, FirmwareConstants.CANCEL);
				if(((FirmwareUpdateActivity) getActivity()).getDownloadFailedCount() >= 3) {
					FirmwareUpdateActivity.setCancelled(true);
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.firmware_container, new FirmwareFailedSupportFragment(), FirmwareConstants.FIRMWARE_FAILED_SUPPORT_FRAGMENT)
					.commit();
				} else {
					FirmwareUpdateActivity.setCancelled(true);
					int failCount = ((FirmwareUpdateActivity) getActivity()).getDownloadFailedCount();
					ALog.i(ALog.FIRMWARE, "FirmwareDownloadFragment$failCount " + failCount);
					((FirmwareUpdateActivity) getActivity()).setDownloadFailedCount(++failCount);
					getFragmentManager()
					.beginTransaction()
					.replace(R.id.firmware_container, new FirmwareDownloadFailedFragment(), FirmwareConstants.FIRMWARE_DOWNLOAD_FAILED_FRAGMENT)
					.commit();
				}
			}
		}
	};
	
	public void showNextFragment() {
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.firmware_container, new FirmwareInstallFragment(), FirmwareConstants.FIRMWARE_INSTALL_FRAGMENT)
		.commit();
	}

	@Override
	public void airPurifierEventReceived(AirPurifierEventDto airPurifierEvent) {
		// NOP
	}

	@Override
	public void firmwareEventReceived(final FirmwareEventDto firmwareEventDto) {
		ALog.d(ALog.FIRMWARE, "FirmwareDownloadFragment$firmwareEventReceived progress " + firmwareEventDto.getProgress());
		counter = 0;
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				progressPercent.setText(firmwareEventDto.getProgress() + "%");
				progressBar.setProgress(firmwareEventDto.getProgress());
			}
		});
		
		if(firmwareEventDto.getProgress() == 100 && firmwareEventDto.getState() == FirmwareState.READY) {
			downloaded = true;
			((FirmwareUpdateActivity) getActivity()).setDeviceDetailsLocally(FirmwareConstants.STATE, FirmwareConstants.GO);
			showNextFragment();
		}
	}
}

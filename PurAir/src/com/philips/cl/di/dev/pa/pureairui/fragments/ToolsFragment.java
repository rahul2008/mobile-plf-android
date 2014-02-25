package com.philips.cl.di.dev.pa.pureairui.fragments;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.controller.CPPController;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseAdapter;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseModel;
import com.philips.cl.di.dev.pa.interfaces.SignonListener;
import com.philips.cl.di.dev.pa.network.TaskGetDiagnosticData;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.utils.Utils;

public class ToolsFragment extends Fragment implements OnClickListener,
SignonListener {

	private static final String TAG = ToolsFragment.class.getSimpleName();
	private TextView tvIpaddress;
	private Button submitButton;
	private View vMain;

	private EditText tvRegId;
	private Button signOnButton;

	private TextView tvCPPDetails;

	private CppDatabaseModel cppDatabaseModel;

	private static final String WIFI = "wifi";
	private static final String WIFIUI = "wifiui";
	private static final String DEVICE = "device";
	private static final String LOG = "log";
	private WifiManager wifii;
	private DhcpInfo dhcpInfo;
	private String[] aResponse;
	private String[] header = new String[] { "Wifi Port:", "WifiUi Port:",
			"Device Port:", "Firmware Port:", "Logs Port:" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vMain = (View) inflater.inflate(R.layout.tools_fragment, container,
				false);
		initViews();
		return vMain;
	}

	private void initViews() {
		tvIpaddress = (EditText) vMain.findViewById(R.id.tvipaddress);
		tvIpaddress.setText(Utils.getIPAddress(getActivity()));
		submitButton = (Button) vMain.findViewById(R.id.submitButton);
		submitButton.setOnClickListener(this);

		tvRegId = (EditText) vMain.findViewById(R.id.tv_cpp_regId_txt);
		signOnButton = (Button) vMain.findViewById(R.id.get_cpp_btn);
		signOnButton.setOnClickListener(this);

		tvCPPDetails = (TextView) vMain.findViewById(R.id.tv_cpp_details);

		if (Utils.getAirPurifierID(getActivity()) != null
				&& Utils.getAirPurifierID(getActivity()).length() > 0) {
			tvCPPDetails.setVisibility(View.VISIBLE);
			signOnButton.setText("Reset");

			tvCPPDetails.setText("AirPurifier ID: "
					+ Utils.getAirPurifierID(getActivity()));
			String regId = Utils.getRegistrationID(getActivity());
			if (regId != null && regId.length() > 0) {
				tvRegId.setText(regId.substring(regId.length() - 5));
			}

		}

		Button diagnostics = (Button) vMain.findViewById(R.id.btn_diagnostics);
		diagnostics.setOnClickListener(this);

	}

	private ProgressDialog dialog;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitButton:
			Log.i(TAG, "Submit button");
			Utils.setIPAddress(tvIpaddress.getText().toString(), getActivity());
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getWindow()
					.getCurrentFocus().getWindowToken(), 0);
			break;
		case R.id.get_cpp_btn:
			Log.i(TAG, "Sign on button");
			if (signOnButton.getText().toString().equals("Reset")) {
				((MainActivity) getActivity()).stopCPPPolling();
				Utils.clearCPPDetails(getActivity());
				tvCPPDetails.setText("");
				signOnButton.setText("Sign On");
				tvRegId.setText("");
			} else {
				String regStr = tvRegId.getText().toString();
				if (regStr != null && regStr.length() == 5) {
					CppDatabaseAdapter cppDatabaseAdapter = new CppDatabaseAdapter(
							getActivity());
					cppDatabaseAdapter.open();
					cppDatabaseModel = cppDatabaseAdapter.getCppInfo(regStr);
					if (cppDatabaseModel != null) {
						dialog = new ProgressDialog(getActivity());
						dialog.setMessage("Please wait...");
						dialog.setCancelable(false);
						dialog.show();

						Utils.storeCPPKeys(getActivity(), cppDatabaseModel);
						CPPController.getInstance(getActivity())
						.addSignonListener(this);
						CPPController.getInstance(getActivity()).init();
					} else {
						Toast.makeText(getActivity(), "Invalid Key",
								Toast.LENGTH_LONG).show();
					}
				}
			}
			break;
		case R.id.btn_diagnostics:
			String message = diagnosticData();
			sendMail(message, "permita.chatterjee@philips.com");
			break;

		default:
			break;
		}

	}

	private boolean isSignon;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (getActivity() != null) {
				if (dialog.isShowing()) {
					dialog.cancel();
				}
				if (isSignon) {
					signOnButton.setText("Reset");
					tvCPPDetails.setVisibility(View.VISIBLE);
					tvCPPDetails.setText("AirPurifier: "
							+ cppDatabaseModel.getDistribution());
					((MainActivity) getActivity()).toggleConnection(false);
					Toast.makeText(getActivity(), "Signon Successfull",
							Toast.LENGTH_LONG).show();
				} else {
					Utils.clearCPPDetails(getActivity());
					Toast.makeText(getActivity(), "Signon failed",
							Toast.LENGTH_LONG).show();
				}
			}
		};
	};

	@Override
	public void signonStatus(boolean isSigonSuccess) {
		this.isSignon = isSigonSuccess;
		handler.sendEmptyMessage(0);
	}

	public void sendMail(String message, String sendTo) {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { sendTo });
		email.putExtra(Intent.EXTRA_SUBJECT, "Diagnostics");
		email.putExtra(Intent.EXTRA_TEXT, message);
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Send this mail via:"));
	}

	/**
	 * Returns MAC address of the given interface name.
	 * 
	 * @param ipAddress
	 *            to get the interfaceName
	 * @return mac address or empty string
	 */
	public static String getMACAddress(String ipAddress) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					String ipAddr = addr.getHostAddress();
					if ((ipAddr).compareTo(ipAddress) == 0) {
						byte[] mac = intf.getHardwareAddress();
						if (mac == null)
							return "";
						StringBuilder buf = new StringBuilder();
						for (int idx = 0; idx < mac.length; idx++)
							buf.append(String.format("%02X:", mac[idx]));
						if (buf.length() > 0)
							buf.deleteCharAt(buf.length() - 1);
						return buf.toString();
					}
				}
			}
		} catch (Exception ex) {
		}
		return "";
	}

	/**
	 * Fetches all required diagnostic data
	 * 
	 * @return String containing all diagnostic data
	 */
	public String diagnosticData() {
		String firmwareUrl = String.format(AppConstants.URL_FIRMWARE_PORT,
				Utils.getIPAddress(getActivity()));
		String wifiUrl = String.format(AppConstants.URL_PORT.concat(WIFI),
				Utils.getIPAddress(getActivity()));
		String wifiUiUrl = String.format(AppConstants.URL_PORT.concat(WIFIUI),
				Utils.getIPAddress(getActivity()));
		String deviceUrl = String.format(AppConstants.URL_PORT.concat(DEVICE),
				Utils.getIPAddress(getActivity()));
		String logUrl = String.format(AppConstants.URL_PORT.concat(LOG),
				Utils.getIPAddress(getActivity()));

		TaskGetDiagnosticData task = new TaskGetDiagnosticData(getActivity());
		task.execute(wifiUrl, wifiUiUrl, deviceUrl, firmwareUrl, logUrl);
		try {
			aResponse = task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		wifii = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		dhcpInfo = wifii.getDhcpInfo();

		CPPController controller = CPPController.getInstance(getActivity());

		String dDns1 = "DNS: " + intToIp(dhcpInfo.dns1);
		String dGateway = "Default Gateway: " + intToIp(dhcpInfo.gateway);
		String dIpAddress = "IP Address: " + intToIp(dhcpInfo.ipAddress);
		String dMacaddress = "MAC Address:"
				+ getMACAddress(intToIp(dhcpInfo.ipAddress));
		String registrationId = "Registration Id: "
				+ Utils.getRegistrationID(getActivity());
		String airpurifierIpAddress = "AirPurifier IpAddress:"
				+ Utils.getIPAddress(getActivity());
		String euid = "AirPurifier EUI64:" + Utils.getEuid(getActivity());
		String macAddress = "Air Purifier Mac Address:"
				+ Utils.getMacAddress(getActivity());
		/*String iCPClientVersion= "ICP Client version:"+
				controller.getICPClientVersion();*/
		String isSignOn = "Is SignOn Successful: " + controller.isSignOn();
		StringBuilder portData = new StringBuilder();
		if (aResponse != null) {

			for (int i = 0; i < aResponse.length; i++) {
				portData.append(header[i]);
				portData.append("\n");
				portData.append(aResponse[i]);
				portData.append("\n");
			}
			Log.d("Diagnostic", "Port: " + portData.toString());
		}
		Log.d("Diagnostic", "Device Network Info\n" + dIpAddress + "\n"
				+ dMacaddress + "\n" + dDns1 + "\n" + dGateway + "\n"
				+ registrationId + "\n" + airpurifierIpAddress + "\n" + euid
				+ "\n" + macAddress + "\n" + isSignOn);

		StringBuilder data = new StringBuilder("Device Network Info\n");
		data.append(dIpAddress);
		data.append("\n");
		data.append(dMacaddress);
		data.append("\n");
		data.append(dGateway);
		data.append("\n");
		data.append(dDns1);
		data.append("\n");
		data.append(registrationId);
		data.append("\n");
		data.append(airpurifierIpAddress);
		data.append("\n");
		data.append(euid);
		data.append("\n");
		data.append(macAddress);
		data.append("\n");
		data.append(isSignOn);
		data.append("\n");
		// data.append(ICPClientVersion);
		// data.append("\n");
		data.append(portData);

		return data.toString();
	}

	private String formatMacAddress(byte[] mac) {
		StringBuilder buf = new StringBuilder();
		for (int idx = 0; idx < mac.length; idx++)
			buf.append(String.format("%02X:", mac[idx]));
		if (buf.length() > 0)
			buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	private String intToIp(int addr) {
		return ((addr & 0xFF) + "." + ((addr >>>= 8) & 0xFF) + "."
				+ ((addr >>>= 8) & 0xFF) + "." + ((addr >>>= 8) & 0xFF));
	}

}

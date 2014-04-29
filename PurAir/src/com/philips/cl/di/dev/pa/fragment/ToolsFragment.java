package com.philips.cl.di.dev.pa.fragment;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.cpp.SignonListener;
import com.philips.cl.di.dev.pa.cpptemp.CppDatabaseAdapter;
import com.philips.cl.di.dev.pa.cpptemp.CppDatabaseModel;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.TaskGetDiagnosticData;
import com.philips.cl.di.dev.pa.purifier.TaskGetDiagnosticData.DiagnosticsDataListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class ToolsFragment extends BaseFragment implements OnClickListener,
SignonListener, DiagnosticsDataListener {

	private static final String TAG = ToolsFragment.class.getSimpleName();
	
	private PurAirDevice mPurifier;
	
	private TextView tvIpaddress;
	private Button submitButton;
	private View vMain;
	private EditText tvRegId;
	private Button signOnButton;
	private TextView tvCPPDetails;
	private CppDatabaseModel cppDatabaseModel;
	private String[] header = new String[] { "Wifi Port:", "WifiUi Port:",
			"Device Port:", "Firmware Port:", "Logs Port:" };
	private char lineSeparator='\n';

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mPurifier = PurifierManager.getInstance().getCurrentPurifier();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vMain = inflater.inflate(R.layout.tools_fragment, container,
				false);
		initViews();
		return vMain;
	}


	private void initViews() {
		tvIpaddress = (EditText) vMain.findViewById(R.id.tvipaddress);
		tvIpaddress.setText(getPurifierIpAddress());
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

		if( Utils.getAirPurifierID(getActivity()) != null && 
				Utils.getAirPurifierID(getActivity()).length() > 0 ) {
			signOnButton.setVisibility(View.INVISIBLE) ;
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
			PurAirDevice dummyDevice = new PurAirDevice(null, null, tvIpaddress.getText().toString(), "ToolsPurifier", null, ConnectionState.CONNECTED_LOCALLY);
			PurifierManager.getInstance().setCurrentPurifier(dummyDevice);
			
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getWindow()
					.getCurrentFocus().getWindowToken(), 0);
			break;
		case R.id.get_cpp_btn:
			Log.i(TAG, "Sign on button");
			if (signOnButton.getText().toString().equals("Reset")) {
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
						//CPPController.getInstance(getActivity()).init();
					} else {
						Toast.makeText(getActivity(), "Invalid Key",
								Toast.LENGTH_LONG).show();
					}
				}
			}
			break;
		case R.id.btn_diagnostics:
			((MainActivity)getActivity()).isDiagnostics=true;			
			String firmwareUrl = Utils.getPortUrl(Port.FIRMWARE, getPurifierIpAddress());
			String wifiUrl = Utils.getPortUrl(Port.WIFI, getPurifierIpAddress());
			String wifiUiUrl = Utils.getPortUrl(Port.WIFIUI, getPurifierIpAddress());
			String deviceUrl = Utils.getPortUrl(Port.DEVICE, getPurifierIpAddress());
			String logUrl = Utils.getPortUrl(Port.LOG, getPurifierIpAddress());

			//fetch all ports data
			TaskGetDiagnosticData task = new TaskGetDiagnosticData(getActivity(), this);
			task.execute(wifiUrl, wifiUiUrl, deviceUrl, firmwareUrl, logUrl);
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
					//((MainActivity) getActivity()).toggleConnection(false);
					Toast.makeText(getActivity(), "Signon Successfull",
							Toast.LENGTH_LONG).show();
					signOnButton.setVisibility(View.INVISIBLE) ;
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
		isSignon = isSigonSuccess;
		handler.sendEmptyMessage(0);
	}

	public void sendMail(String message, String sendTo) {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { sendTo });
		email.putExtra(Intent.EXTRA_SUBJECT, "Smart Air Diagnostics");
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
	public String getMACAddress(String ipAddress) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					String ipAddr = addr.getHostAddress();
					if (ipAddr.compareTo(ipAddress) == 0) {
						byte[] mac = intf.getHardwareAddress();
						if (mac == null){
							return "";
						}
						StringBuilder buf = new StringBuilder();
						for (int idx = 0; idx < mac.length; idx++)
							buf.append(String.format("%02X:", mac[idx]));
						if (buf.length() > 0){
							buf.deleteCharAt(buf.length() - 1);
						}
						return buf.toString();						
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	/**
	 * Fetches all required diagnostic data
	 * 
	 * @return String containing all diagnostic data
	 */
	public String diagnosticData() {

		WifiManager wifii = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		DhcpInfo dhcpInfo = wifii.getDhcpInfo();

		CPPController controller = CPPController.getInstance(getActivity());

		//get device dignostics info
		String appVersion= "App Version: "+((MainActivity) getActivity()).getVersionNumber();
		String dDns1 = "DNS: " + intToIp(dhcpInfo.dns1);
		String dGateway = "Default Gateway: " + intToIp(dhcpInfo.gateway);
		String dIpAddress = "IP Address: " + intToIp(dhcpInfo.ipAddress);
		String dMacaddress = "MAC Address:"
				+ getMACAddress(intToIp(dhcpInfo.ipAddress));
		String registrationId = Utils.getRegistrationID(getActivity());
		if (registrationId != null && registrationId.length() > 0) {
			registrationId = "Registration Id: "
					+ registrationId.substring(registrationId.length() - 5);
		}

		//get AirPurifier diagnostics info
		String airpurifierIpAddress = "AirPurifier IpAddress:" + getPurifierIpAddress();
		String euid = "AirPurifier EUI64:" + Utils.getAirPurifierID(getActivity());
		String macAddress = SessionDto.getInstance().getPurifierMacAddress();
		macAddress = "Air Purifier MAC Address:"
				+ formatMacAddress(macAddress);

		String iCPClientVersion= "ICP Client version:"+
				controller.getICPClientVersion();

		String isSignOn = "Is SignOn Successful: " + controller.isSignOn();

		StringBuilder data = new StringBuilder("Device Network Info\n");
		data.append(appVersion);
		data.append(lineSeparator);
		data.append(dIpAddress);
		data.append(lineSeparator);
		data.append(dMacaddress);
		data.append(lineSeparator);
		data.append(dGateway);
		data.append(lineSeparator);
		data.append(dDns1);
		data.append(lineSeparator);
		data.append(registrationId);
		data.append(lineSeparator);
		data.append("AirPurifier Network Info\n");
		data.append(airpurifierIpAddress);
		data.append(lineSeparator);
		data.append(euid);
		data.append(lineSeparator);
		data.append(macAddress);
		data.append(lineSeparator);
		data.append(isSignOn);
		data.append(lineSeparator);
		data.append(iCPClientVersion);
		data.append(lineSeparator);
		return data.toString();
	}

	//formats mac to be like 00:15:5D:03:8D:01
	private String formatMacAddress(String mac) {
		if( mac == null) {
			return "" ;
		}
		return mac.replaceAll("(..)(?!$)", "$1-");
	}

	private String intToIp(int addr) {
		return ((addr & 0xFF) + "." + ((addr >>>= 8) & 0xFF) + "."
				+ ((addr >>>= 8) & 0xFF) + "." + ((addr >>>= 8) & 0xFF));
	}


	@Override
	public void diagnosticsDataUpdated(String[] data) {
		StringBuilder portData = new StringBuilder();
		String message = diagnosticData();
		portData.append(message);
		portData.append(lineSeparator);
		if (data != null) {			
			for (int i = 0; i < data.length; i++) {
				portData.append(header[i]);
				portData.append(lineSeparator);
				portData.append(data[i]);
				portData.append(lineSeparator);
			}			
		}
		Log.d("Diagnostic", portData.toString());
		sendMail(portData.toString(), "sangamesh.bn@philips.com");
	}
	
	private String getPurifierIpAddress() {
		if (mPurifier == null) {
			return "< unknown >"; // TODO localize
		}
		return mPurifier.getIpAddress();
	}

}

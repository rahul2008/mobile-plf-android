/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclientsample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.WifiPort;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommClientWrapper;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private DiscoveryManager<GenericAppliance> dm;
	private ListView appliancesListView;
	private ArrayAdapter<DICommAppliance> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appliancesListView = (ListView) findViewById(R.id.lv_appliances);
		adapter = new ArrayAdapter<DICommAppliance>(this,
				android.R.layout.simple_list_item_1) {

			public android.view.View getView(int position,
					android.view.View convertView, android.view.ViewGroup parent) {
				TextView tv = new TextView(MainActivity.this);
				tv.setText(this.getItem(position).getName());
				return tv;
			};
		};
		appliancesListView.setAdapter(adapter);
		appliancesListView.setOnItemClickListener(applianceClickListener);
	}
	
	@Override
	protected void onPause() {
		dm.stop();
		super.onPause();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		dm = (DiscoveryManager<GenericAppliance>) DiscoveryManager.getInstance();
		dm.addDiscoveryEventListener(discoveryListener);
		dm.start();
	}

	private DICommPortListener wifiPortListener = new DICommPortListener() {
		
		@Override
		public void onPortUpdate(DICommPort<?> port) {
			
			WifiPortProperties portProperties = ((WifiPort)port).getPortProperties();
			if (portProperties != null) {
				Log.d(TAG, String.format("WifiPortProperties: ipaddress=%s", portProperties.getIpaddress()));
			}
		}
		
		@Override
		public void onPortError(DICommPort<?> port, Error error,
				String errorData) {
		}
	};
	
	private DiscoveryEventListener discoveryListener = new DiscoveryEventListener() {

		@Override
		public void onDiscoveredAppliancesListChanged() {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					adapter.clear();
					adapter.addAll(dm.getAllDiscoveredAppliances());
				}
			});
			
			for (DICommAppliance appliance: dm.getAllDiscoveredAppliances()) {
				appliance.getWifiPort().addPortListener(wifiPortListener);
			}
		}
	};
	
	private OnItemClickListener applianceClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			WifiPortProperties portProperties = adapter.getItem(position).getWifiPort().getPortProperties();
			if (portProperties != null) {
				Log.d(TAG, String.format("WifiPortProperties: ipaddress=%s", portProperties.getIpaddress()));
			}
		}
	};
}

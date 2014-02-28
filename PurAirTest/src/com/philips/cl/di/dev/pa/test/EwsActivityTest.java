package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Method;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSDialogFactory;
import com.philips.cl.di.dev.pa.ews.EwsActivity;
import com.philips.cl.disecurity.DISecurity;

public class EwsActivityTest extends ActivityInstrumentationTestCase2<EwsActivity> {
	
	private EwsActivity activity;

	public EwsActivityTest() {
		super(EwsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}
	
	public void testIntroScreen() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_intro_screen, null);
		Button button = (Button) view.findViewById(R.id.ews_get_start_btn);

        assertEquals(true, button.isClickable());
        assertEquals("Get Started", button.getText().toString());

	}
	
	public void testActionBar() {
		Button button = (Button) activity.findViewById(R.id.ews_actionbar_cancel_btn);

        assertEquals(true, button.isClickable());
        assertEquals("Cancel", button.getText().toString());

	}
	
	public void testStep1Screen() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_step1, null);
        Button button = (Button) view.findViewById(R.id.ews_step1_yes_btn);
        assertEquals(true, button.isClickable());
        assertEquals("Yes", button.getText().toString());

        button = (Button) view.findViewById(R.id.ews_step1_no_btn);
        assertEquals(true, button.isClickable());
        assertEquals("No", button.getText().toString());

	}
	
	public void testStep2Screen() {
		 View view = activity.getLayoutInflater().inflate(R.layout.ews_step2, null);
         Button button = (Button) view.findViewById(R.id.ews_step2_yes_btn);
         assertEquals(true, button.isClickable());
         assertEquals("Yes", button.getText().toString());

         button = (Button) view.findViewById(R.id.ews_step2_no_btn);
         assertEquals(true, button.isClickable());
         assertEquals("No", button.getText().toString());

	}
	
	public void testStep3Screen() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_step3, null);
        Button button = (Button) view.findViewById(R.id.ews_step3_next_btn);
        assertEquals(true, button.isClickable());
        assertEquals("Next", button.getText().toString());

        button = (Button) view.findViewById(R.id.ews_step3_edit_name_btn);
        assertEquals(true, button.isClickable());

	}
	
	public void testCongratulationScreen() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_congratulation, null);
        Button button = (Button) view.findViewById(R.id.ews_congratulation_btn);
        assertEquals(true, button.isClickable());
        assertEquals("Start using your Philips Smart Air Purifier", button.getText().toString());
	}
	
	public void testPurifierNotDetect() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_error_purifier_not_detect, null);
        Button button = (Button) view.findViewById(R.id.ews_purifier_not_dect_btn);
        assertEquals(true, button.isClickable());
        assertEquals("Try again", button.getText().toString());
	}
	
	public void testSupportScreen() {
		View view = activity.getLayoutInflater().inflate(R.layout.contact_philips_support, null);
        assertEquals(true, ((RelativeLayout) view.findViewById(R.id.contact_support_phone_layout)).isClickable());
        assertEquals(true, ((RelativeLayout) view.findViewById(R.id.contact_support_email_layout)).isClickable());
        assertEquals(true, ((RelativeLayout) view.findViewById(R.id.contact_support_website_layout)).isClickable());
	}
	
	public void testGetDialog() {
		EWSDialogFactory ewsDialogFactory = EWSDialogFactory.getInstance(activity);
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.SUPPORT_TS01));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.SUPPORT_TS02));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.SUPPORT_TS03));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.SUPPORT_TS05));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.ERROR_TS01_01));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.ERROR_TS01_02));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.ERROR_TS01_03));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.ERROR_TS01_04));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.ERROR_TS01_05));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.CANCEL_WIFI_SETUP));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.CHECK_SIGNAL_STRENGTH));
		assertNotNull(ewsDialogFactory.getDialog(EWSDialogFactory.CONNECTING_TO_PRODUCT));
		
	}
	
	public void testCheckWifiConnectivity() {
		
		try {
			Method checkWifiConnectivityMethod = DISecurity.class.getDeclaredMethod("checkWifiConnectivity", (Class<?>[])null);
			checkWifiConnectivityMethod.setAccessible(true);
			checkWifiConnectivityMethod.invoke(activity, (Object[])null);
			ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			Log.i("wifi", "mWifi.isConnected()== " +mWifi.isConnected());
			if(!mWifi.isConnected()) {
				View view = activity.getLayoutInflater().inflate(R.layout.ews_connect_2_your_network, null);
				assertEquals(true, view.isInLayout());
			}
				
		} catch (Exception e) {
			
		}
		
		
	}
 
}

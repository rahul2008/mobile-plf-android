package com.philips.cl.di.dev.pa.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.ews.EWSActivity;
import com.philips.cl.di.dev.pa.ews.SetupDialogFactory;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class EwsActivityTest extends ActivityInstrumentationTestCase2<EWSActivity> {
	
	private EWSActivity activity;

	public EwsActivityTest() {
		super(EWSActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		activity = getActivity();
	}
	
	public void testIntroScreen() {
		View view = activity.getLayoutInflater().inflate(R.layout.setup_intro_screen, null);
		Button button = (Button) view.findViewById(R.id.setup_get_start_btn);
		FontTextView tv = (FontTextView) view.findViewById(R.id.setup_intro_message1_txt);
		FontTextView tv2 = (FontTextView) view.findViewById(R.id.setup_intro_message2_txt);
		
        assertEquals(true, button.isClickable());
        assertEquals("Get started", button.getText().toString());
        assertEquals("Follow these 4 easy steps to connect your Philips Smart Air Purifier to your home Wi-Fi and pair with your phone.", tv.getText().toString());
        assertEquals("For a successful Wi-Fi connection, position the Philips Smart Air Purifier within reach of your router. Obstacles such as walls may affect the Wi-Fi performance.", tv2.getText().toString());
	}
	
	
	public void testStep1Screen() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_step1, null);
        Button button = (Button) view.findViewById(R.id.ews_step1_yes_btn);
        assertEquals(true, button.isClickable());
        assertEquals("Yes", button.getText().toString());

        button = (Button) view.findViewById(R.id.ews_step1_no_btn);
        assertEquals(true, button.isClickable());
        assertEquals("No", button.getText().toString());
        
        FontTextView tv = (FontTextView) view.findViewById(R.id.ews_step1_instruction);
        assertEquals("Confirm your Wi-Fi connection.", tv.getText().toString());

	}
	
	public void testActionBar() {
		Button button = (Button) activity.findViewById(R.id.setup_actionbar_cancel_btn);

        assertEquals(true, button.isClickable());
        assertEquals("Cancel", button.getText().toString());

	}
	
	public void testStep2Screen() {
		 View view = activity.getLayoutInflater().inflate(R.layout.setup_step2, null);
         Button button = (Button) view.findViewById(R.id.setup_step2_yes_btn);
         assertEquals(true, button.isClickable());
         assertEquals("Yes", button.getText().toString());

         button = (Button) view.findViewById(R.id.setup_step2_no_btn);
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
		View view = activity.getLayoutInflater().inflate(R.layout.setup_congratulation, null);
        Button button = (Button) view.findViewById(R.id.finish_congratulation_btn);
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
		SetupDialogFactory ewsDialogFactory = SetupDialogFactory.getInstance(activity);
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.SUPPORT_TS01));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.SUPPORT_TS02));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.SUPPORT_TS04));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.SUPPORT_TS05));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.ERROR_TS01_01));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.ERROR_TS01_02));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.ERROR_TS01_03));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.ERROR_TS01_04));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.ERROR_TS01_05));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.CANCEL_WIFI_SETUP));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.CONNECTING_TO_PRODUCT));
		
	}
	
	/**
	 * SSDP
	 *//*
	public void testSsdpDiscoveryStart() {
		SsdpService ssdpService = SsdpService.getInstance();
		ssdpService.startDeviceDiscovery(activity);
		DiscoveryServiceState mServiceState = null;
		Field keysField;
		try {
			keysField = SsdpService.class.getDeclaredField("mServiceState");
			keysField.setAccessible(true);
			mServiceState = (DiscoveryServiceState) keysField.get(ssdpService);
			
			
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		if (mServiceState != null) {
			assertEquals(DiscoveryServiceState.STARTED, mServiceState);
		} else {
			assertNull(mServiceState);
		}
		
	}
	
	public void testSsdpDiscoveryStop() {
		SsdpService ssdpService = SsdpService.getInstance();
		ssdpService.stopDeviceDiscovery();
		Field keysField;
		DiscoveryServiceState mServiceState = null;
		try {
			keysField = SsdpService.class.getDeclaredField("mServiceState");
			keysField.setAccessible(true);
			mServiceState = (DiscoveryServiceState) keysField.get(ssdpService);
			
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail(e.getMessage());		
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		if (mServiceState != null) {
			assertEquals(DiscoveryServiceState.STOPPED, mServiceState);
		} else {
			assertNull(mServiceState);
		}
		
	}
	
	
	public void testGetAliveDevice() {
		
		SsdpService.getInstance().startDeviceDiscovery(activity);
		
		Set<DeviceModel> mAliveDevices = new DeviceListModel().getAliveDevices();
		
		Iterator<DeviceModel> iterator = mAliveDevices.iterator();
		
		if (!iterator.hasNext()) {
			assertFalse(iterator.hasNext());
		}
		
		while (iterator.hasNext()) {
			DeviceModel deviceModel = iterator.next();
			if (deviceModel.getSsdpDevice() != null) {
				if (deviceModel.getSsdpDevice().getModelName().contains("AirPurifier")) {
					assertNotNull(deviceModel.getSsdpDevice().getFriendlyName());
					assertNotNull(deviceModel.getSsdpDevice().getCppId());
					assertNotNull(deviceModel.getIpAddress());
					assertNotNull(deviceModel.getBootID());
				} else {
					assertFalse(deviceModel.getSsdpDevice().getModelName().contains("AirPurifier"));
				}
				
			} else {
				assertNull(deviceModel.getSsdpDevice());
			}
		}
	}*/
 
}

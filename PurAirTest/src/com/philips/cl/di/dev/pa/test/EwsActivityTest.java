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
        assertEquals(activity.getString(R.string.get_start), button.getText().toString());
        assertEquals(activity.getString(R.string.ews_intro_message1_str), tv.getText().toString());
        assertEquals(activity.getString(R.string.ews_intro_message2_str), tv2.getText().toString());
	}
	
	
	public void testStep1Screen() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_step1, null);
        Button button = (Button) view.findViewById(R.id.ews_step1_yes_btn);
        assertEquals(true, button.isClickable());
        assertEquals(activity.getString(R.string.yes), button.getText().toString());

        button = (Button) view.findViewById(R.id.ews_step1_no_btn);
        assertEquals(true, button.isClickable());
        assertEquals(activity.getString(R.string.no), button.getText().toString());
        
        FontTextView tv = (FontTextView) view.findViewById(R.id.ews_step1_instruction);
        assertEquals(activity.getString(R.string.step1_instruction), tv.getText().toString());
	}
	
	/*public void testActionBar() {
		Button button = (Button) activity.findViewById(R.id.setup_actionbar_cancel_btn);

        assertEquals(true, button.isClickable());
        assertEquals(activity.getString(R.string.cancel), button.getText().toString());
	}*/
	
	public void testStep2Screen() {
		 View view = activity.getLayoutInflater().inflate(R.layout.ews_steps_2_3, null);
         Button button = (Button) view.findViewById(R.id.setup_step2_yes_btn);
         assertEquals(true, button.isClickable());
         assertEquals(activity.getString(R.string.yes), button.getText().toString());

         button = (Button) view.findViewById(R.id.setup_step2_no_btn);
         assertEquals(true, button.isClickable());
         assertEquals(activity.getString(R.string.no), button.getText().toString());
	}
	
	public void testStep3Screen() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_step4, null);
        Button button = (Button) view.findViewById(R.id.ews_step3_next_btn);
        assertEquals(true, button.isClickable());
        assertEquals(activity.getString(R.string.next), button.getText().toString());

        button = (Button) view.findViewById(R.id.ews_step3_edit_name_btn);
        assertEquals(true, button.isClickable());
	}
	
	public void testCongratulationScreen() {
		View view = activity.getLayoutInflater().inflate(R.layout.setup_congratulation, null);
        Button button = (Button) view.findViewById(R.id.finish_congratulation_btn);
        assertEquals(true, button.isClickable());
        assertEquals(activity.getString(R.string.congratulation_btn_txt), button.getText().toString());
	}
	
	public void testPurifierNotDetect() {
		View view = activity.getLayoutInflater().inflate(R.layout.ews_error_purifier_not_detect, null);
        Button button = (Button) view.findViewById(R.id.ews_purifier_not_dect_btn);
        assertEquals(true, button.isClickable());
        assertEquals(activity.getString(R.string.error_purifier_not_detect_btn_txt), button.getText().toString());
	}
	
	public void testSupportScreen() {
		View view = activity.getLayoutInflater().inflate(R.layout.contact_philips_support, null);
        assertEquals(true, ((RelativeLayout) view.findViewById(R.id.contact_support_phone_layout)).isClickable());
        assertEquals(true, ((RelativeLayout) view.findViewById(R.id.contact_support_email_layout)).isClickable());
        assertEquals(true, ((RelativeLayout) view.findViewById(R.id.contact_support_website_layout)).isClickable());
	}
	
	public void testGetDialog() {
		SetupDialogFactory ewsDialogFactory = SetupDialogFactory.getInstance(activity);
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.SUPPORT_UNPLUG_PURIFIER));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.SUPPORT_WIFI_LED_ORANGE));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.SUPPORT_TS03));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.SUPPORT_TS05));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.ERROR_TS01_01));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.CHECK_SIGNAL_STRENGTH));
		assertNotNull(ewsDialogFactory.getDialog(SetupDialogFactory.CONNECTING_TO_PRODUCT));
	}
	
}

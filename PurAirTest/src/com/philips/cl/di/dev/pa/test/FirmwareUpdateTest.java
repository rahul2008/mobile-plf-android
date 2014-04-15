package com.philips.cl.di.dev.pa.test;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.firmware.FirmwareDownloadFragment;
import com.philips.cl.di.dev.pa.firmware.FirmwareInstallFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

//import static org.junit.Assert.assertThat;
//import org.hamcrest.Matchers;

public class FirmwareUpdateTest extends ActivityInstrumentationTestCase2<MainActivity>  {
		
	private MainActivity activity;
	private Context mCtx;
	
	//Constructors
	public FirmwareUpdateTest(Class<MainActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}	
	
	public FirmwareUpdateTest() {
	    super(MainActivity.class);
	}

	public FirmwareUpdateTest(Context context) {
	    super(MainActivity.class);
	    mCtx = context;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();	
		setActivityInitialTouchMode(false);
		activity = getActivity();
		mCtx = this.getInstrumentation().getTargetContext().getApplicationContext();
	}
	
	public void onPause() {     
		this.onPause();  
		} 
	
	//********************************************** Test Cases for Entry Criteria ************************************************//
	
	/*  
	 * Class Name  : NA
	 * TestCase ID :
	 */
	
	//Test Case ID: 1
	public void testFirmwareAvailableNotificationForHomeScreen() {
		try {
			
			LinearLayout llfirmwareUpdAvailable = (LinearLayout) activity.findViewById(R.id.firmware_update_available);
			FontTextView lblfirmwareUpdAvailable = (FontTextView) activity.findViewById(R.id.lbl_firmware_update_available);
			ImageButton btnfirmwareUpdAvailable = (ImageButton) activity.findViewById(R.id.btn_firmware_update_available);
			
			assertEquals(false, llfirmwareUpdAvailable.isShown());
			assertEquals(true, lblfirmwareUpdAvailable.isClickable());
			assertEquals(true, btnfirmwareUpdAvailable.isClickable());		
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	
	//********************************************** Test Cases for UI Testing ***************************************************//
	
	
	/*  
	 * Class Name  : Newfirmware
	 * TestCase ID :
	 * Screen #    : NCFU03a/NCFU03b
	 */
	
	public void testNewFirmwareFragmentContentText() {
		try {			
			View view = activity.getLayoutInflater().inflate(R.layout.new_firmware, null);
			ALog.i(ALog.MAINACTIVITY, "Firmware Update => testNewFirmwareFragmentContentText Testcase Started") ;
			FontTextView txtNewFirmware = (FontTextView) view.findViewById(R.id.new_firmware);
	        assertEquals("New firmware", txtNewFirmware.getText());
	        	                
	        FontTextView txtFirmwareVersion = (FontTextView) view.findViewById(R.id.firmware_version_txt);
	        assertEquals("Firmware version", txtFirmwareVersion.getText());
	        
	        FontTextView txtReleaseDate = (FontTextView) view.findViewById(R.id.releasedate_txt);
	        assertEquals("Release date", txtReleaseDate.getText());
	        
	        FontTextView txtReleaseNotes = (FontTextView) view.findViewById(R.id.release_notes_txt);
	        assertEquals("Release notes", txtReleaseNotes.getText());
	        
	        FontTextView txtFirmwareStatus= (FontTextView) view.findViewById(R.id.firmware_status);
	        assertEquals("!App Update needed", txtFirmwareStatus.getText());
	        
	        Button btnFirmwareUpdate = (Button) view.findViewById(R.id.btn_firmware_update);
	        assertEquals("Update", btnFirmwareUpdate.getText());
	        
	        ALog.i(ALog.MAINACTIVITY, "Firmware Update => testNewFirmwareFragmentContentText Testcase Completed") ;
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
		
	
	/*  
	 * Class Name  : DownloadingFirmwareFragment
	 * TestCase ID :
	 * Screen #    : NCFU05a
	 */
	
	public void testDownloadingFirmwareFragmentContentText() {
		try {			
			View view = activity.getLayoutInflater().inflate(R.layout.downloading_firmware, null);
			ALog.i(ALog.MAINACTIVITY, "Firmware Update => testDownloadingFirmwareFragmentContentText Testcase Started") ;
			
			FontTextView txtDownloadingFirmware = (FontTextView) view.findViewById(R.id.downloading_firmware_step);
	        assertEquals("Step X of Y", txtDownloadingFirmware.getText());
	        	        
	        FontTextView txtDownloadingFirmwareForPurifierMsg = (FontTextView) view.findViewById(R.id.downloading_firmware_for_purifier_msg);
	        String sText = "Downloading Firmware for %s";
	        assertTrue(sText.contains(txtDownloadingFirmwareForPurifierMsg.getText()));
	        
	        FontTextView txtDownloadingFirmwareText = (FontTextView) view.findViewById(R.id.downloading_firmware_text);
	        assertEquals("Downloading firmware", txtDownloadingFirmwareText.getText());
	        
	        FontTextView txtDownloadingFirmwareVersionText = (FontTextView) view.findViewById(R.id.downloading_firmware_version_text);
	        sText = txtDownloadingFirmwareVersionText.getText().toString();
	        assertTrue(sText.contains("Current version"));
	        sText = txtDownloadingFirmwareVersionText.getText().toString();
	        assertTrue(sText.contains("New version"));
	        
	        ALog.i(ALog.MAINACTIVITY, "Firmware Update => testDownloadingFirmwareFragmentContentText Testcase Completed") ;
		} catch (Exception e) {
			ALog.i(ALog.MAINACTIVITY, e.getMessage()) ;
			e.printStackTrace();
			fail(e.getMessage());			
		}
	}
	
	
	/*  
	 * Class Name  : FirmwareInstalldFragment
	 * TestCase ID :
	 */
	
	public void testDownloadingFirmwareFragmentProgressBar() {
		try {		
			
			JsonObject jo = new JsonObject();
			jo.addProperty("name", "Jaguar");
			jo.addProperty("version", "19");			
			jo.addProperty("upgrade", "21");
			jo.addProperty("state", "idle");
			jo.addProperty("progress", "0");
			jo.addProperty("statusmsg", "");
			jo.addProperty("is_mandatory_upgrade", "false");
			
			View view = activity.getLayoutInflater().inflate(R.layout.downloading_firmware, null);
			ALog.i(ALog.MAINACTIVITY, "Firmware Update => testDownloadingFirmwareFragmentProgressBar Testcase Started") ;
			
			ProgressBar pbProgressBar = (ProgressBar) view.findViewById(R.id.downloading_progressbar);
			pbProgressBar.setProgress(50);			
			assertEquals("50", String.valueOf(pbProgressBar.getProgress()));
			
			ALog.i(ALog.MAINACTIVITY, "Firmware Update => testDownloadingFirmwareFragmentProgressBar Testcase Completed") ;
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	//*********************************************** Test Cases for Parsing Logic ******************************************** //
	
	/*  
	 * Class Name  : FirmwareDownloadFragment
	 * TestCase ID :
	 */
	
	public void testParsingFirmwareDownload(){
		try {
			JsonObject jo = new JsonObject();
			jo.addProperty("name", "Jaguar");
			jo.addProperty("version", "19");			
			jo.addProperty("upgrade", "21");
			jo.addProperty("state", "idle");
			jo.addProperty("progress", "0");
			jo.addProperty("statusmsg", "");
			jo.addProperty("is_mandatory_upgrade", "false");
			
			String sGetProgress, sGetState;
			FirmwareDownloadFragment objFirwareDldFrag = new FirmwareDownloadFragment();
			sGetProgress = objFirwareDldFrag.getProgress(jo);
			assertEquals(jo.get("progress").getAsString(), sGetProgress);
			
			sGetState = objFirwareDldFrag.getState(jo);
			assertEquals(jo.get("state").getAsString(), sGetState);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/*  
	 * Class Name  : FirmwareInstalledFragment
	 * TestCase ID :
	 */
	
	public void testParsingFirmwareInstall(){
		try {
			JsonObject jo = new JsonObject();
			jo.addProperty("name", "Jaguar");
			jo.addProperty("version", "19");			
			jo.addProperty("upgrade", "21");
			jo.addProperty("state", "idle");
			jo.addProperty("progress", "0");
			jo.addProperty("statusmsg", "");
			jo.addProperty("is_mandatory_upgrade", "false");
			
			String sGetUpgrade, sGetState;
			FirmwareInstallFragment objFirwareDldFrag = new FirmwareInstallFragment();
			sGetUpgrade = objFirwareDldFrag.getUpgrade(jo);
			assertEquals(jo.get("upgrade").getAsString(), sGetUpgrade);
			
			sGetState = objFirwareDldFrag.getState(jo);
			assertEquals(jo.get("state").getAsString(), sGetState);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
}

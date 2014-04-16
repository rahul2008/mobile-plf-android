package com.philips.cl.di.dev.pa.test;

import java.util.List;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.firmware.FirmwareDownloadFragment;
import com.philips.cl.di.dev.pa.firmware.FirmwareInstallFragment;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateActivity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareUpdateTest extends ActivityInstrumentationTestCase2<FirmwareUpdateActivity>  {
		
	private FirmwareUpdateActivity activity;
	private Context mCtx;
	
	public FirmwareUpdateTest() {
	    super(FirmwareUpdateActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();	
		setActivityInitialTouchMode(false);
		activity = getActivity();
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
			
			View view = activity.getLayoutInflater().inflate(R.layout.rl_home_master_fragment, null);
						
			final LinearLayout llfirmwareUpdAvailable = (LinearLayout) view.findViewById(R.id.firmware_update_available);
			final FontTextView lblfirmwareUpdAvailable = (FontTextView) view.findViewById(R.id.lbl_firmware_update_available);
			ImageButton btnfirmwareUpdAvailable = (ImageButton) view.findViewById(R.id.btn_firmware_update_available);
			
			assertEquals(LinearLayout.GONE, llfirmwareUpdAvailable.getVisibility());
			llfirmwareUpdAvailable.setVisibility(LinearLayout.VISIBLE);			
			assertEquals(LinearLayout.VISIBLE, llfirmwareUpdAvailable.getVisibility());
			
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
	
	//*********************************************** Test Cases for Crucial Transitions  ******************************************** //
	
	/*  
	 * Class Name  : FirmwareDownloadFragment
	 * TestCase ID :
	 */

	public void testTransitionDownloadFailed(){
		try {
			JsonObject jo = new JsonObject();
			jo.addProperty("name", "Jaguar");
			jo.addProperty("version", "19");			
			jo.addProperty("upgrade", "21");
			jo.addProperty("state", "ready");
			jo.addProperty("progress", "100");
			jo.addProperty("statusmsg", "");
			jo.addProperty("is_mandatory_upgrade", "false");
			
			ALog.i(ALog.MAINACTIVITY, "Firmware Update => testTransitionDownloadFailed testcase started") ;			
							
			FirmwareDownloadFragment fragment = new FirmwareDownloadFragment();
		    android.support.v4.app.FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
		    ft.replace(R.id.firmware_container, fragment);
		    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		    ft.commit();
		    
		    if(jo.get("progress").getAsString().equals("100") && jo.get("state").getAsString().equals("ready")) {
		    fragment.showNextFragment(); }		    
		    
		    List<android.support.v4.app.Fragment> lstFragment = activity.getSupportFragmentManager().getFragments();
		    
		    String name;
		    for(Fragment frg : lstFragment) {
		    	name = frg.getClass().getSimpleName();
		    	if (frg != null && !name.equals("NewFirmwareUpdateFragment")) {
		    		assertEquals("FirmwareInstallFragment", frg.getClass().getSimpleName());
		    	}
		    }		    
		    
			ALog.i(ALog.MAINACTIVITY, "Firmware Update => testTransitionDownloadFailed testcase Completed ") ;
			
		 } catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}

package com.philips.cl.di.dev.pa.test;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.firmware.FirmwareDownloadFragment;
import com.philips.cl.di.dev.pa.firmware.FirmwareUpdateActivity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FirmwareUpdateTest extends ActivityInstrumentationTestCase2<FirmwareUpdateActivity>  {
		
	private FirmwareUpdateActivity activity;
	
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
			
	//********************************************** Test Cases for UI Testing ***************************************************//
		
	/*  
	 * Class Name  : Newfirmware
	 * TestCase ID :
	 * Screen #    : NCFU03a/NCFU03b
	 */
	
	public void testNewFirmwareFragmentContentText() {
		try {			
			View view = activity.getLayoutInflater().inflate(R.layout.update_firmware, null);
			ALog.i(ALog.MAINACTIVITY, "Firmware Update => testNewFirmwareFragmentContentText Testcase Started") ;
			FontTextView txtNewFirmware = (FontTextView) view.findViewById(R.id.new_firmware);
	        assertEquals("New firmware", txtNewFirmware.getText());
	        	                
	        FontTextView txtFirmwareVersion = (FontTextView) view.findViewById(R.id.firmware_version_txt);
	        assertEquals("Firmware version", txtFirmwareVersion.getText());
	        
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
	        assertEquals("Step 1 of 2", txtDownloadingFirmware.getText());
	        	        
	        FontTextView txtDownloadingFirmwareForPurifierMsg = (FontTextView) view.findViewById(R.id.downloading_firmware_for_purifier_msg);
	        String sText = "Downloading Firmware for %s";
	        assertTrue(sText.contains(txtDownloadingFirmwareForPurifierMsg.getText()));
	        
	        FontTextView txtDownloadingFirmwareText = (FontTextView) view.findViewById(R.id.downloading_firmware_text);
	        assertEquals("Downloading firmware", txtDownloadingFirmwareText.getText());
	        
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
		    ft.commitAllowingStateLoss();
		    
		    if(jo.get("progress").getAsString().equals("100") && jo.get("state").getAsString().equals("ready")) {
		    fragment.showNextFragment(); }		    
		    
		    List<android.support.v4.app.Fragment> lstFragment = activity.getSupportFragmentManager().getFragments();
		    
		    String name;
		    for(Fragment frg : lstFragment) {
		    	name = frg.getClass().getSimpleName();
		    	if (frg != null && !name.equals("FirmwareInstalledFragment")) {
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

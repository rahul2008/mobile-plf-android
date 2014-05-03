package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.datamodel.City;
import com.philips.cl.di.dev.pa.purifier.TaskGetHttp;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class OutdoorController implements ServerResponseListener{

	private List<OutdoorEventListener> outdoorEventListeners;
	
	private static OutdoorController smInstance;
	
	private OutdoorController() {
		outdoorEventListeners = new ArrayList<OutdoorEventListener>();
	}
	
	public static OutdoorController getInstance() {
		if(smInstance == null) {
			smInstance = new OutdoorController();
		}
		return smInstance;
	}
	
	public void setOutdoorEventListener(OutdoorEventListener outdoorEventListener) {
		outdoorEventListeners.add(outdoorEventListener);
	}
	
	public void removeOutdoorEventListener(OutdoorEventListener outdoorEventListener) {
		outdoorEventListeners.remove(outdoorEventListener);
	}

	//TODO : Move URL to AppConstants.
	public void startCitiesTask() {
		TaskGetHttp citiesList = new TaskGetHttp("http://ixuanwu.com.cn/app/citys.php", PurAirApplication.getAppContext(), this);
		citiesList.start();
	}
	
	
	
	private void notifyListeners(String data) {
//		ALog.i(ALog.DASHBOARD, "OutdoorController notifyListeners data " + data);
		if(outdoorEventListeners == null) return;
		
		List<City> citiesList = DataParser.parseLocationData(data);
		
		if(citiesList == null) return;
		
		for(int index = 0; index < outdoorEventListeners.size(); index++) {
			outdoorEventListeners.get(index).outdoorLocationDataReceived(citiesList);
		}
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String data) {
//		ALog.i(ALog.DASHBOARD, "OutdoorController receiveServerResponse data " + data);
		if(data != null) {
			notifyListeners(data);
		}
	}

}

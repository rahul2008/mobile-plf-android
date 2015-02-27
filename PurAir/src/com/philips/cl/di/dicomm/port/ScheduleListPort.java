package com.philips.cl.di.dicomm.port;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulerHandler;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.util.ALog;

public class ScheduleListPort extends DICommPort {
	
	private final SchedulerHandler mSchedulerHandler;
	
	private List<SchedulePortInfo> mSchedulerPortInfoList;
	
	public ScheduleListPort(NetworkNode networkNode, SchedulerHandler schedulerHandler){
		super(networkNode);
		mSchedulerHandler = schedulerHandler;
	}
	
	public List<SchedulePortInfo> getSchedulePortInfoList() {
		return mSchedulerPortInfoList;
	}

	public void setSchedulePortInfoList(List<SchedulePortInfo> schedulePortInfoList) {
		mSchedulerPortInfoList = schedulePortInfoList;
	}

	@Override
	public boolean isResponseForThisPort(String response) {
		if (parseResponseAsSingleSchedule(response)!=null) return true;
		if (parseResponseAsScheduleList(response)!=null) return true;
		return false;
	}

	@Override
	public void processResponse(String response) {
		//TODO: DIComm Refactor, implement
        throw new RuntimeException("Method Not Implemented, SchedulerActivity should be refactored");
	}
	
	// TODO: DICOmm refactor - make private
	public SchedulePortInfo parseResponseAsSingleSchedule(String response) {
		//TODO: DIComm Refactor
		if (response == null || response.isEmpty()) return null;
		SchedulePortInfo schedulePortInfo = new SchedulePortInfo() ;
		try {
			JSONObject scheduleJson = new JSONObject(response) ;
			JSONObject scheduleJsonViaCPP = scheduleJson.optJSONObject("data") ;
			if(scheduleJsonViaCPP != null ) {
				scheduleJson = scheduleJsonViaCPP ;
			}
			schedulePortInfo.setName(scheduleJson.getString("name")) ;
			schedulePortInfo.setEnabled(scheduleJson.getBoolean("enabled")) ;
			schedulePortInfo.setDays(scheduleJson.getString("days")) ;
			schedulePortInfo.setMode(scheduleJson.getJSONObject("command").getString("om")) ;
			schedulePortInfo.setScheduleTime(scheduleJson.getString("time")) ;
		} catch (JSONException e) {
			schedulePortInfo = null ;
			ALog.e(ALog.SCHEDULELISTPORT, "Exception: " + "Error: " + e.getMessage());
		} catch (Exception e) {
			schedulePortInfo = null ;
			ALog.e(ALog.SCHEDULELISTPORT, "Exception: " + "Error: " + e.getMessage());
		}
		return schedulePortInfo ;
     }
	
	// TODO: DICOmm refactor - make private
	public List<SchedulePortInfo> parseResponseAsScheduleList(String response) {
		//TODO: DIComm Refactor
		if (response == null || response.isEmpty()) return null;
		ALog.i(ALog.SCHEDULELISTPORT, response) ;
		List<SchedulePortInfo> schedulesList = new ArrayList<SchedulePortInfo>() ;
		JSONObject jsonObject = null ;
		try {			
			jsonObject = new JSONObject(response);
			JSONObject schedulerJsonFromCPP = jsonObject.optJSONObject("data") ;
			if( schedulerJsonFromCPP != null ) {
				jsonObject = schedulerJsonFromCPP ;
			}
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = jsonObject.keys() ;
			String key = null ;
			while(iterator.hasNext()) {
				key = iterator.next() ;
				SchedulePortInfo schedules = new SchedulePortInfo() ;
				JSONObject schedule;
				schedule = jsonObject.getJSONObject(key);
				schedules.setName((String)schedule.get("name")) ;
				schedules.setScheduleNumber(Integer.parseInt(key)) ;
				schedulesList.add(schedules) ;
			}
		
		} catch (JSONException e) {
			schedulesList = null ;
			ALog.e(ALog.SCHEDULELISTPORT, "JsonIOException: " + "Error: " + e.getMessage());
		} catch(Exception e) {
			schedulesList = null ;
			ALog.e(ALog.SCHEDULELISTPORT, "JsonIOException : " + "Error: " + e.getMessage());
		}
		return schedulesList ;
     }
	
	public void sendScheduleDetailsToPurifier(String data, SCHEDULE_TYPE scheduleType,int scheduleNumber) {
		mSchedulerHandler.setScheduleDetails(data, mNetworkNode, scheduleType, scheduleNumber) ;
    }
	
}

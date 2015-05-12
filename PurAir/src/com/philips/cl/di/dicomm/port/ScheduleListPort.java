package com.philips.cl.di.dicomm.port;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortListener;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.Error;
import com.philips.cl.di.dicomm.communication.ResponseHandler;

public class ScheduleListPort extends DICommPort<SchedulePortInfo> {

    private static final String KEY_SCHEDULECOMMAND = "command";
	private static final String KEY_SCHEDULEPORT = "port";
	private static final String KEY_SCHEDULEPRODUCTID = "product";
	private static final String KEY_SCHEDULEDAYS = "days";
	private static final String KEY_SCHEDULETIME = "time";
	private static final String KEY_SCHEDULEENABLED = "enabled";
	private static final String KEY_SCHEDULENAME = "name";
	
	private final String SCHEDULELISTPORT_NAME = "schedules";
	private final int SCHEDULELISTPORT_PRODUCTID = 0;
	private List<SchedulePortInfo> mSchedulerPortInfoList;
	private SchedulePortListener mSchedulePortListener;
	
	public static final String ERROR_OUT_OF_MEMORY = "out of memory" ;
	public static final int MAX_SCHEDULES_REACHED = 1;
	public static final int DEFAULT_ERROR = 999;
	
	public ScheduleListPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy){
		super(networkNode,communicationStrategy);
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
	
	@Override
	public String getDICommPortName() {
		return SCHEDULELISTPORT_NAME;
	}
	
	@Override
	public int getDICommProductId() {
		return SCHEDULELISTPORT_PRODUCTID;
	}

    @Override
    public boolean supportsSubscription() {
        return false;
    }
    
	public List<SchedulePortInfo> getSchedulePortInfoList() {
		return mSchedulerPortInfoList;
	}

	public void setSchedulePortInfoList(List<SchedulePortInfo> schedulePortInfoList) {
		mSchedulerPortInfoList = schedulePortInfoList;
	}
    
	private String getDICommNestedPortName(int scheduleNumber) {
		return String.format("%s/%d", getDICommPortName(), scheduleNumber);
	}
	
	public void setSchedulePortListener(SchedulePortListener listener) {
		mSchedulePortListener = listener;
	}
	
	public void clearSchedulePortListener() {
		mSchedulePortListener = null;
	}

	public void getSchedules() {
		mCommunicationStrategy.getProperties(getDICommPortName(), getDICommProductId(), mNetworkNode, new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				handleSuccessResponse(data);
			}
			
			@Override
			public void onError(Error error, String errorData) {
				handleErrorResponse();
			}
		});
	}

	public void getScheduleDetails(int scheduleNumber) {
		mCommunicationStrategy.getProperties(getDICommNestedPortName(scheduleNumber), getDICommProductId(), mNetworkNode,new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				handleSuccessResponse(data);
			}
			
			@Override
			public void onError(Error error, String errorData) {
				handleErrorResponse();
			}
		});
	}

	public void addSchedule(String portName, int productId, String time, String days, boolean enabled, Map<String, Object> commandMap) {
		Map<String, Object> dataMap = createDataMap(portName, productId, time, days, enabled, commandMap);

		mCommunicationStrategy.addProperties(dataMap, getDICommPortName(), getDICommProductId(), mNetworkNode, new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				handleSuccessResponse(data);
			}
			
			@Override
			public void onError(Error error, String errorData) {
				handleErrorResponse();
			}
		});
	}

	public void updateSchedule(int scheduleNumber, String portName, int productId, String time, String days, boolean enabled, Map<String, Object> commandMap) {
		Map<String, Object> dataMap = createDataMap(portName, productId, time, days, enabled, commandMap);
		
		mCommunicationStrategy.putProperties(dataMap, getDICommNestedPortName(scheduleNumber), getDICommProductId(), mNetworkNode, new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				handleSuccessResponse(data);
			}
			
			@Override
			public void onError(Error error, String errorData) {
				handleErrorResponse();
			}
		});
	}

	public void deleteSchedule(int scheduleNumber) {
		mCommunicationStrategy.deleteProperties(getDICommNestedPortName(scheduleNumber), getDICommProductId(), mNetworkNode, new ResponseHandler() {
			
			@Override
			public void onSuccess(String data) {
				handleSuccessResponse(data);
			}
			
			@Override
			public void onError(Error error, String errorData) {
				handleErrorResponse();
			}
		});
	}

	private Map<String, Object> createDataMap(String portName, int productId, String time, String days, boolean enabled, Map<String, Object> commandMap) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put(KEY_SCHEDULENAME, time);
		dataMap.put(KEY_SCHEDULEENABLED, enabled);
		dataMap.put(KEY_SCHEDULETIME, time);
		dataMap.put(KEY_SCHEDULEDAYS, days);
		dataMap.put(KEY_SCHEDULEPRODUCTID, productId);
		dataMap.put(KEY_SCHEDULEPORT, portName);
		dataMap.put(KEY_SCHEDULECOMMAND, commandMap);
		return dataMap;
	}

	private void handleErrorResponse() {
		if (mSchedulePortListener != null) {
			mSchedulePortListener.onError(ScheduleListPort.DEFAULT_ERROR);					
		}
	}
	
	private void handleSuccessResponse(String data) {
		SchedulePortInfo schedulePortInfo = parseResponseAsSingleSchedule(data);
		if( schedulePortInfo != null && mSchedulePortListener != null) {
			mSchedulePortListener.onScheduleReceived(schedulePortInfo);
			return ;
		}
		List<SchedulePortInfo> schedulePortInfoList = parseResponseAsScheduleList(data);
		if(  schedulePortInfoList != null && mSchedulePortListener != null ) {
			mSchedulePortListener.onSchedulesReceived(schedulePortInfoList);
			return ;
		}

		if( data.contains(ScheduleListPort.ERROR_OUT_OF_MEMORY)) {
			if(mSchedulePortListener != null){
				mSchedulePortListener.onError(ScheduleListPort.MAX_SCHEDULES_REACHED);
			}
		}
	}
	
	SchedulePortInfo parseResponseAsSingleSchedule(String response) {
		//TODO: DIComm Refactor
		if (response == null || response.isEmpty()) return null;
		SchedulePortInfo schedulePortInfo = new SchedulePortInfo() ;
		ALog.i(ALog.SCHEDULELISTPORT, response) ;
		try {
			JSONObject scheduleJson = new JSONObject(response) ;
			JSONObject scheduleJsonViaCPP = scheduleJson.optJSONObject("data") ;
			if(scheduleJsonViaCPP != null ) {
				scheduleJson = scheduleJsonViaCPP ;
			}
			schedulePortInfo.setName(scheduleJson.getString(KEY_SCHEDULENAME)) ;
			schedulePortInfo.setEnabled(scheduleJson.getBoolean(KEY_SCHEDULEENABLED)) ;
			schedulePortInfo.setDays(scheduleJson.getString(KEY_SCHEDULEDAYS)) ;
			schedulePortInfo.setMode(scheduleJson.getJSONObject(KEY_SCHEDULECOMMAND).getString("om")) ;
			schedulePortInfo.setScheduleTime(scheduleJson.getString(KEY_SCHEDULETIME)) ;
		} catch (JSONException e) {
			schedulePortInfo = null ;
			ALog.e(ALog.SCHEDULELISTPORT, "Exception: " + "Error: " + e.getMessage());
		} catch (Exception e) {
			schedulePortInfo = null ;
			ALog.e(ALog.SCHEDULELISTPORT, "Exception: " + "Error: " + e.getMessage());
		}
		return schedulePortInfo ;
     }

	 List<SchedulePortInfo> parseResponseAsScheduleList(String response) {
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
			Iterator<String> iterator = jsonObject.keys() ;
			String key = null ;
			while(iterator.hasNext()) {
				key = iterator.next() ;
				SchedulePortInfo schedules = new SchedulePortInfo() ;
				JSONObject schedule;
				schedule = jsonObject.getJSONObject(key);
				schedules.setName((String)schedule.get(KEY_SCHEDULENAME)) ;
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
	
}

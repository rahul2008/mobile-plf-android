/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScheduleListPort extends DICommPort<ScheduleListPortInfo> {

    private static final String KEY_SCHEDULECOMMAND = "command";
    private static final String KEY_SCHEDULEPORT = "port";
    private static final String KEY_SCHEDULEPRODUCTID = "product";
    private static final String KEY_SCHEDULEDAYS = "days";
    private static final String KEY_SCHEDULETIME = "time";
    private static final String KEY_SCHEDULEENABLED = "enabled";
    private static final String KEY_SCHEDULENAME = "name";

    private final String SCHEDULELISTPORT_NAME = "schedules";
    private final int SCHEDULELISTPORT_PRODUCTID = 0;
    private List<ScheduleListPortInfo> mSchedulerPortInfoList;
    private SchedulePortListener mSchedulePortListener;

    public static final String ERROR_OUT_OF_MEMORY = "out of memory";
    public static final int MAX_SCHEDULES_REACHED = 1;
    public static final int DEFAULT_ERROR = 999;

    public ScheduleListPort(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public boolean isResponseForThisPort(String jsonResponse) {
        if (parseResponseAsSingleSchedule(jsonResponse) != null) return true;
        if (parseResponseAsScheduleList(jsonResponse) != null) return true;
        return false;
    }

    @Override
    public void processResponse(String jsonResponse) {
        //TODO: DIComm Refactor, implement
        DICommLog.e(DICommLog.SCHEDULELISTPORT, "Method Not Implemented, SchedulerActivity should be refactored");
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

    public List<ScheduleListPortInfo> getSchedulePortInfoList() {
        return mSchedulerPortInfoList;
    }

    public void setSchedulePortInfoList(List<ScheduleListPortInfo> schedulePortInfoList) {
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
        mCommunicationStrategy.getProperties(getDICommPortName(), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                handleSuccessResponse(data);
            }

            @Override
            public void onError(Error error, String errorData) {
                handleErrorResponse(error);
            }
        });
    }

    public void getScheduleDetails(int scheduleNumber) {
        mCommunicationStrategy.getProperties(getDICommNestedPortName(scheduleNumber), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                handleSuccessResponse(data);
            }

            @Override
            public void onError(Error error, String errorData) {
                handleErrorResponse(error);
            }
        });
    }

    /**
     * Use addSchedule(String portName, int productId, final String name, String time, String days, boolean enabled, Map<String, Object> commandMap) instead.<br><br>
     * <p/>
     * This method will insert the time parameter as the name parameter
     */
    @Deprecated
    public void addSchedule(String portName, int productId, String time, String days, boolean enabled, Map<String, Object> commandMap) {
        addSchedule(portName, productId, time, time, days, enabled, commandMap);
    }

    public void addSchedule(String portName, int productId, final String name, String time, String days, boolean enabled, Map<String, Object> commandMap) {
        Map<String, Object> dataMap = createDataMap(portName, productId, name, time, days, enabled, commandMap);

        mCommunicationStrategy.addProperties(dataMap, getDICommPortName(), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                handleSuccessResponse(data);
            }

            @Override
            public void onError(Error error, String errorData) {
                handleErrorResponse(error);
            }
        });
    }

    /**
     * Use addSchedule(String portName, int productId, final String name, String time, String days, boolean enabled, Map<String, Object> commandMap) instead.<br><br>
     * <p/>
     * This method will insert the time parameter as the name parameter
     */
    @Deprecated
    public void updateSchedule(int scheduleNumber, String portName, int productId, String time, String days, boolean enabled, Map<String, Object> commandMap) {
        updateSchedule(scheduleNumber, portName, productId, time, time, days, enabled, commandMap);
    }

    public void updateSchedule(int scheduleNumber, String portName, int productId, final String name, String time, String days, boolean enabled, Map<String, Object> commandMap) {
        Map<String, Object> dataMap = createDataMap(portName, productId, name, time, days, enabled, commandMap);

        mCommunicationStrategy.putProperties(dataMap, getDICommNestedPortName(scheduleNumber), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                handleSuccessResponse(data);
            }

            @Override
            public void onError(Error error, String errorData) {
                handleErrorResponse(error);
            }
        });
    }

    public void deleteSchedule(int scheduleNumber) {
        mCommunicationStrategy.deleteProperties(getDICommNestedPortName(scheduleNumber), getDICommProductId(), new ResponseHandler() {

            @Override
            public void onSuccess(String data) {
                handleSuccessResponse(data);
            }

            @Override
            public void onError(Error error, String errorData) {
                handleErrorResponse(error);
            }
        });
    }

    private Map<String, Object> createDataMap(String portName, int productId, final String name, String time, String days, boolean enabled, Map<String, Object> commandMap) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(KEY_SCHEDULENAME, name);
        dataMap.put(KEY_SCHEDULEENABLED, enabled);
        dataMap.put(KEY_SCHEDULETIME, time);
        dataMap.put(KEY_SCHEDULEDAYS, days);
        dataMap.put(KEY_SCHEDULEPRODUCTID, productId);
        dataMap.put(KEY_SCHEDULEPORT, portName);
        dataMap.put(KEY_SCHEDULECOMMAND, commandMap);
        return dataMap;
    }

    private void handleErrorResponse(final Error error) {
        if (mSchedulePortListener != null) {
            mSchedulePortListener.onError(error != null ? error.ordinal() : DEFAULT_ERROR);
        }
    }

    private void handleSuccessResponse(String data) {
        ScheduleListPortInfo schedulePortInfo = parseResponseAsSingleSchedule(data);
        if (schedulePortInfo != null && mSchedulePortListener != null) {
            mSchedulePortListener.onScheduleReceived(schedulePortInfo);
            return;
        }
        List<ScheduleListPortInfo> schedulePortInfoList = parseResponseAsScheduleList(data);
        if (schedulePortInfoList != null && mSchedulePortListener != null) {
            mSchedulePortListener.onSchedulesReceived(schedulePortInfoList);
            return;
        }

        if (data.contains(ScheduleListPort.ERROR_OUT_OF_MEMORY)) {
            if (mSchedulePortListener != null) {
                mSchedulePortListener.onError(ScheduleListPort.MAX_SCHEDULES_REACHED);
            }
        }
    }

    ScheduleListPortInfo parseResponseAsSingleSchedule(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        DICommLog.i(DICommLog.SCHEDULELISTPORT, response);

        ScheduleListPortInfo scheduleListPortInfo = null;
        try {
            Gson gson = new GsonBuilder().create();
            ScheduleListPortInfo.ScheduleListPortInfoFromCpp scheduleListPortInfoFromCpp = gson.fromJson(response, ScheduleListPortInfo.ScheduleListPortInfoFromCpp.class);
            if (hasValidPortInfo(scheduleListPortInfoFromCpp)) {
                scheduleListPortInfo = scheduleListPortInfoFromCpp.getData();
            } else {
                ScheduleListPortInfo tempPortInfo = gson.fromJson(response, ScheduleListPortInfo.class);
                if (tempPortInfo.getName() != null) {
                    scheduleListPortInfo = tempPortInfo;
                }
            }
        } catch (JsonSyntaxException e) {
            DICommLog.e(DICommLog.SCHEDULELISTPORT, "JsonSyntaxException: " + "Error: " + e.getMessage());
        } catch (JsonIOException e) {
            DICommLog.e(DICommLog.SCHEDULELISTPORT, "JsonIOException: " + "Error: " + e.getMessage());
        } catch (Exception e) {
            DICommLog.e(DICommLog.SCHEDULELISTPORT, "Exception: " + "Error: " + e.getMessage());
        }

        return scheduleListPortInfo;
    }

    private boolean hasValidPortInfo(final ScheduleListPortInfo.ScheduleListPortInfoFromCpp scheduleListPortInfoFromCpp) {
        boolean isValid = false;
        ScheduleListPortInfo portInfo = scheduleListPortInfoFromCpp.getData();
        if (portInfo != null) {
            isValid = portInfo.getName() != null
                    && portInfo.getPort() != null
                    && portInfo.getScheduleTime() != null;
        }

        return isValid;
    }

    List<ScheduleListPortInfo> parseResponseAsScheduleList(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        DICommLog.i(DICommLog.SCHEDULELISTPORT, response);

        List<ScheduleListPortInfo> schedulesList = new ArrayList<>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            Iterator<String> iterator = jsonObject.keys();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                Object opt = jsonObject.optJSONObject(key);
                if (opt == null) {
                    schedulesList = null;
                    break;
                }

                String string = jsonObject.getJSONObject(key).toString();
                ScheduleListPortInfo portInfo = parseResponseAsSingleSchedule(string);
                portInfo.setScheduleNumber(Integer.parseInt(key));
                schedulesList.add(portInfo);
            }
        } catch (JSONException e) {
            schedulesList = null;
            DICommLog.w(DICommLog.SCHEDULELISTPORT, "JSONException: " + "Error: " + e.getMessage());
        } catch (Exception e) {
            schedulesList = null;
            DICommLog.w(DICommLog.SCHEDULELISTPORT, "Exception : " + "Error: " + e.getMessage());
        }
        return schedulesList;
    }
}

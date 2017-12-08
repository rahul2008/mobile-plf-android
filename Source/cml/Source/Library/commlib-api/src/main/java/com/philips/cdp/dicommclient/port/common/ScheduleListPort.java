/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.reflect.TypeToken;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    public ScheduleListPort(final @NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    @Override
    public void processResponse(String jsonResponse) {
        DICommLog.e(DICommLog.SCHEDULELISTPORT, "Not implemented.");
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
        return String.format(Locale.US, "%s/%d", getDICommPortName(), scheduleNumber);
    }

    public void setSchedulePortListener(SchedulePortListener listener) {
        mSchedulePortListener = listener;
    }

    public void clearSchedulePortListener() {
        mSchedulePortListener = null;
    }

    public void getSchedules() {
        this.communicationStrategy.getProperties(getDICommPortName(), getDICommProductId(), new ResponseHandler() {

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
        this.communicationStrategy.getProperties(getDICommNestedPortName(scheduleNumber), getDICommProductId(), new ResponseHandler() {

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

        this.communicationStrategy.addProperties(dataMap, getDICommPortName(), getDICommProductId(), new ResponseHandler() {

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

        this.communicationStrategy.putProperties(dataMap, getDICommNestedPortName(scheduleNumber), getDICommProductId(), new ResponseHandler() {

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
        this.communicationStrategy.deleteProperties(getDICommNestedPortName(scheduleNumber), getDICommProductId(), new ResponseHandler() {

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

    @VisibleForTesting
    ScheduleListPortInfo parseResponseAsSingleSchedule(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        DICommLog.i(DICommLog.SCHEDULELISTPORT, response);

        ScheduleListPortInfo scheduleListPortInfo = null;
        try {
            ScheduleListPortInfo.ScheduleListPortInfoFromCpp scheduleListPortInfoFromCpp = gson.fromJson(response, ScheduleListPortInfo.ScheduleListPortInfoFromCpp.class);
            if (hasValidPortInfo(scheduleListPortInfoFromCpp)) {
                scheduleListPortInfo = scheduleListPortInfoFromCpp.getData();
            } else {
                ScheduleListPortInfo tempPortInfo = gson.fromJson(response, ScheduleListPortInfo.class);
                if (tempPortInfo.getName() != null) {
                    scheduleListPortInfo = tempPortInfo;
                }
            }
        } catch (Exception e) {
            DICommLog.e(DICommLog.SCHEDULELISTPORT, e.getMessage());
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

    @Nullable
    @VisibleForTesting
    List<ScheduleListPortInfo> parseResponseAsScheduleList(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }
        DICommLog.i(DICommLog.SCHEDULELISTPORT, response);

        final Type scheduleMapType = new TypeToken<Map<Integer, ScheduleListPortInfo>>() {
        }.getType();
        try {
            Map<Integer, ScheduleListPortInfo> map = gson.fromJson(response, scheduleMapType);
            List<ScheduleListPortInfo> scheduleList = new ArrayList<>();

            for (Map.Entry<Integer, ScheduleListPortInfo> entry : map.entrySet()) {
                ScheduleListPortInfo scheduleListPortInfo = entry.getValue();
                scheduleListPortInfo.setScheduleNumber(entry.getKey());

                scheduleList.add(scheduleListPortInfo);
            }
            return scheduleList;
        } catch (Throwable ignored) {
        }
        return null;
    }
}

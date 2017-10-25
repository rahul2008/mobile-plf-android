/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.os.Handler;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScheduleListPortTest {

    private String scheduleDetailJson_WithMode = "{\"name\":\"16:00\",\"enabled\":true,\"name\":\"testName\",\"time\":\"16:00\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"mode\":{\"om\":\"a\"}}";
    private String scheduleDetailJson_WithCommand = "{\"name\":\"16:00\",\"enabled\":true,\"name\":\"testName\",\"time\":\"16:00\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"testKey\":\"testValue\"}}";
    private String allScheduleJson = "{\"2\":{\"name\":\"18:45\"},\"3\":{\"name\":\"15:45\"},\"4\":{\"name\":\"20:00\"}}";

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Mock
    private Handler handlerMock;

    private ScheduleListPort scheduleListPort;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);

        scheduleListPort = new ScheduleListPort(communicationStrategyMock);
    }

    @Test
    public void testParseSchedulerDtoWithNullParam() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(null);
        assertNull(schedulePortInfos);
    }

    @Test
    public void testParseSchedulerDtoWithEmptyStringParam() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("");
        assertNull(schedulePortInfos);
    }

    @Test
    public void testParseSchedulerDtoWithWrongParam() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("{\"temp\":\"1\"}");
        assertNull(schedulePortInfos);
    }

    @Test
    public void testParseSchedulerDtoSize() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJson);
        assertEquals(3, schedulePortInfos.size());
    }

    @Test
    public void testParseSchedulerDtoKeys() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJson);
        ArrayList<Integer> keys = new ArrayList<>();
        keys.add(2);
        keys.add(3);
        keys.add(4);

        assertThat(keys.contains(schedulePortInfos.get(0).getScheduleNumber())).isTrue();
        assertThat(keys.contains(schedulePortInfos.get(1).getScheduleNumber())).isTrue();
        assertThat(keys.contains(schedulePortInfos.get(2).getScheduleNumber())).isTrue();
    }

    @Test
    public void testParseSchedulerDtoNames() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJson);

        ArrayList<String> names = new ArrayList<>();
        names.add("18:45");
        names.add("15:45");
        names.add("20:00");

        assertTrue(names.contains(schedulePortInfos.get(0).getName()));
        assertTrue(names.contains(schedulePortInfos.get(1).getName()));
        assertTrue(names.contains(schedulePortInfos.get(2).getName()));
    }

    @Test
    public void testParseSchedulerDtoWithWrongJsonFormat() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("hello");
        assertNull(schedulePortInfos);
    }

    @Test
    public void testparseSchedulerDtoWithNullParam() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(null);
        assertNull(schedulePortInfos);
    }

    @Test
    public void testparseSchedulerDtoWithEmptyStringParam() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("");
        assertNull(schedulePortInfos);
    }

    @Test
    public void testparseSchedulerDtoWithWrongParam() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("{\"temp\":\"1\"}");
        assertNull(schedulePortInfos);
    }

    @Test
    public void testparseSchedulerDtoWithWrongJsonFormat() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("hello");
        assertNull(schedulePortInfos);
    }

    @Test
    public void testParseScheduleDetailsWithNullParam() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(null);
        assertNull(schedulePortInfo);
    }

    @Test
    public void testParseScheduleDetailsWithEmptyStringParam() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("");
        assertNull(schedulePortInfo);
    }

    @Test
    public void testParseScheduleDetailsWithWrongParam() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("{\"temp\":\"1\"}");
        assertNull(schedulePortInfo);
    }

    @Test
    public void testParseScheduleDetailsWithWrongJsonFormat() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("hello");
        assertNull(schedulePortInfo);
    }

    @Test
    public void testParseScheduleDetailsScheduleName() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson_WithMode);
        assertEquals("testName", schedulePortInfo.getName());
    }

    @Test
    public void testParseScheduleDetailsScheduleTime() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson_WithMode);
        assertEquals("16:00", schedulePortInfo.getScheduleTime());
    }

    @Test
    public void testParseScheduleDetailsScheduleDay() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson_WithMode);
        assertEquals("123", schedulePortInfo.getDays());
    }

    @Test
    public void testParseScheduleDetailsSchedulePort() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson_WithMode);
        assertEquals("air", schedulePortInfo.getPort());
    }

    @Test
    public void testParseScheduleDetailsScheduleMode() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson_WithMode);
        assertEquals("a", schedulePortInfo.getMode());
    }

    @Test
    public void testParseScheduleDetailsScheduleCommand() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson_WithCommand);
        Map<String, Object> command = schedulePortInfo.getCommand();
        assertEquals("testValue", command.get("testKey"));
    }

    @Test
    public void testparseScheduleDetailsWithNullParam() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(null);
        assertNull(schedulePortInfo);
    }

    @Test
    public void testparseScheduleDetailsWithEmptyStringParam() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("");
        assertNull(schedulePortInfo);
    }

    @Test
    public void testparseScheduleDetailsWithWrongParam() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("{\"temp\":\"1\"}");
        assertNull(schedulePortInfo);
    }

    @Test
    public void testparseScheduleDetailsWithWrongJsonFormat() {
        ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("hello");
        assertNull(schedulePortInfo);
    }

    private ScheduleListPortInfo parseSingleScheduledata(String data) {
        return scheduleListPort.parseResponseAsSingleSchedule(data);
    }

    private List<ScheduleListPortInfo> parseScheduleListdata(String data) {
        return scheduleListPort.parseResponseAsScheduleList(data);
    }
}

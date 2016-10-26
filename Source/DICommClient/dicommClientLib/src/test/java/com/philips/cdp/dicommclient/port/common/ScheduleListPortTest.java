/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class ScheduleListPortTest extends RobolectricTest {

    private String scheduleDetailJson_WithMode = "{\"name\":\"16:00\",\"enabled\":true,\"name\":\"testName\",\"time\":\"16:00\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"mode\":{\"om\":\"a\"}}";
    private String scheduleDetailJson_WithCommand = "{\"name\":\"16:00\",\"enabled\":true,\"name\":\"testName\",\"time\":\"16:00\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"testKey\":\"testValue\"}}";
    private String allScheduleJson = "{\"2\":{\"name\":\"18:45\"},\"3\":{\"name\":\"15:45\"},\"4\":{\"name\":\"20:00\"}}";

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
        ArrayList<Integer> keys = new ArrayList<Integer>();
        keys.add(2);
        keys.add(3);
        keys.add(4);

        assertTrue(keys.contains(schedulePortInfos.get(0).getScheduleNumber()));
        assertTrue(keys.contains(schedulePortInfos.get(1).getScheduleNumber()));
        assertTrue(keys.contains(schedulePortInfos.get(2).getScheduleNumber()));
    }

    @Test
    public void testParseSchedulerDtoNames() {
        List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJson);

        ArrayList<String> names = new ArrayList<String>();
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
        ScheduleListPort scheduleListPort = new ScheduleListPort(null);
        return scheduleListPort.parseResponseAsSingleSchedule(data);
    }

    private List<ScheduleListPortInfo> parseScheduleListdata(String data) {
        ScheduleListPort scheduleListPort = new ScheduleListPort(null);
        return scheduleListPort.parseResponseAsScheduleList(data);
    }
}

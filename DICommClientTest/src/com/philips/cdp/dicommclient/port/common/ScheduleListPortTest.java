package com.philips.cdp.dicommclient.port.common;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.philips.cdp.dicommclient.port.common.ScheduleListPort;
import com.philips.cdp.dicommclient.port.common.ScheduleListPortInfo;

public class ScheduleListPortTest extends TestCase {

	private String scheduleDetailJson = "{\"name\":\"16:00\",\"enabled\":true,\"time\":\"16:00\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"om\":\"a\"}}";
	private String allScheduleJson = "{\"2\":{\"name\":\"18:45\"},\"3\":{\"name\":\"15:45\"},\"4\":{\"name\":\"20:00\"}}";

	private String scheduleDetailJsonCpp = "{\"status\":0,\"data\":{\"name\":\"12:15\",\"enabled\":true,\"time\":\"12:15\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"om\":\"a\"}}}";
	private String allScheduleJsonCpp = "{\"status\":0,\"data\":{\"0\":{\"name\":\"16:14\"},\"1\":{\"name\":\"12:15\"}}}";

	public void testParseSchedulerDtoWithNullParam() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(null);
		assertNull(schedulePortInfos);
	}

	public void testParseSchedulerDtoWithEmptyStringParam() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("");
		assertNull(schedulePortInfos);
	}

	public void testParseSchedulerDtoWithWrongParam() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("{\"temp\":\"1\"}");
		assertNull(schedulePortInfos);
	}

	public void testParseSchedulerDtoSize() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJson);
		assertEquals(3, schedulePortInfos.size());
	}

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

	public void testParseSchedulerDtoWithWrongJsonFormat() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("hello");
		assertNull(schedulePortInfos);
	}

	public void testparseSchedulerDtoWithNullParam() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(null);
		assertNull(schedulePortInfos);
	}

	public void testparseSchedulerDtoWithEmptyStringParam() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("");
		assertNull(schedulePortInfos);
	}

	public void testparseSchedulerDtoWithWrongParam() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("{\"temp\":\"1\"}");
		assertNull(schedulePortInfos);
	}

	public void testparseSchedulerDtoWithWrongJsonFormat() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata("hello");
		assertNull(schedulePortInfos);
	}

	public void testparseSchedulerDtoSize() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJsonCpp);
		assertEquals(2, schedulePortInfos.size());
	}

	public void testparseSchedulerDtoKeys() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJsonCpp);
		ArrayList<Integer> keys = new ArrayList<Integer>();
		keys.add(0);
		keys.add(1);

		assertTrue(keys.contains(schedulePortInfos.get(0).getScheduleNumber()));
		assertTrue(keys.contains(schedulePortInfos.get(1).getScheduleNumber()));
	}

	public void testparseSchedulerDtoNames() {
		List<ScheduleListPortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJsonCpp);

		ArrayList<String> names = new ArrayList<String>();
		names.add("16:14");
		names.add("12:15");

		assertTrue(names.contains(schedulePortInfos.get(0).getName()));
		assertTrue(names.contains(schedulePortInfos.get(1).getName()));
	}


	public void testParseScheduleDetailsWithNullParam() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(null);
		assertNull(schedulePortInfo);
	}

	public void testParseScheduleDetailsWithEmptyStringParam() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("");
		assertNull(schedulePortInfo);
	}

	public void testParseScheduleDetailsWithWrongParam() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("{\"temp\":\"1\"}");
		assertNull(schedulePortInfo);
	}

	public void testParseScheduleDetailsWithWrongJsonFormat() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("hello");
		assertNull(schedulePortInfo);
	}

	public void testParseScheduleDetailsScheduleName() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson);
		assertEquals("16:00", schedulePortInfo.getName());
	}

	public void testParseScheduleDetailsScheduleTime() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson);
		assertEquals("16:00", schedulePortInfo.getScheduleTime());
	}

	public void testParseScheduleDetailsScheduleDay() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson);
		assertEquals("123", schedulePortInfo.getDays());
	}

	public void testParseScheduleDetailsScheduleMode() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson);
		assertEquals("a", schedulePortInfo.getMode());
	}

	public void testparseScheduleDetailsWithNullParam() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(null);
		assertNull(schedulePortInfo);
	}

	public void testparseScheduleDetailsWithEmptyStringParam() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("");
		assertNull(schedulePortInfo);
	}

	public void testparseScheduleDetailsWithWrongParam() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("{\"temp\":\"1\"}");
		assertNull(schedulePortInfo);
	}

	public void testparseScheduleDetailsWithWrongJsonFormat() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata("hello");
		assertNull(schedulePortInfo);
	}

	public void testParseScheduleDetailsScheduleCppName() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJsonCpp);
		assertEquals("12:15", schedulePortInfo.getName());
	}

	public void testParseScheduleDetailsScheduleCppTime() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJsonCpp);
		assertEquals("12:15", schedulePortInfo.getScheduleTime());
	}

	public void testParseScheduleDetailsScheduleCppDay() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJsonCpp);
		assertEquals("123", schedulePortInfo.getDays());
	}

	public void testParseScheduleDetailsScheduleCppMode() {
		ScheduleListPortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJsonCpp);
		assertEquals("a", schedulePortInfo.getMode());
	}

	private ScheduleListPortInfo parseSingleScheduledata(String data){
		ScheduleListPort scheduleListPort = new ScheduleListPort(null, null);
		return scheduleListPort.parseResponseAsSingleSchedule(data);
	}

	private List<ScheduleListPortInfo> parseScheduleListdata(String data){
        ScheduleListPort scheduleListPort = new ScheduleListPort(null, null);
		return scheduleListPort.parseResponseAsScheduleList(data);
	}
}

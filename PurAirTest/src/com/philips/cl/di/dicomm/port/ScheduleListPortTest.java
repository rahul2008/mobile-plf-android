package com.philips.cl.di.dicomm.port;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dicomm.port.ScheduleListPort;

public class ScheduleListPortTest extends TestCase {
	
	private String scheduleDetailJson = "{\"name\":\"16:00\",\"enabled\":true,\"time\":\"16:00\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"om\":\"a\"}}";
	private String allScheduleJson = "{\"2\":{\"name\":\"18:45\"},\"3\":{\"name\":\"15:45\"},\"4\":{\"name\":\"20:00\"}}";
	
	private String scheduleDetailJsonCpp = "{\"status\":0,\"data\":{\"name\":\"12:15\",\"enabled\":true,\"time\":\"12:15\",\"days\":\"123\",\"product\":1,\"port\":\"air\",\"command\":{\"om\":\"a\"}}}";
	private String allScheduleJsonCpp = "{\"status\":0,\"data\":{\"0\":{\"name\":\"16:14\"},\"1\":{\"name\":\"12:15\"}}}";
	
	public void testParseSchedulerDtoWithNullParam() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata(null);
		assertNull(schedulePortInfos);
	}
	
	public void testParseSchedulerDtoWithEmptyStringParam() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata("");
		assertNull(schedulePortInfos);
	}
	
	public void testParseSchedulerDtoWithWrongParam() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata("{\"temp\":\"1\"}");
		assertNull(schedulePortInfos);
	}
	
	public void testParseSchedulerDtoSize() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJson);
		assertEquals(3, schedulePortInfos.size());
	}
	
	public void testParseSchedulerDtoKeys() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJson);
		ArrayList<Integer> keys = new ArrayList<Integer>();
		keys.add(2);
		keys.add(3);
		keys.add(4);
		
		assertTrue(keys.contains(schedulePortInfos.get(0).getScheduleNumber()));
		assertTrue(keys.contains(schedulePortInfos.get(1).getScheduleNumber()));
		assertTrue(keys.contains(schedulePortInfos.get(2).getScheduleNumber()));
	}
	
	public void testParseSchedulerDtoNames() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJson);
		
		ArrayList<String> names = new ArrayList<String>();
		names.add("18:45");
		names.add("15:45");
		names.add("20:00");
		
		assertTrue(names.contains(schedulePortInfos.get(0).getName()));
		assertTrue(names.contains(schedulePortInfos.get(1).getName()));
		assertTrue(names.contains(schedulePortInfos.get(2).getName()));
	}
	
	public void testParseSchedulerDtoWithWrongJsonFormat() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata("hello");
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoWithNullParam() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata(null);
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoWithEmptyStringParam() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata("");
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoWithWrongParam() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata("{\"temp\":\"1\"}");
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoWithWrongJsonFormat() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata("hello");
		assertNull(schedulePortInfos);
	}
	
	public void testparseSchedulerDtoSize() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJsonCpp);
		assertEquals(2, schedulePortInfos.size());
	}
	
	public void testparseSchedulerDtoKeys() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJsonCpp);
		ArrayList<Integer> keys = new ArrayList<Integer>();
		keys.add(0);
		keys.add(1);
		
		assertTrue(keys.contains(schedulePortInfos.get(0).getScheduleNumber()));
		assertTrue(keys.contains(schedulePortInfos.get(1).getScheduleNumber()));
	}
	
	public void testparseSchedulerDtoNames() {
		List<SchedulePortInfo> schedulePortInfos = parseScheduleListdata(allScheduleJsonCpp);
		
		ArrayList<String> names = new ArrayList<String>();
		names.add("16:14");
		names.add("12:15");
		
		assertTrue(names.contains(schedulePortInfos.get(0).getName()));
		assertTrue(names.contains(schedulePortInfos.get(1).getName()));
	}
	
	
	public void testParseScheduleDetailsWithNullParam() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(null);
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsWithEmptyStringParam() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata("");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsWithWrongParam() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata("{\"temp\":\"1\"}");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsWithWrongJsonFormat() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata("hello");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsScheduleName() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson);
		assertEquals("16:00", schedulePortInfo.getName());
	}
	
	public void testParseScheduleDetailsScheduleTime() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson);
		assertEquals("16:00", schedulePortInfo.getScheduleTime());
	}
	
	public void testParseScheduleDetailsScheduleDay() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson);
		assertEquals("123", schedulePortInfo.getDays());
	}
	
	public void testParseScheduleDetailsScheduleMode() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJson);
		assertEquals("a", schedulePortInfo.getMode());
	}
	
	public void testparseScheduleDetailsWithNullParam() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(null);
		assertNull(schedulePortInfo);
	}
	
	public void testparseScheduleDetailsWithEmptyStringParam() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata("");
		assertNull(schedulePortInfo);
	}
	
	public void testparseScheduleDetailsWithWrongParam() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata("{\"temp\":\"1\"}");
		assertNull(schedulePortInfo);
	}
	
	public void testparseScheduleDetailsWithWrongJsonFormat() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata("hello");
		assertNull(schedulePortInfo);
	}
	
	public void testParseScheduleDetailsScheduleCppName() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJsonCpp);
		assertEquals("12:15", schedulePortInfo.getName());
	}
	
	public void testParseScheduleDetailsScheduleCppTime() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJsonCpp);
		assertEquals("12:15", schedulePortInfo.getScheduleTime());
	}
	
	public void testParseScheduleDetailsScheduleCppDay() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJsonCpp);
		assertEquals("123", schedulePortInfo.getDays());
	}
	
	public void testParseScheduleDetailsScheduleCppMode() {
		SchedulePortInfo schedulePortInfo = parseSingleScheduledata(scheduleDetailJsonCpp);
		assertEquals("a", schedulePortInfo.getMode());
	}	
	
	private SchedulePortInfo parseSingleScheduledata(String data){
		ScheduleListPort scheduleListPort = new ScheduleListPort(null, null, null);
		return scheduleListPort.parseResponseAsSingleSchedule(data);
	}
	
	private List<SchedulePortInfo> parseScheduleListdata(String data){
        ScheduleListPort scheduleListPort = new ScheduleListPort(null, null, null);
		return scheduleListPort.parseResponseAsScheduleList(data);
	}
}

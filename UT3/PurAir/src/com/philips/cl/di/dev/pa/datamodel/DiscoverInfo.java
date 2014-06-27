package com.philips.cl.di.dev.pa.datamodel;

public class DiscoverInfo {
	
	private String State = null;
	private String[] ClientIds = null;
	
	public DiscoverInfo() {
		// NOP
	}

	public DiscoverInfo(String state) {
		State = state;
		ClientIds = new String[0];
	}
	
	public String[] getClientIds() {
		return ClientIds;
	}
	
	public boolean isConnected() {
		if (State.toLowerCase().equals("connected")) return true;
		return false;
	}

	public boolean isValid() {
		if (State == null || State.isEmpty()) return false;
		if (!State.toLowerCase().equals("connected") && !State.toLowerCase().equals("disconnected")) return false;
		if (ClientIds == null || ClientIds.length <= 0) return false;
		return true;
	}
	
	public static DiscoverInfo getMarkAllOnlineDiscoverInfo() {
		// No devices offline, will cause all to go online
		return new DiscoverInfo("disconnected");
	}

	public static DiscoverInfo getMarkAllOfflineDiscoverInfo() {
		// No devices online will cause all to go offline
		return new DiscoverInfo("connected");
	}

}

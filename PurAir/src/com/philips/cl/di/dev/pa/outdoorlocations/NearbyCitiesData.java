package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyCitiesData {
	private Map<String, List<LocalityInfo>> nearby_cities = new HashMap<String, List<LocalityInfo>>();
	
	public Map<String, List<LocalityInfo>> getNearbyCitiesMap() {
		return nearby_cities;
	}

	public class LocalityInfo {
		
		private String localityEN;
        private String localityCN;
		private String areaID;
		private String longitude;
		private String latitude;
		public String getLocalityEN() {
			return localityEN;
		}
		public String getLocalityCN() {
			return localityCN;
		}
		public String getAreaID() {
			return areaID;
		}
		public String getLongitude() {
			return longitude;
		}
		public String getLatitude() {
			return latitude;
		}
		
	}
}

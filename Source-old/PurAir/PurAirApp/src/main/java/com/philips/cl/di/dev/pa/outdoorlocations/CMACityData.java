package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.ArrayList;
import java.util.List;

public class CMACityData {
	private List<CMACityDetail> cmaCityDetail = new ArrayList<CMACityDetail>();
	
	public List<CMACityDetail> getCmaCitiesData() {
		return cmaCityDetail;
	}
	
	public class CMACityDetail {
		private String nameEN;
		private String areaID;
		private String longitude;
		private String latitude;
		private String nameCN;
		private String nameTW;
		
		public String getNameEN() {
			return nameEN;
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
		public String getNameCN() {
			return nameCN;
		}
		public String getNameTW() {
			return nameTW;
		}
	}
}

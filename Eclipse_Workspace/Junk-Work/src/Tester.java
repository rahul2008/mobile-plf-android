import com.cdp.horizontal.prx.PRXDataBuilder;

public class Tester {

	public static void main(String[] args) {

		final String COUNTRY_URL = "www.philips.co.uk";
		final String SECTOR = "B2C";
		final String LANGUAGE = "en";
		final String COUNTRY = "GB";
		final String CATALOGCODE = "CONSUMER";
		final String CTN = "RQ1250/17";

		PRXDataBuilder mRequestManager = new PRXDataBuilder();
		mRequestManager.setServerInfo(COUNTRY_URL);
		mRequestManager.setSectorCode(SECTOR);
		mRequestManager.setLanguageCode(LANGUAGE);
		mRequestManager.setCountryCode(COUNTRY);
		mRequestManager.setCatalogCode(CATALOGCODE);
		mRequestManager.setCTN(CTN);

	}

}

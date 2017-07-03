package com.philips.cdp.digitalcare.contactus;
/*
import org.mockito.Mockito;*/

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;

public class CdlsParsingCallbackTest extends InstrumentationTestCase {

	private Context context = null;

	protected void setUp() throws Exception {
		super.setUp();

		context = getInstrumentation().getContext();

	}

	/*public void testonCdlsParsingComplete() {

		CdlsParsingCallback mockparsecallback = new CdlsParsingCallbackTest()

		GetCdlsInstance cdlsInstance = new GetCdlsInstance(mockparsecallback);
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		cdlsInstance.parseCdlsResponse(response);

		Mockito.verify(mockparsecallback).onCdlsParsingComplete(
				Mockito.isA(CdlsResponseModel.class));

	}*/

}

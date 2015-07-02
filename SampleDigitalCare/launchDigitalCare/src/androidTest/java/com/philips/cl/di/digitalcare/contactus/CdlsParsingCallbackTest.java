package com.philips.cl.di.digitalcare.contactus;

import org.mockito.Mockito;

import android.content.Context;
import android.test.InstrumentationTestCase;

public class CdlsParsingCallbackTest extends InstrumentationTestCase {

	private Context context = null;

	protected void setUp() throws Exception {
		super.setUp();

		context = getInstrumentation().getContext();

	}

	public void testonCdlsParsingComplete() {

		CdlsParsingCallback mockparsecallback = Mockito
				.mock(CdlsParsingCallback.class);

		GetCdlsInstance cdlsInstance = new GetCdlsInstance(mockparsecallback);
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		cdlsInstance.parseCdlsResponse(response);

		Mockito.verify(mockparsecallback).onCdlsParsingComplete(
				Mockito.isA(CdlsResponseModel.class));

	}

}

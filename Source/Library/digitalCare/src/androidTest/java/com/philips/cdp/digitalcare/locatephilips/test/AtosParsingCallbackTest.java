package com.philips.cdp.digitalcare.locatephilips.test;
/*

import org.mockito.Mockito;
*/


import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.philips.cdp.digitalcare.locatephilips.models.AtosResponseModel;
import com.philips.cdp.digitalcare.locatephilips.parser.AtosParsingCallback;

public class AtosParsingCallbackTest extends InstrumentationTestCase {

	private Context context = null;

	protected void setUp() throws Exception {
		super.setUp();

		context = getInstrumentation().getContext();

	}

	/*@SmallTest
	public void testonAtosParsingComplete() {

		AtosParsingCallback mockparsercallback = Mockito
				.mock(AtosParsingCallback.class);

		GetAtosInstance atosparseInstance = new GetAtosInstance(
				mockparsercallback);
		String response = AtosParserUtils.loadJSONFromAsset("atos.json",
				context);
		atosparseInstance.parseAtosResponse(response);

		Mockito.verify(mockparsercallback).onAtosParsingComplete(
				Mockito.isA(AtosResponseModel.class));

	}*/

}

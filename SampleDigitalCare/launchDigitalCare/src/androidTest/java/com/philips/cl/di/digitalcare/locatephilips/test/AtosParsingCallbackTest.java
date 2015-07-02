package com.philips.cl.di.digitalcare.locatephilips.test;

import org.mockito.Mockito;

import com.philips.cl.di.digitalcare.locatephilips.AtosParsingCallback;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseModel;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

public class AtosParsingCallbackTest extends InstrumentationTestCase {

	private Context context = null;

	protected void setUp() throws Exception {
		super.setUp();

		context = getInstrumentation().getContext();

	}

	@SmallTest
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

	}

}

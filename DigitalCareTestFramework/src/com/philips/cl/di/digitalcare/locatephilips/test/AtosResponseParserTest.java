package com.philips.cl.di.digitalcare.locatephilips.test;

import org.mockito.Mockito;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.digitalcare.locatephilips.AtosResponseModel;
import com.philips.cl.di.digitalcare.locatephilips.AtosResponseParser;

public class AtosResponseParserTest extends InstrumentationTestCase {

	private final String TAG = AtosResultsModelTest.class.getSimpleName();
	private Context mContext, context = null;
	private AtosResponseParser mParser = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// mContext = getInstrumentation().getTargetContext();
		mContext = Mockito.mock(Context.class);
		context = getInstrumentation().getContext();
		// context = Mockito.mock(Context.class);
		mParser = AtosResponseParser.getParserControllInstance(mContext);

	}

	public void testprocessAtosResponse() {

		mParser.processAtosResponse(getResponse());
		AtosResponseModel Received = mParser.getAtosResponse();

		assertNotNull(Received);

	}

	private String getResponse() {
		String response = AtosParserUtils.loadJSONFromAsset("atos.json",
				context);
		return response;
	}

}

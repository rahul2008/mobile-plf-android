package com.philips.cl.di.digitalcare.contactus;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class CdlsResponseModelTest extends InstrumentationTestCase {

	private final String TAG = CdlsResponseModelTest.class.getSimpleName();

	private Context mContext, context = null;
	private CdlsResponseParser mParser = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Log.d(TAG, "setUp..");
		mContext = getInstrumentation().getTargetContext();
		context = getInstrumentation().getContext();

		mParser = CdlsResponseParser.getParserControllInstance(mContext);
	}
	
	
	
	public void testResponseValidation()
	{
		String response = CdlsParserUtils.loadJSONFromAsset("cdls.json",
				context);
		String received = null;
		mParser.processCdlsResponse(response);
		CdlsResponseModel mResponseModel = null;
		mResponseModel = mParser.getCdlsResponseModel();
		Log.d(TAG, "Response : "+ mResponseModel);
		assertNotNull(mResponseModel);
		
	}

}

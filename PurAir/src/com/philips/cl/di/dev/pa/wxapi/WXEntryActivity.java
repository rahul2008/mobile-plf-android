
package com.philips.cl.di.dev.pa.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R.string;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/*public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub

		String result;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "ok";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "user cancel";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "Auth denied";
			break;
		default:
			result = "defult";
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();

	}

}
*/



public class WXEntryActivity extends WXCallbackActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

}
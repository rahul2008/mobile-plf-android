package com.philips.platform.referenceapp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Arrays;

//This class is tightly coupled with package name .Don't modify package or refactor
//Make sure keep this class in Progaurd

/**
 * Activity with no view to handle any responses or requests from WeChat. This activity can
 * never be moved or renamed or WeChat authentication won't work. Also notice that this
 * activity is being exported in the manifest. Lots of examples shows the WXEntryActivity
 * combined with an activity doing other things related to your app. However, this seemed like a poor
 * separation of functionality as well as something that's going to get ugly if you want interaction
 * with WeChat beyond authentication. Treating this activity as more of a broadcast listener keeps
 * things simple and detached from the rest of your application.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "RegistrationWXEntryActivity";
    private IWXAPI api;

    public void onCreate(Bundle savedInstanceState) {
        RALog.d(TAG," on create called ");
        super.onCreate(savedInstanceState);
        AppFrameworkTagging.getInstance().trackPage(TAG);
        String weChatAppId = "wx5b3bfa4e2970475e";
        // Handle any communication from WeChat and then terminate activity. This class must be an activity
        // or the communication will not be received from WeChat.
        api = WXAPIFactory.createWXAPI(this, weChatAppId, false);
        api.handleIntent(getIntent(), this);
        finish();
    }

    /**
     * Called when WeChat is initiating a request to your application. This is not used for
     * authentication.
     *
     * @param req
     */
    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * Called when WeChat is responding to a request this app initiated. Invoked by WeChat after
     * authorization has been given by the user.
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
        int error_code = resp.errCode;
        String weChatCode = null;
        if (error_code == BaseResp.ErrCode.ERR_OK) {
            try {
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                weChatCode = sendResp.code;
            } catch (Exception e) {
                RALog.e(TAG, Arrays.toString(e.getStackTrace()));
                RALog.e(TAG,"error in  response received ");
            }
        }
        sendMessage(error_code, weChatCode);
    }

    private void sendMessage(int error_code, String weChatCode) {
        Intent intent = new Intent(RegConstants.WE_CHAT_AUTH);
        intent.putExtra(RegConstants.WECHAT_ERR_CODE, error_code);
        intent.putExtra(RegConstants.WECHAT_CODE, weChatCode);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
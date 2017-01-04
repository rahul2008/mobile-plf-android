package com.janrainphilips.philipsregistration.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

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
    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        String weChatAppId = (String) RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("weChatAppId", UR,
                        configError);



        // Handle any communication from WeChat and then terminate activity. This class must be an activity
        // or the communication will not be received from WeChat.
        api = WXAPIFactory.createWXAPI(this, weChatAppId , false);
        api.handleIntent(getIntent(), this);

        finish();
    }


    /**
     * Called when WeChat is initiating a request to your application. This is not used for
     * authentication.
     * @param req
     */
    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * Called when WeChat is responding to a request this app initiated. Invoked by WeChat after
     * authorization has been given by the user.
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {

        //Send code back to fragment by local broadcase
        //Handle this case in Home fragment

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                try {
                    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    sendResp.code;
                    //Send l
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.WECHAT_AUTH);
                } catch(Exception e){
                    Toast.makeText(this, "Exception while parsing token", Toast.LENGTH_LONG).show();
                    Log.e(TAG,e.getStackTrace().toString());
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "User canceled the request", Toast.LENGTH_LONG).show();
                Log.i(TAG, "WeChat - User canceled the request");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "User denied the request", Toast.LENGTH_LONG).show();
                Log.i(TAG, "WeChat - User denied the request");
                break;
        }

    }

}
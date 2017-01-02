/*
 *  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  Copyright (c) 2011, Janrain, Inc.
 *
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *  * Neither the name of the Janrain, Inc. nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.philips.cdp.registration.wechat.philipsregistration;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.utils.LogUtils;
import com.janrainphilips.philipsregistration.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import static com.janrainphilips.philipsregistration.wxapi.WXEntryActivity.code;

//WeChat China

public class MainActivity extends FragmentActivity {

    private boolean flowDownloaded = false;

    //WeChat China
    private static final String TAG = "MainActivity";
    private static final String weChatAppId = "wx855fc0d8fd1ade1d";
    private static final String weChatAppSecret = "12158c0e8cdf0e1446914e0f913f8099";
    private IWXAPI weChatApi;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;


    private class MySignInResultHandler implements Jump.SignInResultHandler, Jump.SignInCodeHandler {

        public void onSuccess() {

            System.out.println("*****onSuccess");
            LogUtils.logd("User Logged in: " + String.valueOf(Jump.getSignedInUser()));

            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            b.setMessage("Sign-in complete.");
            b.setNeutralButton("Dismiss", null);
            AlertDialog alertDialog = b.create();
            alertDialog.show();
        }

        public void onCode(String code) {
            System.out.println("*****onCode" +code);
            Toast.makeText(MainActivity.this, "Authorization Code: " + code, Toast.LENGTH_LONG).show();
        }

        public void onFailure(SignInError error) {
            System.out.println("*****onFailure" +error.reason);
            if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR &&
                    error.captureApiError.isMergeFlowError()) {
                // Called below is the default merge-flow handler. Merge behavior may also be implemented by
                // headless-native-API for more control over the user experience.
                //
                // To do so, call Jump.showSignInDialog or Jump.performTraditionalSignIn directly, and
                // pass in the merge-token and existing-provider-name retrieved from `error`.
                //
                // String mergeToken = error.captureApiError.getMergeToken();
                // String existingProvider = error.captureApiError.getExistingAccountIdentityProvider()
                //
                // (An existing-provider-name of "capture" indicates a conflict with a traditional-sign-in
                // account. You can handle this case yourself, by displaying a dialog and calling
                // Jump.performTraditionalSignIn, or you can call Jump.showSignInDialog(..., "capture") and
                // a library-provided dialog will be provided.)

                Jump.startDefaultMergeFlowUi(MainActivity.this, error, signInResultHandler);
            } else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR &&
                    error.captureApiError.isTwoStepRegFlowError()) {
                // Called when a user cannot sign in because they have no record, but a two-step social
                // registration is possible. (Which means that the error contains pre-filled form fields
                // for the registration form.
                Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
                i.putExtra("preregistrationRecord", prefilledRecord.toString());
                i.putExtra("socialRegistrationToken", error.captureApiError.getSocialRegistrationToken());
                MainActivity.this.startActivity(i);
            } else {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setMessage("Sign-in failure:" + error);
                b.setNeutralButton("Dismiss", null);
                b.show();
            }
        }
    }



    private final MySignInResultHandler signInResultHandler = new MySignInResultHandler();

    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            b.setMessage("Could not download flow.");
            b.setNeutralButton("Dismiss", null);
            b.show();
        }
    };

    private final BroadcastReceiver flowMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            flowDownloaded = true;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String state = extras.getString("message");
                Toast.makeText(MainActivity.this, state, Toast.LENGTH_LONG).show();
            }

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enableStrictMode();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        IntentFilter filter = new IntentFilter(Jump.JR_FAILED_TO_DOWNLOAD_FLOW);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, filter);
        this.registerReceiver(messageReceiver, filter);

        IntentFilter flowFilter = new IntentFilter(Jump.JR_DOWNLOAD_FLOW_SUCCESS);
        LocalBroadcastManager.getInstance(this).registerReceiver(flowMessageReceiver, flowFilter);
        this.registerReceiver(flowMessageReceiver, flowFilter);

        //WeChat China
        // Handle any communication from WeChat and then terminate activity. This class must be an activity
        // or the communication will not be received from WeChat.
        WXEntryActivity.API_ID = weChatAppId;
        weChatApi = WXAPIFactory.createWXAPI(this, weChatAppId, false);
        weChatApi.registerApp(WXEntryActivity.API_ID);
        //weChatApi.handleIntent(getIntent(), this);
        boolean weChatSuccess = weChatApi.registerApp(MainActivity.weChatAppId);

        LogUtils.logd("WeChat: App Registration Status: " + weChatSuccess);
        LogUtils.logd("WeChat: App Installed: " + weChatApi.isWXAppInstalled());
        LogUtils.logd("WeChat: App Support API: " + weChatApi.isWXAppSupportAPI());


        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button weChatAuth = addButton(linearLayout, "WeChat Sign-In");




        sv.addView(linearLayout);
        setContentView(sv);

        weChatAuth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flowDownloaded){
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "123456";
                    weChatApi.sendReq(req);
                } else {
                    Toast.makeText(MainActivity.this, "Flow Configuration not downloaded yet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    //WeChat China
    public void handleWeChatToken(String token, String openId) {
        Log.i(TAG, String.format("Start Janrain Auth - WeChat Token: %s WeChat OpenID: %s",token, openId));
        Jump.startTokenAuthForNativeProvider(MainActivity.this, "wechat", token, openId, MainActivity.this.signInResultHandler, "");
    }

    protected void handleWeChatCode(final String code) {
        Log.i(TAG, String.format("WeChat Code: %s",code));
        String weChatData = String.format("access_token&appid=%s&secret=%s&grant_type=authorization_code&state=123456&code=%s",
                weChatAppId,
                weChatAppSecret,
                code);

        WeChatTokenPostClient wctpClient = new  WeChatTokenPostClient();
        wctpClient.execute(weChatAppId,
                weChatAppSecret,
                code);

    }

    class WeChatTokenPostClient extends AsyncTask<String, Void, String> {
        public String doInBackground(String... PARAMS) {
            String appId = new String(PARAMS[0]);
            String appSecret = new String(PARAMS[1]);
            String code = new String(PARAMS[2]);
            new String(PARAMS[0]);

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("https://api.weixin.qq.com/sns/oauth2/access_token");

                // add header
                post.setHeader("User-Agent", "wechatLoginDemo");
                post.setHeader("Content=Type", "application/x-www-form-urlencoded");

                List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
                urlParameters.add(new BasicNameValuePair("appid", appId));
                urlParameters.add(new BasicNameValuePair("secret", appSecret));
                urlParameters.add(new BasicNameValuePair("code", code));
                urlParameters.add(new BasicNameValuePair("state", "123456"));
                urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));

                post.setEntity(new UrlEncodedFormEntity(urlParameters));

                HttpResponse response = client.execute(post);
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuilder postResult = new StringBuilder();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    postResult.append(line);
                }

                Log.i(TAG, postResult.toString());
                JSONObject wechatData = new JSONObject(postResult.toString());
                final String token = wechatData.getString("access_token");
                final String openId = wechatData.getString("openid");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleWeChatToken(token, openId);
                    }
                });



            } catch (Exception ex) {
                Log.e(TAG, ex.getStackTrace().toString());
            }
            return "";

        }
    }


    private void promptForResendVerificationEmailAddress() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        b.setView(input);
        b.setTitle("Please confirm your email address");
        b.setMessage("We'll resend your verification email.");
        b.setNegativeButton("Cancel", null);
        b.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String emailAddress = input.getText().toString();
                sendVerificationEmail(emailAddress);
            }
        });
        b.show();
    }

    private void sendVerificationEmail(String emailAddress) {
        Jump.resendEmailVerification(emailAddress, new CaptureApiRequestCallback() {

            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Verification email sent.", Toast.LENGTH_LONG).show();
            }

            public void onFailure(CaptureApiError e) {
                Toast.makeText(MainActivity.this, "Failed to send verification email: " + e,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private Button addButton(LinearLayout linearLayout, String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        linearLayout.addView(button);
        return button;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
        LogUtils.loge("onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
    }



    @Override
    protected void onPause() {
        Jump.saveToDisk(this);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Jump.saveToDisk(this);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if(Jump.getCaptureFlowName() != "") flowDownloaded = true;

        //WeChat China

        System.out.println("*******onResume"+code);
        if(code != null){
            String weChatCode = code;
            handleWeChatCode(weChatCode);
            code = null;
        }

    }

    private static void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                        //        .detectDiskReads()
                        //        .detectDiskWrites()
                        //        .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                        //        .penaltyDeath()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                //.detectAll()
                //.detectActivityLeaks()
                //.detectLeakedSqlLiteObjects()
                //.detectLeakedClosableObjects()
                .penaltyLog()
                        //.penaltyDeath()
                .build());
    }


    @Override
    protected void onDestroy() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(flowMessageReceiver);
        super.onDestroy();

    }



}

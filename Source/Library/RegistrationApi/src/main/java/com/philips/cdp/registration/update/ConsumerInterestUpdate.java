
package com.philips.cdp.registration.update;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import com.janrain.android.Jump;
import com.philips.cdp.registration.HttpClient;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.ConsumerInterest;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.UpdateConsumerInterestHandler;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsumerInterestUpdate {

    private final String TAG = "ConsumerInterestUpdate";
    private  String baseUrl;
    private Context mContext;
    public void updateConsumerInterest(Context context, UpdateConsumerInterestHandler updateConsumerInterestHandler,
                                       ArrayList<ConsumerInterest> consumerInterests) {
        baseUrl = UserRegistrationInitializer.getInstance().getRegistrationSettings().getmRegisterBaseCaptureUrl()+"/entity.replace";
        mContext = context;
        startUpdateTask(updateConsumerInterestHandler, convertConsumerArrayToJOSNString(consumerInterests));
    }

    private String convertConsumerArrayToJOSNString(ArrayList<ConsumerInterest> consumerInterests) {
        if (consumerInterests == null || consumerInterests.size() == 0) {
            return null;
        }
        final String SUBJECT_AREA = "subjectArea";
        final String TOPIC_VALUE = "topicValue";
        final String TOPIC_COMMUNICATION_KEY = "topicCommunicationKey";
        final String CAMPAIGN_NAME = "campaignName";
        final String START_ARRAY_SYMB = "[";
        final String END_ARRAY_SYMB = "]";
        final String OPENING_BRACE = "{";
        final String CLOSING_BRACE = "}";
        final String DOUBLE_QUOTE_SYMB = "\"";
        final String KEY_DIVIDER_SYMB = ":";
        final String SPLITTER = ",";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(START_ARRAY_SYMB);
        for (ConsumerInterest interest : consumerInterests) {
            stringBuilder.append(OPENING_BRACE);
            stringBuilder.append(DOUBLE_QUOTE_SYMB + SUBJECT_AREA + DOUBLE_QUOTE_SYMB);
            stringBuilder.append(KEY_DIVIDER_SYMB);
            stringBuilder.append(DOUBLE_QUOTE_SYMB + interest.getSubjectArea() + DOUBLE_QUOTE_SYMB);
            stringBuilder.append(SPLITTER);
            stringBuilder.append(DOUBLE_QUOTE_SYMB + TOPIC_VALUE + DOUBLE_QUOTE_SYMB);
            stringBuilder.append(KEY_DIVIDER_SYMB);
            stringBuilder.append(DOUBLE_QUOTE_SYMB + interest.getTopicValue() + DOUBLE_QUOTE_SYMB);
            stringBuilder.append(SPLITTER);
            stringBuilder.append(DOUBLE_QUOTE_SYMB + TOPIC_COMMUNICATION_KEY + DOUBLE_QUOTE_SYMB);
            stringBuilder.append(KEY_DIVIDER_SYMB);
            stringBuilder.append(DOUBLE_QUOTE_SYMB + interest.getTopicCommunicationKey() + DOUBLE_QUOTE_SYMB);
            stringBuilder.append(SPLITTER);
            stringBuilder.append(DOUBLE_QUOTE_SYMB + CAMPAIGN_NAME + DOUBLE_QUOTE_SYMB);
            stringBuilder.append(KEY_DIVIDER_SYMB);
            stringBuilder.append(DOUBLE_QUOTE_SYMB + interest.getCampaignName() + DOUBLE_QUOTE_SYMB);
            stringBuilder.append(CLOSING_BRACE);
            stringBuilder.append(SPLITTER);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(END_ARRAY_SYMB);
        return stringBuilder.toString();
    }


    private void startUpdateTask(UpdateConsumerInterestHandler updateConsumerInterestHandler, String attributes) {
        String accessToken = Jump.getSignedInUser() != null ? Jump.getSignedInUser()
                .getAccessToken() : null;
        List<Pair<String,String>> nameValuePair = new ArrayList<Pair<String,String>>();

       // List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        final String ATTRIBUTES = "attributes";
        final String ACCESS_TOKEN = "access_token";
        final String INCLUDE_RECORD = "include_record";
        final String ATTRIBUTE_NAME = "attribute_name";
        final String CONSUMER_INTERESTS = "/consumerInterests";
        final String TRUE_VALUE = "true";

        nameValuePair.add(new Pair(ATTRIBUTES, attributes));
        nameValuePair.add(new Pair(ACCESS_TOKEN, accessToken));
        nameValuePair.add(new Pair(INCLUDE_RECORD, TRUE_VALUE));
        nameValuePair.add(new Pair(ATTRIBUTE_NAME, CONSUMER_INTERESTS));

        updateConsumerInterestTask prodRegTask = new updateConsumerInterestTask();
        prodRegTask.url = baseUrl;
        prodRegTask.accessToken = accessToken;
        prodRegTask.nameValuePairs = nameValuePair;
        prodRegTask.updateConsumerInterestHandler = updateConsumerInterestHandler;
        prodRegTask.execute();
    }


    private class updateConsumerInterestTask extends AsyncTask<Void, Void, String> {
        String url;
        List<Pair<String,String>> nameValuePairs;
        UpdateConsumerInterestHandler updateConsumerInterestHandler;
        String accessToken;

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new HttpClient();
            Log.d(TAG, "URL = " + url);
            Log.d(TAG, "Param = " + nameValuePairs);
            Log.d(TAG, "AccessToken = " + accessToken);
            String resultString = httpClient.callPost(url, nameValuePairs, accessToken);
            Log.i(TAG, "Response = " + resultString);
            return resultString;
        }

        @Override
        protected void onPostExecute(String resultString) {
            super.onPostExecute(resultString);
            processResponse(resultString);
        }


        private void processResponse(String resultString) {
            if (resultString == null) {
                updateConsumerInterestHandler.onUpdateConsumerInterestFailedWithError(new com.janrain.android.capture.CaptureApiError(null, null, null));
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(resultString);
                    if ("ok".equals(jsonObject.opt("stat"))) {
                        User user = new User(mContext);
                        user.refreshUser(new RefreshUserHandler() {
                            @Override
                            public void onRefreshUserSuccess() {
                                updateConsumerInterestHandler.onUpdateConsumerInterestSuccess();
                            }

                            @Override
                            public void onRefreshUserFailed(int error) {
                                updateConsumerInterestHandler.onUpdateConsumerInterestFailedWithError(null);
                            }
                        });


                    } else {
                        updateConsumerInterestHandler.onUpdateConsumerInterestFailedWithError(new com.janrain.android.capture.CaptureApiError(jsonObject, null, null));
                    }

                } catch (Exception e) {

                }


            }
        }


    }


}

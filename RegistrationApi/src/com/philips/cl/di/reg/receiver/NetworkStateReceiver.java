/*
 * package com.philips.cl.di.reg.receiver;
 * import android.annotation.SuppressLint;
 * import android.content.BroadcastReceiver;
 * import android.content.Context;
 * import android.content.Intent;
 * import com.philips.cl.di.reg.settings.RegistrationHelper;
 * import com.philips.cl.di.reg.ui.utils.NetworkUtility;
 * import com.philips.cl.di.reg.ui.utils.RLog;
 * public class NetworkStateReceiver extends BroadcastReceiver {
 * @SuppressLint("InlinedApi")
 * @Override
 * public void onReceive(Context context, Intent intent) {
 * if (NetworkUtility.isNetworkAvailable(context)) {
 * RLog.d(RLog.NETWORK_STATE, "Network state : true");
 * RegistrationHelper.getInstance().getNetworkStateListener().onNetWorkStateReceived(true);
 * } else {
 * RLog.d(RLog.NETWORK_STATE, "Network state : false");
 * RegistrationHelper.getInstance().getNetworkStateListener().onNetWorkStateReceived(false);
 * }
 * }
 * }
 */

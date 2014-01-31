 package com.philips.cl.di.discovery;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class DiscoverService implements NsdManager.ResolveListener, NsdManager.DiscoveryListener {
	private NsdServiceInfo mService;
    private NsdManager mNsdManager;
	private DiscoverListener mInterface;

	    
		public DiscoverService(Context aContext, DiscoverListener aInterface) {
			mInterface = aInterface;
			mNsdManager = (NsdManager)aContext.getSystemService(Context.NSD_SERVICE);
		}
		public void startDiscover(){
			mNsdManager.discoverServices(mInterface.getServiceType(), NsdManager.PROTOCOL_DNS_SD, this);
		}

		public void stopDiscover(){
			mNsdManager.stopServiceDiscovery(this);
		}
		
		@Override
        public void onDiscoveryStarted(String regType) {
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            if (service.getServiceType().equals(mInterface.getServiceType())){// && service.getServiceName().contains(mServiceName)){
                mNsdManager.resolveService(service, this);
            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            if (mService == service) {
                mService = null;
            }
        }
        
        @Override
        public void onDiscoveryStopped(String serviceType) {
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
        	String uri = "http://"+serviceInfo.getHost().getHostAddress();
            mInterface.ResolvedDeviceIp(uri);
        }
 }

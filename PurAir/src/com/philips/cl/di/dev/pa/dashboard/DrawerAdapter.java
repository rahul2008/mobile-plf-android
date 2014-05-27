package com.philips.cl.di.dev.pa.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.util.ALog;

import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.View;

public class DrawerAdapter implements DrawerListener {
	
	public enum DrawerEvent { DRAWER_OPENED, DRAWER_CLOSED }
	
	private static DrawerAdapter smInstance;
	
	private List<DrawerEventListener> listeners;

	private DrawerAdapter() {
		listeners = new ArrayList<DrawerEventListener>();
	}
	
	public static DrawerAdapter getInstance() {
		if(smInstance == null) {
			smInstance = new DrawerAdapter();
		}
		return smInstance;
	}
	
	public void addDrawerListener(DrawerEventListener listener) {
		listeners.add(listener);
	}
	
	public void removeDrawerListener(DrawerEventListener listener) {
		listeners.remove(listener);
	}
	
	private void notifyListeners(DrawerEvent event, View drawerView) {
		for(DrawerEventListener listener : listeners) {
			listener.onDrawerEvent(event, drawerView);
		}
	}
	
	@Override
	public void onDrawerClosed(View drawerView) {
		ALog.i(ALog.TEMP, "DrawerManager$onDrawerClosed");
		notifyListeners(DrawerEvent.DRAWER_CLOSED, drawerView);
	}

	@Override
	public void onDrawerOpened(View drawerView) {
		ALog.i(ALog.TEMP, "DrawerManager$onDrawerOpened");
		notifyListeners(DrawerEvent.DRAWER_OPENED, drawerView);
	}

	@Override
	public void onDrawerSlide(View drawerView, float slideOffset) {
		
	}

	@Override
	public void onDrawerStateChanged(int newState) {
		
	}
	
	public interface DrawerEventListener {
		void onDrawerEvent(DrawerEvent event, View drawerView);
	}
}

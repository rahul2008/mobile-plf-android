/*******************************************************************************
 * File name: Device.java
 * Creation date: 2011
 * Author: Bartosz Mikulski, Justyna Staron
 * Change log:
 * 2011-06-14 - Maciej Gorski
 * * removed UDN - replaced with Id
 * 10-06-2011 - Justyna Staron
 * * adding to the constructor used to create objects from database
 * * getting icon from path and establishing path to the icon
 * * writing getters: deviceID, baseURL, UDN, resourceURI, parentDevice
 * 06-06-2011 - Justyna Staron
 * * change of type of icon to Drawable
 * * getter getModelName()
 * * bug fix in equals(Device)
 * * getter getIcon()
 ******************************************************************************/
package com.philips.cl.di.common.ssdp.models;

import java.util.List;

import android.app.LauncherActivity.ListItem;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Device class which keeps device information and services
 * 
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class DeviceModel {

	private static final String LOG_TAG = null;

	public static final String MANUFACTURER_PHILIPS = "philips";

	public static final String MODEL_NAME_TWO_K_NINE = "2k9"; //$NON-NLS-1$

	public static final String MODEL_NAME_TWO_K_TEN = "2k10"; //$NON-NLS-1$

	private String ipAddress;

	private Drawable mIcon;

	private String mId;

	private boolean mIsLocal = false;

	private String mLocation = null;

	private String mNts = null;

	private int mPort;

	private SSDPdevice mSsdpDevice;

	private String mUsn = null;

	/**
	 * constructor used by TwonkyService for discovered UPNPs
	 * 
	 * @param listInfo
	 *            ListItem
	 * @param isNew
	 *            boolean
	 */
	public DeviceModel(final ListItem listInfo, final boolean isNew) {
		ipAddress = "";
	}

	/**
	 * Creates dummy device from SSDP byebye to remove Webremote services
	 * 
	 * @param pUDN
	 *            String
	 */
	public DeviceModel(final String pUDN) {
		mId = pUDN;
	}

	/**
	 * Creates device downloaded from EPG server
	 * 
	 * @param jointspaceId
	 *            String
	 * @param epgId
	 *            String
	 */
	public DeviceModel(final String jointspaceId, final String epgId) {
		mId = jointspaceId;
	}

	/**
	 * Constructor for DeviceModel.
	 * 
	 * @param pNTS
	 *            String
	 * @param pUSN
	 *            String
	 * @param pLOCATION
	 *            String
	 * @param pIpAddress
	 *            String
	 * @param pPort
	 *            int
	 */
	public DeviceModel(final String pNTS, final String pUSN, final String pLOCATION, final String pIpAddress,
	        final int pPort) {
		mNts = pNTS;
		mUsn = pUSN;
		mLocation = pLOCATION;
		ipAddress = pIpAddress;
		mPort = pPort;
	}

	/**
	 * Used to device comparison. In most cases we base comparison on UDN.
	 * Special case is 2k9 which has a variable UDN - IP used instead
	 * 
	 * @param object
	 *            device object to compare
	 * @return false if not the same device
	 */
	@Override
	public boolean equals(final Object object) {
		boolean flag = false;
		if (!(object instanceof DeviceModel)) {
			flag = false;
		}
		final DeviceModel currentObject = (DeviceModel) object;
		if (null != currentObject) {
			if ((mId != null) && mId.equals(currentObject.mId)) {
				flag = true;
			} else if ((ipAddress != null) && ipAddress.equals(currentObject.ipAddress)) {
				flag = true;
			} else if ((mSsdpDevice != null) && (currentObject.getSsdpDevice() != null)) {
				if (mSsdpDevice.getModelName().equals(currentObject.getSsdpDevice().getModelName())
				        || mSsdpDevice.getManufacturer().equals(currentObject.getSsdpDevice().getManufacturer())) {
					// if both devices are 2k9 and they have the same IP address
					// we assume it's the same device
					flag = true;
				}
				flag = false;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * @return icon representing device
	 */
	public Drawable getIcon() {
		return mIcon;
	}

	/**
	 * Method getIconUrl.
	 * 
	 * @param iconList
	 *            List<Icon>
	 * @param baseUrl
	 *            String
	 * @return String
	 */
	public String getIconUrl(final List<Icon> iconList, String baseUrl) {
		int biggestHeight = -1;
		int iconHeight = 0;

		Icon biggestIcon = null;

		for (final Icon iconData : iconList) {
			if (null != iconData) {
				try {
					iconHeight = Integer.valueOf(iconData.height);
					if (biggestHeight < iconHeight) {
						biggestIcon = iconData;
						biggestHeight = iconHeight;
					}
				} catch (final NumberFormatException e) {
					Log.e(LOG_TAG, "NumberFormatException" + e.getMessage());
				}
			}
		}

		if ((null != biggestIcon) && (null != biggestIcon.url)) {
			baseUrl += biggestIcon.url;
		}
		return baseUrl;
	}

	/**
	 * Returns device identification numer.
	 * 
	 * @return String
	 */
	public String getId() {
		return mId;
	}

	/**
	 * @return IP address representing device
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return the mLocation
	 */
	public String getLocation() {
		return mLocation;
	}

	/**
	 * @return the mNts
	 */
	public String getNts() {
		return mNts;
	}

	/**
	 * Method getPort.
	 * 
	 * @return int
	 */
	public int getPort() {
		return mPort;
	}

	/**
	 * Method getSsdpDevice.
	 * 
	 * @return SSDPdevice
	 */
	public SSDPdevice getSsdpDevice() {
		return mSsdpDevice;
	}

	/**
	 * @return the mUsn
	 */
	public String getUsn() {
		return mUsn;
	}

	/**
	 * Method hashCode.
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		int hashcode;
		if (mId != null) {
			hashcode = mId.hashCode();
		} else {
			hashcode = super.hashCode();
		}
		return hashcode;
	}

	/**
	 * Method isLocal.
	 * 
	 * @return boolean
	 */
	public boolean isLocal() {
		return mIsLocal;
	}

	/**
	 * Method isPhilipsDevice.
	 * 
	 * @return boolean
	 */
	public final boolean isPhilipsDevice() {
		Boolean isPhilipsDevice = false;
		final String manufacturer = mSsdpDevice.getManufacturer();
		if (null != manufacturer) {
			isPhilipsDevice = manufacturer.toLowerCase().contains(MANUFACTURER_PHILIPS);
		}
		return isPhilipsDevice;
	}

	/**
	 * Device constructor for SSDP discovery
	 * 
	 * @param pSSDPdevice
	 *            delivered by SSDP discovery service
	 */
	@SuppressWarnings("deprecation")
    public void setFromSSDP(final SSDPdevice pSSDPdevice) {
		mSsdpDevice = pSSDPdevice;
		if (null != pSSDPdevice) {
			mId = pSSDPdevice.getUdn();
			mIsLocal = false;
			mIcon = new BitmapDrawable();

			if (isPhilipsDevice()) {
				pSSDPdevice.getPresentationURL();
				if (!pSSDPdevice.getPresentationURL().contains("http://")) {
					final StringBuilder builder = new StringBuilder(mSsdpDevice.getBaseURL());
					builder.append(pSSDPdevice.getPresentationURL());
					builder.append(pSSDPdevice.getXScreen());
					mSsdpDevice.setBaseURL(builder.toString());
				} else {
					pSSDPdevice.setPresentationURL(pSSDPdevice.getPresentationURL() + pSSDPdevice.getXScreen());
				}
			}
		}
	}

	/**
	 * Method setIcon.
	 * 
	 * @param drawable
	 *            Drawable
	 */
	void setIcon(final Drawable drawable) {
		mIcon = drawable;
	}

	/**
	 * @param pId
	 *            the mId to set
	 */
	public void setId(final String pId) {
		mId = pId;
	}

	/**
	 * @param pIpAddress
	 *            the mIpAddress to set
	 */
	public void setIpAddress(final String pIpAddress) {
		ipAddress = pIpAddress;
	}

	/**
	 * @param pIsLocal
	 *            the mIsLocal to set
	 */
	public void setIsLocal(final boolean pIsLocal) {
		mIsLocal = pIsLocal;
	}

	/**
	 * @param pLocation
	 *            the mLocation to set
	 */
	public void setLocation(final String pLocation) {
		mLocation = pLocation;
	}

	/**
	 * @param pNts
	 *            the mNts to set
	 */
	public void setNts(final String pNts) {
		mNts = pNts;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(final int port) {
		mPort = port;
	}

	/**
	 * Method setSsdpDevice.
	 * 
	 * @param pSsdpDevice
	 *            SSDPdevice
	 */
	public void setSsdpDevice(final SSDPdevice pSsdpDevice) {
		mSsdpDevice = pSsdpDevice;
	}

	/**
	 * @param pUsn
	 *            the mUsn to set
	 */
	public void setUsn(final String pUsn) {
		mUsn = pUsn;
	}

	/**
	 * printing method used for debugging
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("[mUsn:").append(mUsn);
		builder.append("][mNts:").append(mNts).append(']');
		builder.append("[mLocation:").append(mLocation).append("][ mIpAddress:");
		builder.append(ipAddress).append("][ port:").append(mPort);
		builder.append("][SSDP ").append(mSsdpDevice).append(']'); //$NON-NLS-1$
		return builder.toString();
	}

}

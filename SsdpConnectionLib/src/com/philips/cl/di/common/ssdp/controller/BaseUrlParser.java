package com.philips.cl.di.common.ssdp.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;
import com.philips.cl.di.common.ssdp.contants.XmlParserConstants;
import com.philips.cl.di.common.ssdp.models.Icon;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

/*
 * Parser for the base URL currently parser only for icons
 */
/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class BaseUrlParser {

	/*
	 * Parser that does the whole job
	 */
	/**
	 * @author 310151556
	 */
	class BaseUrlXmlHandler extends DefaultHandler {
		private int devicesCount = -1;
		private Icon icon;

		private boolean insideIconList = false;
		private boolean isSmallScreen = false;
		private SSDPdevice mSsdpDevice;
		/**
		 * Method startDocument.
		 * 
		 * @throws SAXException
		 *             * @see org.xml.sax.ContentHandler#startDocument() * @see
		 *             org.xml.sax.ContentHandler#startDocument()
		 */
		private StringBuilder mStringBuilder;

		/**
		 * Method characters.
		 * 
		 * @param ch
		 *            char[]
		 * @param start
		 *            int
		 * @param length
		 *            int
		 *            sax.ContentHandler#characters(char[], int, int) * * @see
		 *            org.xml.sax.ContentHandler#characters(char[], int, int) * * @see
		 *            org.xml.sax.ContentHandler#characters(char[], int, int)
		 * @throws SAXException
		 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(final char[] ch, final int start, final int length)
		        throws SAXException {
			super.characters(ch, start, length);
			mStringBuilder.append(ch, start, length);
		}

		/**
		 * Method endElement.
		 * 
		 * @param uri
		 *            String
		 * @param localName
		 *            String
		 * @param qName
		 *            String
		 *            entHandler#endElement(String, String, String) * @see
		 *            org.xml.sax.ContentHandler#endElement(String, String, String) * @see
		 *            org.xml.sax.ContentHandler#endElement(String, String, String)
		 * @throws SAXException
		 * @see org.xml.sax.ContentHandler#endElement(String, String, String)
		 */
		@Override
		public void endElement(final String uri, final String localName, final String qName)
		        throws SAXException {
			// TODO: remove .replaceAll(REGEXP, ""); and make one regexp for the whole xmlToParse
			super.endElement(uri, localName, qName);
			if (devicesCount >= 0) {
				if (isEqual(localName, XmlParserConstants.ICON_LIST)) {
					insideIconList = false;
				} else {
					final String modifiedString = mStringBuilder.toString().replaceAll(XmlParserConstants.REGEXP, "");
					if (insideIconList && (null != icon)) {
						if (isEqual(localName, XmlParserConstants.MIME)) {
							icon.mimeType = modifiedString;
						} else if (isEqual(localName, XmlParserConstants.HEIGHT)) {
							icon.height = modifiedString;
						} else if (isEqual(localName, XmlParserConstants.WIDTH)) {
							icon.width = modifiedString;
						} else if (isEqual(localName, XmlParserConstants.DEPTH)) {
							icon.depth = modifiedString;
						} else if (isEqual(localName, XmlParserConstants.URL)) {
							icon.url = modifiedString;
						} else if (isEqual(localName, XmlParserConstants.ICON)) {
							mSsdpDevice.getIconList().add(icon);
							icon = null;
						}
					} else if (isEqual(localName, XmlParserConstants.PRESENTATION_URL)) {
						mSsdpDevice.setPresentationURL(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.DEVICE_TYPE)) {
						mSsdpDevice.setDeviceType(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.FRIENDLY_NAME)) {
						mSsdpDevice.setFriendlyName(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.MANUFACTURER)) {
						mSsdpDevice.setManufacturer(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.MANUFACTURER_URL)) {
						mSsdpDevice.setManufacturerURL(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.MODEL_DESCRIPTION)) {
						mSsdpDevice.setModelDescription(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.MODEL_NAME)) {
						mSsdpDevice.setModelName(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.MODEL_NUMBER)) {
						mSsdpDevice.setModelNumber(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.UDN)) {
						mSsdpDevice.setUdn(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.UPC)) {
						mSsdpDevice.setUPC(modifiedString);
					} else if (isEqual(localName, XmlParserConstants.X_SCREEN) && isSmallScreen) {
						mSsdpDevice.setXScreen(modifiedString);
						isSmallScreen = false;
					} else if (isEqual(localName, XmlParserConstants.DEVICE)) {
						devicesCount--;
						if (devicesCount >= 0) {
							mSsdpDevice = devicesList.get(devicesCount);
						} else if (isEqual(localName, XmlParserConstants.FRIENDLY_NAME)) {
							mSsdpDevice.setFriendlyName(modifiedString);
						}
					}
				}
			}
			mStringBuilder.setLength(0);
		}

		private boolean isEqual(final String first, final String second) {
			boolean flag = false;
			if ((null != first) && first.equalsIgnoreCase(second)) {
				flag = true;
			}
			return flag;

		}

		/**
		 * Method startDocument.
		 * 
		 * @throws SAXException
		 *             * @see org.xml.sax.ContentHandler#startDocument()
		 */
		@Override
		public void startDocument()
		        throws SAXException {
			super.startDocument();

			mStringBuilder = new StringBuilder(16);
			devicesList = new ArrayList<SSDPdevice>();
		}

		/**
		 * Method startElement.
		 * 
		 * @param uri
		 *            String
		 * @param localName
		 *            String
		 * @param qName
		 *            String
		 * @param attributes
		 *            Attributes
		 *            String,
		 *            String, Attributes) * @see org.xml.sax.ContentHandler#startElement(String,
		 *            String, String,
		 *            Attributes) * @see org.xml.sax.ContentHandler#startElement(String, String,
		 *            String, Attributes)
		 * @throws SAXException
		 * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
		 */
		@Override
		public void startElement(final String uri, final String localName, final String qName,
		        final Attributes attributes)
		        throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (isEqual(localName, XmlParserConstants.ICON_LIST)) {
				insideIconList = true;
			} else if (isEqual(localName, XmlParserConstants.ICON) && insideIconList && (null == icon)) {
				icon = new Icon();
			} else if (isEqual(localName, XmlParserConstants.DEVICE)) {
				mSsdpDevice = new SSDPdevice();
				mSsdpDevice.setIconList(new ArrayList<Icon>());
				isSmallScreen = false;
				devicesList.add(mSsdpDevice);
				devicesCount++;
			} else if (isEqual(localName, XmlParserConstants.X_SCREEN)) {
				if (null != attributes) {
					final int length = attributes.getLength();
					for (int i = 0; i < length; i++) {
						if (attributes.getQName(i).equals(XmlParserConstants.X_SCREEN_ATTRIBUTE)
						        && attributes.getValue(i).equals(XmlParserConstants.X_SCREEN_VALUE)) {
							isSmallScreen = true;
							break;
						}
					}
				}
			}
		}
	}

	private List<SSDPdevice> devicesList;

	/**
	 * Method getDevicesList.
	 * 
	 * @return List<SSDPdevice>
	 */
	public List<SSDPdevice> getDevicesList() {
		return devicesList;
	}

	/**
	 * Starts parsing of the string object pointed by xmlToParse
	 * Results of parsing are available in iconList
	 * 
	 * @param xmlToParse
	 *            String
	 */
	public void parse(final String xmlToParse) {
		if (xmlToParse != null) {
			InputStream inputStream = null;
			try {
				final SAXParserFactory factory = SAXParserFactory.newInstance();
				if (null != factory) {
					final SAXParser parser = factory.newSAXParser();
					if (null != parser) {
						inputStream = new ByteArrayInputStream(xmlToParse.getBytes("ISO-8859-15"));
						final BaseUrlXmlHandler baseUrlXmlHandler = new BaseUrlXmlHandler();
						parser.parse(inputStream, baseUrlXmlHandler);
					}
				}
			} catch (final ParserConfigurationException e) {
				Log.e(ConnectionLibContants.LOG_TAG, "ParserConfigurationException " + e.getMessage());
			} catch (final SAXException e) {
				Log.e(ConnectionLibContants.LOG_TAG, "SAXException " + e.getMessage());
			} catch (final IOException e) {
				Log.e(ConnectionLibContants.LOG_TAG, "IOException " + e.getMessage());
			} finally {
				try {
					if (null != inputStream) {
						inputStream.close();
					}
				} catch (final IOException e) {
					Log.e(ConnectionLibContants.LOG_TAG, "IOException " + e.getMessage());
				}
			}
		}
	}
}

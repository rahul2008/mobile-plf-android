package com.philips.cl.di.common.ssdp.controller;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.models.DeviceModel;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class MessageController implements MessageNotifier {

	/**
	 * Constructor for MessageController.
	 * 
	 * @param callback
	 *            Callback
	 */

	private static MessageController controller = null;

	/**
	 * Field LOG.
	 */
	private static final String LOG = MessageController.class.getSimpleName();

	/**
	 * Method getInstance.
	 * 
	 * @return MessageController
	 */
	public static MessageController getInstance() {
		if (controller == null) {
			controller = new MessageController();
		}
		return controller;
	}

	/**
	 * Field messageHandlers.
	 */
	private final List<Handler> messageHandlers;

	/**
	 * Field myHandler.
	 */
	private Handler myHandler = null;

	private MessageController() {
		messageHandlers = new ArrayList<Handler>();
	}

	/**
	 * Method addMessageHandler.
	 * 
	 * @param handler
	 *            Handler
	 * @see com.philips.cl.di.common.ssdp.controller.MessageNotifier#addMessageHandler(Handler)
	 */
	@Override
	public void addMessageHandler(final Handler handler) {
		
		if (!messageHandlers.contains(handler)) {
			messageHandlers.add(handler);
		}
	}

	/**
	 * Forwards internal message to other objects. Use only when sending InternalMessage
	 * in Messgae.obj
	 * 
	 * @param message
	 *            Message
	 */
	public void forwardMessage(final Message message) {
		InternalMessage msg = null;
		boolean exceptioncaught = false;
		try {
			if (null != message) {
				msg = (InternalMessage) message.obj;
			}
		} catch (final Exception e) {
			Log.d(LOG, "InternalMessage m = (InternalMessage) message.obj  --> Exception" + e.getMessage());
			exceptioncaught = true;
		}
		if (!exceptioncaught) {
			// what message was NOT yet sent by this message controller
			if ((null != msg) && !msg.isHandlerRegistered(myHandler)) {
				for (final Handler handler : messageHandlers) {
					if (null != handler) {
						handler.sendMessage(Message.obtain(message));
					}
				}
				msg.registerHandler(myHandler);
			} else {
				// Log.d(LOG,
				// String.format("MessageContoller Message name=%s was already send by this MessageController",message.what));
			}
			// used to free object for later reuse
			if (null != message) {
				message.recycle();
			}
		}
	}

	/**
	 * Method getHandler.
	 * 
	 * @return Handler
	 */
	public Handler getHandler() {
		return myHandler;
	}

	/**
	 * Method removeMessageHandler.
	 * Removes the handler object from the observer list
	 * 
	 * @param handler
	 *            Handler
	 * @see com.philips.cl.di.common.ssdp.controller.MessageNotifier#removeMessageHandler(Handler)
	 */
	@Override
	public void removeMessageHandler(final Handler handler) {
		Log.d(ConnectionLibContants.LOG_TAG, "Remove message handler. ");
		messageHandlers.clear() ;
	}

	/**
	 * Method sendInternalMessage.
	 * 
	 * @param messageID
	 *            MessageID
	 */
	public void sendInternalMessage(final DiscoveryMessageID messageID) {
		if (null != messageID) {
			sendInternalMessageWithID(messageID.ordinal(), null);
		}
	}

	/**
	 * Method sendInternalMessage.
	 * 
	 * @param messageID
	 *            MessageID
	 * @param arg1
	 *            int
	 * @param arg2
	 *            int
	 */
	public void sendInternalMessage(final DiscoveryMessageID messageID, final int arg1, final int arg2) {
		if (null != messageID) {
			final InternalMessage msg = new InternalMessage();
			msg.obj = null;
			msg.what = messageID.ordinal();
			msg.registerHandler(myHandler);

			final Message message = Message.obtain();
			if (null != message) {
				message.what = messageID.ordinal();
				message.obj = msg;
				message.arg1 = arg1;
				message.arg2 = arg2;
			}
			for (final Handler handler : messageHandlers) {
				if (null != handler) {
					handler.sendMessage(Message.obtain(message));
				}
			}

			// used to free object for later reuse
			if (null != message) {
				message.recycle();
			}
		}
	}

	/**
	 * Method sendInternalMessage.
	 * 
	 * @param messageID
	 *            MessageID
	 * @param obj
	 *            Object
	 */
	public void sendInternalMessage(final DiscoveryMessageID messageID, final Object obj) {
		if (null != messageID) {
			sendInternalMessageWithID(messageID.ordinal(), obj);
		}
	}

	/**
	 * Method sendInternalMessage.
	 * 
	 * @param what
	 *            int
	 * @param obj
	 *            Object
	 */
	private void sendInternalMessageWithID(final int what, final Object obj) {
		final InternalMessage msg = new InternalMessage();
		msg.obj = obj;
		msg.what = what;
		msg.registerHandler(myHandler);

		final Message message = Message.obtain();
		if (null != message) {
			message.what = what;
			message.obj = msg;
			final DeviceModel device = (DeviceModel) obj;
			if ((null != device) && (device.getSsdpDevice() != null)) {
				final Bundle bundle = new Bundle();
				bundle.putString(ConnectionLibContants.XML_KEY, device.getSsdpDevice().getBaseURL());
				bundle.putString(ConnectionLibContants.IP_KEY, device.getIpAddress());
				bundle.putInt(ConnectionLibContants.PORT_KEY, Integer.valueOf(device.getPort()));
				message.setData(bundle);
			}
			for (final Handler handler : messageHandlers) {
				Log.e(ConnectionLibContants.LOG_TAG, "messageHandlers size= " + messageHandlers.size());
				if (null != handler) {
					handler.sendMessage(Message.obtain(message));
				}
			}
			// used to free object for later reuse
			message.recycle();
		}
	}

	/**
	 * Method setCallback.
	 * 
	 * @param cb
	 *            Callback
	 */
	public void setCallback(final Callback cb) {
		if ( messageHandlers != null && messageHandlers.size() > 0) {
			messageHandlers.clear();
			Log.e(ConnectionLibContants.LOG_TAG, "All ready messageHandlers having message.");
		}
		myHandler = new Handler(cb);
		messageHandlers.add(myHandler);
	}
}

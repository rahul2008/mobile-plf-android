package com.philips.cl.di.common.ssdp.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.cdp2.commlib.core.util.HandlerProvider;
import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;
import com.philips.cl.di.common.ssdp.contants.DiscoveryMessageID;
import com.philips.cl.di.common.ssdp.models.DeviceModel;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
@Deprecated
public class MessageController implements MessageNotifier {

    /**
     * Constructor for MessageController.
     *
     * @param callback
     * Callback
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
    private final Set<Handler> messageHandlers;

    /**
     * Field handler.
     */
    private Handler handler = null;

    private MessageController() {
        messageHandlers = new CopyOnWriteArraySet<>();
    }

    /**
     * Method addMessageHandler.
     *
     * @param handler Handler
     * @see MessageNotifier#addMessageHandler(Handler)
     */
    @Override
    public void addMessageHandler(final Handler handler) {
        messageHandlers.add(handler);
    }

    /**
     * Forwards internal message to other objects. Use only when sending InternalMessage
     * in Messgae.obj
     *
     * @param message Message
     */
    public void forwardMessage(final @NonNull Message message) {
        InternalMessage msg = null;
        try {
            msg = (InternalMessage) message.obj;
        } catch (final Exception e) {
            Log.d(LOG, "InternalMessage m = (InternalMessage) message.obj --> Exception" + e.getMessage());
        }

        // what message was NOT yet sent by this message controller
        if ((null != msg) && !msg.isHandlerRegistered(handler)) {
            for (final Handler handler : messageHandlers) {
                if (null != handler) {
                    handler.sendMessage(Message.obtain(message));
                }
            }
            msg.registerHandler(handler);
        } else {
            Log.d(LOG, String.format("MessageController message name=%s was already sent by this MessageController", message.what));
        }
        message.recycle();
    }

    /**
     * Method getHandler.
     *
     * @return Handler
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * Method removeMessageHandler.
     * Removes the handler object from the observer list
     *
     * @param handler Handler
     * @see MessageNotifier#removeMessageHandler(Handler)
     */
    @Override
    public void removeMessageHandler(final Handler handler) {
        Log.d(ConnectionLibContants.LOG_TAG, "Remove message handler. ");

        messageHandlers.remove(handler);
    }

    /**
     * Method sendInternalMessage.
     *
     * @param messageID MessageID
     */
    public void sendInternalMessage(final @NonNull DiscoveryMessageID messageID) {
        sendInternalMessageWithID(messageID.ordinal(), null);
    }

    /**
     * Method sendInternalMessage.
     *
     * @param messageID MessageID
     * @param arg1      int
     * @param arg2      int
     */
    public void sendInternalMessage(final @NonNull DiscoveryMessageID messageID, final int arg1, final int arg2) {
        final InternalMessage msg = new InternalMessage();
        msg.obj = null;
        msg.what = messageID.ordinal();
        msg.registerHandler(handler);

        final Message message = Message.obtain();
        message.what = messageID.ordinal();
        message.obj = msg;
        message.arg1 = arg1;
        message.arg2 = arg2;

        for (final Handler handler : messageHandlers) {
            if (null != handler) {
                handler.sendMessage(Message.obtain(message));
            }
        }
        message.recycle();
    }

    /**
     * Method sendInternalMessage.
     *
     * @param messageID MessageID
     * @param obj       Object
     */
    public void sendInternalMessage(final @NonNull DiscoveryMessageID messageID, @NonNull final Object obj) {
        sendInternalMessageWithID(messageID.ordinal(), obj);
    }

    /**
     * Method sendInternalMessage.
     *
     * @param what int
     * @param obj  Object
     */
    private void sendInternalMessageWithID(final int what, final @NonNull Object obj) {
        final InternalMessage msg = new InternalMessage();
        msg.obj = obj;
        msg.what = what;
        msg.registerHandler(handler);

        final Message message = Message.obtain();
        message.what = what;
        message.obj = msg;

        final DeviceModel device = (DeviceModel) obj;

        if (device.getSsdpDevice() != null) {
            final Bundle bundle = new Bundle();
            bundle.putString(ConnectionLibContants.XML_KEY, device.getSsdpDevice().getBaseURL());
            bundle.putString(ConnectionLibContants.IP_KEY, device.getIpAddress());
            bundle.putInt(ConnectionLibContants.PORT_KEY, device.getPort());

            message.setData(bundle);
        }

        for (final Handler handler : messageHandlers) {
            if (handler != null) {
                handler.sendMessage(Message.obtain(message));
            }
        }
        message.recycle();
    }

    /**
     * Method setCallback.
     *
     * @param cb Callback
     */
    public void setCallback(final Callback cb) {
        if (messageHandlers.size() > 0) {
            messageHandlers.clear();
            Log.e(ConnectionLibContants.LOG_TAG, "All ready messageHandlers having message.");
        }
        handler = HandlerProvider.createHandler(cb);

        messageHandlers.add(handler);
    }
}

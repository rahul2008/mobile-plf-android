/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.dicommsupport.exceptions.IncompleteMessageException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidMessageTerminationException;
import com.philips.pins.shinelib.dicommsupport.exceptions.StartBytesNotFoundException;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.nio.ByteBuffer;

import static android.content.ContentValues.TAG;

public class DiCommByteStreamReader {

    @NonNull
    private final DiCommMessageListener mListener;
    private byte[] mReceiveBuffer = new byte[0];

    public interface DiCommMessageListener {
        void onMessage(DiCommMessage message);

        void onError(String errorMessage);
    }

    public DiCommByteStreamReader(@NonNull DiCommMessageListener listener) {
        mListener = listener;
    }

    public void onBytes(byte[] data) {
        appendReceivedData(data);

        while (mReceiveBuffer.length > 0) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(mReceiveBuffer);

            try {
                final DiCommMessage diCommMessage = new DiCommMessage(byteBuffer);
                mListener.onMessage(diCommMessage);

                reduceReceivedBuffer(byteBuffer);
            } catch (StartBytesNotFoundException e) {
                final String msg = "Error parsing incoming message, start bytes not found. Some information may be lost.";
                SHNLogger.e(TAG, msg);
                reduceReceivedBuffer(byteBuffer);
            } catch (InvalidMessageTerminationException e) {
                final String msg = "Invalid message termination.";
                SHNLogger.e(TAG, msg);
                mListener.onError(msg);
                reduceReceivedBuffer(byteBuffer);
            } catch (IncompleteMessageException ignored) {
                break;
            }
        }
    }

    private void reduceReceivedBuffer(ByteBuffer byteBuffer) {
        synchronized (mReceiveBuffer) {
            byte[] reducedBuffer = new byte[byteBuffer.remaining()];
            byteBuffer.get(reducedBuffer);
            mReceiveBuffer = reducedBuffer;
        }
    }

    private void appendReceivedData(byte[] data) {
        synchronized (mReceiveBuffer) {
            byte[] newReceivedBuffer = new byte[mReceiveBuffer.length + data.length];
            ByteBuffer byteBuffer = ByteBuffer.wrap(newReceivedBuffer);

            byteBuffer.put(mReceiveBuffer);
            byteBuffer.put(data);
            mReceiveBuffer = newReceivedBuffer;
        }
    }
}

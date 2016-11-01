/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.dicommsupport.exceptions.IncompleteMessageException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidPayloadLengthException;
import com.philips.pins.shinelib.dicommsupport.exceptions.StartBytesNotFoundException;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.nio.ByteBuffer;

import static android.content.ContentValues.TAG;

public class DiCommByteStreamReader {

    @NonNull
    private final DiCommMessageListener mListener;
    private byte[] mReceiveBuffer = new byte[0];

    interface DiCommMessageListener {
        void onMessage(DiCommMessage message);
    }

    public DiCommByteStreamReader(@NonNull DiCommMessageListener listener) {
        mListener = listener;
    }

    public void onBytes(byte[] data) {
        appendReceivedData(data);

        while (mReceiveBuffer.length > 0) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(mReceiveBuffer);

            try {
                DiCommMessage diCommMessage = new DiCommMessage(byteBuffer);
                mListener.onMessage(diCommMessage);

                reduceReceivedBuffer(byteBuffer);
            } catch (StartBytesNotFoundException e) {
                SHNLogger.e(TAG, "Error parsing incoming message, start bytes not found. Some information may be lost.");
                reduceReceivedBuffer(byteBuffer);
            } catch (InvalidPayloadLengthException e) {
                SHNLogger.e(TAG, "Invalid payload length in message.");
                reduceReceivedBuffer(byteBuffer);
            } catch (IncompleteMessageException e) {
                SHNLogger.d(TAG, "Incomplete message.");
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

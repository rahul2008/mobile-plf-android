package com.philips.pins.shinelib.services;

import android.util.Log;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.SHNStringResultListener;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNServiceUserData implements SHNService.SHNServiceListener {

    private static final String TAG = SHNServiceUserData.class.getSimpleName();
    private static final boolean LOGGING = false;

    public static final UUID DATABASE_CHANGE_INDEX_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A99));
    public static final UUID USER_INDEX_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9A));
    public static final UUID USER_CONTROL_POINT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9F));

    public static final UUID SERVICE_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x181C));
    public static final UUID FIRST_NAME_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A8A));
    public static final UUID LAST_NAME_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A90));
    public static final UUID AGE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A80));

    private static final byte OP_CODE_REGISTER_NEW_USER = (byte) 0x01;
    private static final byte OP_CODE_CONSENT = (byte) 0x02;
    private static final byte OP_CODE_DELETE_USER_DATA = (byte) 0x03;
    private static final byte OP_CODE_RESPONSE = (byte) 0x20;

    private static final byte RESPONSE_CODE_SUCCESS = (byte) 0x01;
    private static final byte RESPONSE_CODE_OP_CODE_NOT_SUPPORTED = (byte) 0x02;
    private static final byte RESPONSE_CODE_INVALID_PARAMETER = (byte) 0x03;
    private static final byte RESPONSE_CODE_OPERATION_FAILED = (byte) 0x04;
    private static final byte RESPONSE_CODE_USER_NOT_AUTHORIZED = (byte) 0x05;

    private SHNService shnService;

    protected LinkedList<SHNUserDataCommand> commandQueue;
    protected boolean executing;

    protected SHNCharacteristic.SHNCharacteristicChangedListener shnCharacteristicChangedListener = new SHNCharacteristic.SHNCharacteristicChangedListener() {
        @Override
        public void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data) {
            if (executing) {
                SHNUserDataCommand command = commandQueue.getFirst();

                ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                try {
                    if (byteBuffer.get() == OP_CODE_RESPONSE) { // match the starting command with received response command
                        if (command.getType() == SHNUserDataCommand.Command.REGISTER && byteBuffer.get() == OP_CODE_REGISTER_NEW_USER) {
                            extractValueAndNotifyListener(byteBuffer, command);
                        } else if (command.getType() == SHNUserDataCommand.Command.CONSENT && byteBuffer.get() == OP_CODE_CONSENT) {
                            extractResultAndNotifyListener(byteBuffer, command);
                        } else if (command.getType() == SHNUserDataCommand.Command.DELETE && byteBuffer.get() == OP_CODE_DELETE_USER_DATA) {
                            extractResultAndNotifyListener(byteBuffer, command);
                        } else {
                            command.notifyListeners(SHNResult.SHNInvalidResponseError);
                        }
                    } else {
                        command.notifyListeners(SHNResult.SHNInvalidResponseError);
                    }
                } catch (BufferUnderflowException ex) { // is this good?
                    command.notifyListeners(SHNResult.SHNResponseIncompleteError);
                } finally { // and this?
                    removeCurrentCommandAndStartNext();
                }
            } else {
                Log.w(TAG, "Notification is received with no request");
            }
        }
    };

    private void extractValueAndNotifyListener(ByteBuffer byteBuffer, SHNUserDataCommand command) {
        int userId = -1;
        SHNResult result = getSHNResult(byteBuffer.get());
        if (result == SHNResult.SHNOk) {
            userId = ScalarConverters.ubyteToInt(byteBuffer.get());
        }
        command.notifyListeners(userId, result);
    }

    private void extractResultAndNotifyListener(ByteBuffer byteBuffer, SHNUserDataCommand command) {
        SHNResult result = getSHNResult(byteBuffer.get());
        command.notifyListeners(result);
    }

    private SHNResult getSHNResult(byte byteResult) {
        switch (byteResult) {
            case RESPONSE_CODE_SUCCESS:
                return SHNResult.SHNOk;
            case RESPONSE_CODE_OP_CODE_NOT_SUPPORTED:
                return SHNResult.SHNUnsupportedOperation;
            case RESPONSE_CODE_INVALID_PARAMETER:
                return SHNResult.SHNInvalidParameterError;
            case RESPONSE_CODE_OPERATION_FAILED:
                return SHNResult.SHNOperationFailed;
            case RESPONSE_CODE_USER_NOT_AUTHORIZED:
                return SHNResult.SHNUserNotAuthorized;
            default:
                return SHNResult.SHNInvalidResponseError;
        }
    }

    public SHNServiceUserData(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(SERVICE_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        shnService.registerSHNServiceListener(this);
        commandQueue = new LinkedList<>();
    }

    private Set<UUID> getOptionalCharacteristics() {
        Set<UUID> optionalCharacteristics = new HashSet<>();
        optionalCharacteristics.add(FIRST_NAME_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(LAST_NAME_CHARACTERISTIC_UUID);
        optionalCharacteristics.add(AGE_CHARACTERISTIC_UUID);
        return optionalCharacteristics;
    }

    private Set<UUID> getRequiredCharacteristics() {
        Set<UUID> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(DATABASE_CHANGE_INDEX_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(USER_INDEX_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        return requiredCharacteristicUUIDs;
    }

    public SHNService getShnService() {
        return shnService;
    }

    //implements SHNService.SHNServiceListener
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (state == SHNService.State.Available) {
            shnService.transitionToReady();
            SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(USER_CONTROL_POINT_CHARACTERISTIC_UUID);
            shnCharacteristic.setNotification(true, null);
            shnCharacteristic.setShnCharacteristicChangedListener(shnCharacteristicChangedListener);
        }
    }

    public void getFirstName(final SHNStringResultListener listener) {
        getStringCharacteristic(listener, FIRST_NAME_CHARACTERISTIC_UUID);
    }

    public void getLastName(final SHNStringResultListener listener) {
        getStringCharacteristic(listener, LAST_NAME_CHARACTERISTIC_UUID);
    }

    public void setFirstName(final String name, final SHNResultListener listener) {
        setStringCharacteristic(name, listener, FIRST_NAME_CHARACTERISTIC_UUID);
    }

    public void setLastName(final String name, final SHNResultListener listener) {
        setStringCharacteristic(name, listener, LAST_NAME_CHARACTERISTIC_UUID);
    }

    public void setAge(int age, SHNResultListener listener) {
        setUIntCharacteristic(1, age, listener, AGE_CHARACTERISTIC_UUID);
    }

    public void incrementDatabaseIndex(final SHNResultListener listener) {
        getDatabaseIndex(new SHNIntegerResultListener() {
            @Override
            public void onActionCompleted(int value, SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    setDatabaseIndex(value + 1, listener);
                } else {
                    listener.onActionCompleted(result);
                }
            }
        });
    }

    private void getDatabaseIndex(final SHNIntegerResultListener listener) {
        if (LOGGING) Log.i(TAG, "getDatabaseIndex");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(DATABASE_CHANGE_INDEX_CHARACTERISTIC_UUID);

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getDatabaseIndex reportResult");
                int value = -1;
                if (shnResult == SHNResult.SHNOk) {

                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    value = byteBuffer.getInt();
                }
                if (listener != null) {
                    listener.onActionCompleted(value, shnResult);
                }
            }
        };
        shnCharacteristic.read(resultReporter);
    }

    private void setDatabaseIndex(int increment, final SHNResultListener listener) {
        if (LOGGING) Log.i(TAG, "setStringCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(DATABASE_CHANGE_INDEX_CHARACTERISTIC_UUID);

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] value = buffer.putInt(increment).array();

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "setStringCharacteristic reportResult");

                if (listener != null) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };

        shnCharacteristic.write(value, resultReporter);
    }

    private void setStringCharacteristic(String name, final SHNResultListener listener, UUID uuid) {
        if (LOGGING) Log.i(TAG, "setStringCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        byte[] value = name.getBytes(StandardCharsets.UTF_8);

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "setStringCharacteristic reportResult");

                if (listener != null) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };

        shnCharacteristic.write(value, resultReporter);
    }

    private void setUIntCharacteristic(int sizeByte, int value, final SHNResultListener listener, UUID uuid) { // is this generic method good?
        if (LOGGING) Log.i(TAG, "setUIntCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);

        ByteBuffer buffer = ByteBuffer.allocate(sizeByte);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        switch (sizeByte) {
            case 1:
                buffer.put((byte) value);
                break;
            case 2:
                buffer.putShort((short) value);
                break;
            case 4:
                buffer.putInt(value);
                break;
        }
        byte[] byteValue = buffer.array();

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "setUIntCharacteristic reportResult");

                if (listener != null) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };

        shnCharacteristic.write(byteValue, resultReporter);
    }

    private void getStringCharacteristic(final SHNStringResultListener listener, UUID uuid) {
        if (LOGGING) Log.i(TAG, "getStringCharacteristic");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getStringCharacteristic reportResult");
                String value = null;
                if (shnResult == SHNResult.SHNOk) {
                    value = new String(data, StandardCharsets.UTF_8);
                }

                if (listener != null) {
                    listener.onActionCompleted(value, shnResult);
                }
            }
        };

        shnCharacteristic.read(resultReporter);
    }

    public void getUserIndex(final SHNIntegerResultListener listener) {
        if (LOGGING) Log.i(TAG, "getUserIndex");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(USER_INDEX_CHARACTERISTIC_UUID);
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getUserIndex reportResult");
                int value = -1;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    value = ScalarConverters.ubyteToInt(byteBuffer.get());
                }

                if (listener != null) {
                    listener.onActionCompleted(value, shnResult);
                }
            }
        };

        shnCharacteristic.read(resultReporter);
    }

    public void registerNewUser(int consentCode, final SHNIntegerResultListener listener) {
        SHNUserDataCommand command = new SHNUserDataCommand(SHNUserDataCommand.Command.REGISTER, consentCode, listener);
        commandQueue.add(command);
        executeCommandIfAllowed();
    }

    public void consentExistingUser(int userId, int consentCode, SHNResultListener listener) {
        SHNUserDataCommand command = new SHNUserDataCommand(SHNUserDataCommand.Command.CONSENT, userId, consentCode, listener);
        commandQueue.add(command);
        executeCommandIfAllowed();
    }

    public void deleteUser(SHNResultListener listener) {
        SHNUserDataCommand command = new SHNUserDataCommand(SHNUserDataCommand.Command.DELETE, listener);
        commandQueue.add(command);
        executeCommandIfAllowed();
    }

    private void executeCommandIfAllowed() {
        if (!executing && commandQueue.size() > 0) {
            executing = true;
            final SHNUserDataCommand command = commandQueue.getFirst();
            SHNResultListener listener = new SHNResultListener() {
                @Override
                public void onActionCompleted(SHNResult result) {
                    command.notifyListeners(result);
                    removeCurrentCommandAndStartNext();
                }
            };

            switch (command.getType()) {
                case REGISTER:
                    SHNIntegerResultListener integerListener = new SHNIntegerResultListener() {
                        @Override
                        public void onActionCompleted(int value, SHNResult result) {
                            command.notifyListeners(value, result);
                            removeCurrentCommandAndStartNext();
                        }
                    };
                    registerNewUserImpl(command.consentCode, integerListener);
                    break;
                case CONSENT:
                    consentExistingUserImpl(command.userId, command.consentCode, listener);
                    break;
                case DELETE:
                    deleteUserImpl(listener);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private void removeCurrentCommandAndStartNext() {
        commandQueue.remove();
        executing = false;
        executeCommandIfAllowed();
    }

    private void registerNewUserImpl(int consentCode, final SHNIntegerResultListener listener) {
        if (consentCode > 9999 || consentCode < 0) {
            listener.onActionCompleted(-1, SHNResult.SHNInvalidParameterError);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(3);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.put(OP_CODE_REGISTER_NEW_USER);
            buffer.putShort((short) consentCode);
            byte[] value = buffer.array();

            writeToControlPointCharacteristic(value, listener);
        }
    }

    private void consentExistingUserImpl(int userId, int consentCode, final SHNResultListener listener) {
        if (consentCode > 9999 || consentCode < 0 || userId < 0 || userId > 254) {
            listener.onActionCompleted(SHNResult.SHNInvalidParameterError);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.put(OP_CODE_CONSENT);
            buffer.put((byte) userId);
            buffer.putShort((short) consentCode);
            byte[] value = buffer.array();

            writeToControlPointCharacteristic(value, listener);
        }
    }

    private void deleteUserImpl(SHNResultListener listener) {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(OP_CODE_DELETE_USER_DATA);
        byte[] value = buffer.array();

        writeToControlPointCharacteristic(value, listener);
    }

    private void writeToControlPointCharacteristic(byte[] value, final SHNIntegerResultListener listener) {
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (shnResult != SHNResult.SHNOk) {
                    listener.onActionCompleted(-1, shnResult);
                }
            }
        };

        shnCharacteristic.write(value, shnCommandResultReporter);
    }

    private void writeToControlPointCharacteristic(byte[] value, final SHNResultListener listener) {
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(USER_CONTROL_POINT_CHARACTERISTIC_UUID);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (shnResult != SHNResult.SHNOk) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };

        shnCharacteristic.write(value, shnCommandResultReporter);
    }

    protected static class SHNUserDataCommand {

        protected enum Command {REGISTER, CONSENT, DELETE}

        private final Command command;
        private final int consentCode;
        private final int userId;

        public final SHNIntegerResultListener shnIntegerResultListener;
        public final SHNResultListener shnResultListener;

        public SHNUserDataCommand(Command command, int consentCode, SHNIntegerResultListener shnIntegerResultListener) {
            this.command = command;
            this.userId = 255;
            this.consentCode = consentCode;
            this.shnIntegerResultListener = shnIntegerResultListener;
            this.shnResultListener = null;
        }

        public SHNUserDataCommand(Command command, int userId, int consentCode, SHNResultListener shnResultListener) {
            this.command = command;
            this.userId = userId;
            this.consentCode = consentCode;
            this.shnIntegerResultListener = null;
            this.shnResultListener = shnResultListener;
        }

        public SHNUserDataCommand(Command command, SHNResultListener shnResultListener) {
            this.command = command;
            this.userId = 255;
            this.consentCode = -1;
            this.shnResultListener = shnResultListener;
            this.shnIntegerResultListener = null;
        }

        public void notifyListeners(int value, SHNResult result) {
            if (shnIntegerResultListener != null)
                shnIntegerResultListener.onActionCompleted(value, result);
        }

        public void notifyListeners(SHNResult result) {
            if (shnIntegerResultListener != null)
                shnIntegerResultListener.onActionCompleted(-1, result);
            if (shnResultListener != null)
                shnResultListener.onActionCompleted(result);
        }

        public Command getType() {
            return command;
        }

        public int getConsentCode() {
            return consentCode;
        }
    }
}


/*
 * Copyright 2016 © Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import com.philips.icpinterface.data.Commands;

enum CloudCommand {
    SIGNON(Commands.SIGNON),
    GET_SERVICE_URL(Commands.GET_SERVICE_URL),
    GET_DATETIME(Commands.GET_DATETIME),
    GET_TIMEZONES(Commands.GET_TIMEZONES),
    GET_COMPONENT_DETAILS(Commands.GET_COMPONENT_DETAILS),
    DOWNLOAD_FILE(Commands.DOWNLOAD_FILE),
    EVENT_NOTIFICATION(Commands.EVENT_NOTIFICATION),
    FETCH_EVENTS(Commands.FETCH_EVENTS),
    RESET(Commands.RESET),
    DATA_COLLECTION(Commands.DATA_COLLECTION),
    SUBSCRIBE_EVENTS(Commands.SUBSCRIBE_EVENTS),
    GET_UPLOADSIZE(Commands.GET_UPLOADSIZE),
    CUSTOM_SERVICE(Commands.CUSTOM_SERVICE),
    RE_SIGNON_NOTIFICATION(Commands.RE_SIGNON_NOTIFICATION),
    KEY_PROVISION(Commands.KEY_PROVISION),
    REGISTER_PRODUCT(Commands.REGISTER_PRODUCT),
    UNREGISTER_PRODUCT(Commands.UNREGISTER_PRODUCT),
    QUERY_REGISTRATION_STATUS(Commands.QUERY_REGISTRATION_STATUS),
    QUERY_TC_STATUS(Commands.QUERY_TC_STATUS),
    ACCEPT_TERMSANDCONDITIONS(Commands.ACCEPT_TERMSANDCONDITIONS),
    KEY_DEPROVISION(Commands.KEY_DEPROVISION),
    PUBLISH_EVENT(Commands.PUBLISH_EVENT),
    CANCEL_EVENT(Commands.CANCEL_EVENT),
    EVENT_DISTRIBUTION_LIST(Commands.EVENT_DISTRIBUTION_LIST),
    DOWNLOAD_DATA(Commands.DOWNLOAD_DATA),
    THIRDPARTY_REGISTER_PROTOCOLADDRS(Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS),
    PAIRING_ADD_RELATIONSHIP(Commands.PAIRING_ADD_RELATIONSHIP),
    PAIRING_GET_RELATIONSHIPS(Commands.PAIRING_GET_RELATIONSHIPS),
    PAIRING_ADD_PERMISSIONS(Commands.PAIRING_ADD_PERMISSIONS),
    PAIRING_REMOVE_PERMISSIONS(Commands.PAIRING_REMOVE_PERMISSIONS),
    PAIRING_GET_PERMISSIONS(Commands.PAIRING_GET_PERMISSIONS),
    PAIRING_QUERY_PERMISSION(Commands.PAIRING_QUERY_PERMISSION),
    PAIRING_REMOVE_RELATIONSHIP(Commands.PAIRING_REMOVE_RELATIONSHIP),
    PAIRING_RESET_TTL(Commands.PAIRING_RESET_TTL),
    PAIRING_SETMY_METADATA(Commands.PAIRING_SETMY_METADATA),
    COMMAND_END(Commands.COMMAND_END),
    UNKNOWN(999);

    public final int commandCode;

    CloudCommand(final int commandCode) {
        this.commandCode = commandCode;
    }

    public static CloudCommand fromCommandCode(final int commandCode) {
        for (CloudCommand command : CloudCommand.values()) {
            if (command.commandCode == commandCode) {
                return command;
            }
        }

        return UNKNOWN;
    }
}
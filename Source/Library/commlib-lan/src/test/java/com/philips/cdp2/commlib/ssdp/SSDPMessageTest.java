/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import org.junit.Test;

import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_FOUND;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_NOTIFY;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_SEARCH;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NEWLINE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SEPARATOR;
import static org.assertj.core.api.Assertions.assertThat;

public class SSDPMessageTest {

    @Test
    public void whenAMessageIsConstructedWithASupportedType_thenItShouldNotThrowAnException() {
        SSDPMessage message1 = new SSDPMessage(MESSAGE_TYPE_SEARCH);
        SSDPMessage message2 = new SSDPMessage(MESSAGE_TYPE_FOUND);
        SSDPMessage message3 = new SSDPMessage(MESSAGE_TYPE_NOTIFY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAMessageIsConstructedWithAnUnsupportedType_thenItShouldThrowAnException() {
        SSDPMessage message = new SSDPMessage("INVALID");
    }

    @Test
    public void givenASupportedMessage_whenAValueIsPutInTheHeaders_thenTheNewValueShouldBeReturned() {
        SSDPMessage message = new SSDPMessage(MESSAGE_TYPE_SEARCH);

        message.put("test", "value");

        assertThat(message.get("test")).isEqualTo("value");
    }

    @Test
    public void givenASupportedMessage_whenTheStringRepresentationIsObtained_thenItShouldEndWithANewlineSequence() {
        SSDPMessage message = new SSDPMessage(MESSAGE_TYPE_SEARCH);
        message.put("key1", "value1");
        message.put("key2", "value2");
        message.put("key3", "value3");

        assertThat(message.toString().endsWith(NEWLINE));
    }

    @Test
    public void givenASupportedMessage_whenTheStringRepresentationIsObtained_thenEachHeaderKeyAndValueShouldBeSeparatedByAColon() {
        SSDPMessage message = new SSDPMessage(MESSAGE_TYPE_SEARCH);
        message.put("key", "value");

        String[] lines = message.toString().split(NEWLINE);
        String[] header = lines[1].split(SEPARATOR);

        assertThat(header[0]).isEqualTo("key");
        assertThat(header[1]).isEqualTo("value");
    }
}

/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import org.junit.Test;

import java.net.URL;

import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_FOUND;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_NOTIFY;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.MESSAGE_TYPE_SEARCH;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.NEWLINE;
import static com.philips.cdp2.commlib.ssdp.SSDPMessage.SEPARATOR;
import static org.assertj.core.api.Assertions.assertThat;

public class SSDPMessageTest {

    private static final String VALID_MSEARCH_RESPONSE = "HTTP/1.1 200 OK\r\nCACHE-CONTROL: max-age=1800\r\nEXT: \r\nLOCATION: http://192.168.1.112/upnp/description.xml\r\nSERVER: ThreadX/5.6 UPnP/1.1 Polaris/48\r\nST: urn:philips-com:device:DiProduct:1\r\nUSN: uuid:12345678-1234-1234-1234-1c5a6bcc90af::urn:philips-com:device:DiProduct:1\r\nBOOTID.UPNP.ORG: 35";
    private static final String MSEARCH_LOCATION = "http://192.168.1.112/upnp/description.xml";

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

    @Test
    public void givenAMessageWithLocation_whenDescriptionUrlIsRetrieved_thenItContainsTheCorrectUrl() throws Exception {
        SSDPMessage message = new SSDPMessage(VALID_MSEARCH_RESPONSE);

        assertThat(message.getDescriptionUrl()).isEqualTo(new URL(MSEARCH_LOCATION));
    }
}

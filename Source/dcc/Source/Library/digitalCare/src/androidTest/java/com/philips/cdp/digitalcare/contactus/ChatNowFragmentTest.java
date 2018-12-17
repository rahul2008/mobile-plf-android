/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus;

import com.philips.cdp.digitalcare.contactus.fragments.ChatNowFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ChatNowFragmentTest {

    private final String TAG = ChatNowFragmentTest.class.getSimpleName();

    @Before
    public void setUp() throws Exception {
        DigiCareLogger.d(TAG, "setUp..");
    }

    @Test
    public void testChatLink() {
        ChatNowFragment mChatFragment = new ChatNowFragment();
        mChatFragment.setChatEndPoint("digitalCare");
        assertNull(mChatFragment.getChatEndPoint());
    }

    @Test
    public void testChatLinkWithHttps() {
        ChatNowFragment mChatFragment = new ChatNowFragment();
        mChatFragment
                .setChatEndPoint("https://ph-india.livecom.net/5g/ch/?___________________________________________________________=&aid=WuF95jlNIAA%3D&gid=3&skill=undefined&tag=PHILIPS_GEN_GR&cat=&chan=LWC;LVC;LVI&fields=&customattr=Group%3APHILIPS_GEN_GR%3B%20Category%3A%3B%20Sub-category%3A%3B%20CTN%3A%3B%20Country%3AIN%3B%20Language%3AEN&sID=1mOYTHel%2BAI%3D&cID=uENOfpmJKAA%3D&lcId=SMS_IN_EN&url=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Ffragments%2Fchat_now_fragment.jsp%3FparentId%3DPB_IN_1%26userCountry%3Din%26userLanguage%3Den&ref=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Fcontact_page.jsp%3FuserLanguage%3Den%26userCountry%3Din");
        assertNotNull(mChatFragment.getChatEndPoint());
    }

    @Test
    public void testChatLinkWithFTP() {
        ChatNowFragment mChatFragment = new ChatNowFragment();
        mChatFragment
                .setChatEndPoint("ftp://ph-india.livecom.net/5g/ch/?___________________________________________________________=&aid=WuF95jlNIAA%3D&gid=3&skill=undefined&tag=PHILIPS_GEN_GR&cat=&chan=LWC;LVC;LVI&fields=&customattr=Group%3APHILIPS_GEN_GR%3B%20Category%3A%3B%20Sub-category%3A%3B%20CTN%3A%3B%20Country%3AIN%3B%20Language%3AEN&sID=1mOYTHel%2BAI%3D&cID=uENOfpmJKAA%3D&lcId=SMS_IN_EN&url=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Ffragments%2Fchat_now_fragment.jsp%3FparentId%3DPB_IN_1%26userCountry%3Din%26userLanguage%3Den&ref=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Fcontact_page.jsp%3FuserLanguage%3Den%26userCountry%3Din");
        assertNull(mChatFragment.getChatEndPoint());
    }

    @Test
    public void testChatLinkWithoutHttp() {
        ChatNowFragment mChatFragment = new ChatNowFragment();
        mChatFragment
                .setChatEndPoint("htt://ph-india.livecom.net/5g/ch/?___________________________________________________________=&aid=WuF95jlNIAA%3D&gid=3&skill=undefined&tag=PHILIPS_GEN_GR&cat=&chan=LWC;LVC;LVI&fields=&customattr=Group%3APHILIPS_GEN_GR%3B%20Category%3A%3B%20Sub-category%3A%3B%20CTN%3A%3B%20Country%3AIN%3B%20Language%3AEN&sID=1mOYTHel%2BAI%3D&cID=uENOfpmJKAA%3D&lcId=SMS_IN_EN&url=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Ffragments%2Fchat_now_fragment.jsp%3FparentId%3DPB_IN_1%26userCountry%3Din%26userLanguage%3Den&ref=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Fcontact_page.jsp%3FuserLanguage%3Den%26userCountry%3Din");
        assertNull(mChatFragment.getChatEndPoint());
    }

    @Test
    public void testChatLinkWithoutHttpAlternate() {
        ChatNowFragment mChatFragment = new ChatNowFragment();
        mChatFragment
                .setChatEndPoint("httsp://ph-india.livecom.net/5g/ch/?___________________________________________________________=&aid=WuF95jlNIAA%3D&gid=3&skill=undefined&tag=PHILIPS_GEN_GR&cat=&chan=LWC;LVC;LVI&fields=&customattr=Group%3APHILIPS_GEN_GR%3B%20Category%3A%3B%20Sub-category%3A%3B%20CTN%3A%3B%20Country%3AIN%3B%20Language%3AEN&sID=1mOYTHel%2BAI%3D&cID=uENOfpmJKAA%3D&lcId=SMS_IN_EN&url=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Ffragments%2Fchat_now_fragment.jsp%3FparentId%3DPB_IN_1%26userCountry%3Din%26userLanguage%3Den&ref=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Fcontact_page.jsp%3FuserLanguage%3Den%26userCountry%3Din");
        assertNull(mChatFragment.getChatEndPoint());
    }
}

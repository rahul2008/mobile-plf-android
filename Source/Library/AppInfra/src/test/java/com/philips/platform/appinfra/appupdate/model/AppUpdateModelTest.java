package com.philips.platform.appinfra.appupdate.model;


import junit.framework.TestCase;


public class AppUpdateModelTest extends TestCase {

	AppUpdateModel mAppUpdateModel;
	Version mVerion;
	Messages mMessages;
	Requirements mRequirements;

	@Override
	protected void setUp() throws Exception {
		mAppUpdateModel = new AppUpdateModel();
		mVerion = new Version();
		mMessages = new Messages();
		mRequirements = new Requirements();
		mAppUpdateModel.setVersion(mVerion);
		mAppUpdateModel.setMessages(mMessages);
		mAppUpdateModel.setRequirements(mRequirements);
		assertNotNull(mVerion);
		assertNotNull(mMessages);
		assertNotNull(mRequirements);
		assertNotNull(mAppUpdateModel.getVersion());
		assertNotNull(mAppUpdateModel.getMessages());
		assertNotNull(mAppUpdateModel.getRequirements());
	}


	public void testGetMinimumVersion() {
		mVerion.setMinimumVersion("1.5.0");
		assertNotNull(mVerion.getMinimumVersion());
		assertEquals(mVerion.getMinimumVersion(),"1.5.0");
	}

	public void testGetdeprecatedVersion() {
		mVerion.setDeprecatedVersion("1.0.0");
		assertNotNull(mVerion.getDeprecatedVersion());
		assertEquals(mVerion.getDeprecatedVersion(),"1.0.0");
	}

	public void testGetCurrentVersion() {
		mVerion.setCurrentVersion("1.6.0");
		assertNotNull(mVerion.getCurrentVersion());
		assertEquals(mVerion.getCurrentVersion(),"1.6.0");
	}

	public void testGetDeprecatedDate() {
		mVerion.setDeprecationDate("2017-08-12");
		assertNotNull(mVerion.getDeprecationDate());
		assertEquals(mVerion.getDeprecationDate(),"2017-08-12");
	}

	public void testGetminimumVersionMessage(){
		mMessages.setMinimumVersionMessage("test minimum version message");
		assertNotNull(mMessages.getMinimumVersionMessage());
		assertEquals(mMessages.getMinimumVersionMessage(),"test minimum version message");
	}

	public void testGetdeprecatedVersionMessage() {
		mMessages.setDeprecatedVersionMessage("test deprecated version message");
		assertNotNull(mMessages.getDeprecatedVersionMessage());
		assertEquals(mMessages.getDeprecatedVersionMessage(),"test deprecated version message");
	}

	public void testGetcurrentVersionMessage() {
		mMessages.setCurrentVersionMessage("test current version message");
		assertNotNull(mMessages.getCurrentVersionMessage());
		assertEquals(mMessages.getCurrentVersionMessage(),"test current version message");
	}

	public void testminimumOSVersion() {
		mRequirements.setMinimumOSVersion("21");
		assertNotNull(mRequirements.getMinimumOSVersion());
		assertEquals(mRequirements.getMinimumOSVersion() ,"21");
	}

}

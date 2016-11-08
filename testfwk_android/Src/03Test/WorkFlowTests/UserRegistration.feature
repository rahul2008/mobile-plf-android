Feature: US12100 - UserRegistration
	As tester I want to automated e2e User Registration scenarios of BaseApp User Registration Reference App context for Android
	PostCondition:Registered User needs to be deleted after the scenarios are executed.

@URtag
Scenario: Login using My Philips Account
    Given that the application is in logout state 
	Then I click on Skip
	Then Verify that the user is in User Registration screen 
	Then I click on Philips Account
	Then enter email as "eno55465@gmail.com" and password as "Philips123"
	Then I click on Log In button
	Then accept terms conditions and click on continue
	Then Verify that the user should land to home screen
	Then I click on Hamburger Menu Icon
	Then I click on Account Settings from Hamburger Menu
	Then I click on Logout in Account Setting Screen 

@URtag
Scenario: Login using Gmail Account
    Given that the application is in logout state 
	Then I click on Skip
	Then Verify that the user is in User Registration screen
	Then I click on google+ to login with gmail account
	Then I enter email as "app32622@gmail.com" and password as "Philips123" in gmail webview screen
	Then verify the accept and terms condition in google plus
	Then Verify that the user should land to home screen

@URtag
Scenario: Login using Facebook Account
    Given that the application is in logout state 
	Then I click on Skip
	Then Verify that the user is in User Registration screen
	Then I click on Facebook to login with Facebook account
	Then login to facebook account with name "urautomation1@gmail.com" and password as "ABCD12345"
	Then verify the accept and terms condition in Facebook
	Then Verify that the user should land to home screen


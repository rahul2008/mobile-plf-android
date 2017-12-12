Feature: RefAppJourney03 - US14119:Creation of Feature Files For App Journey 2,3,4,5
    Pre-Condition : My Philips Account needs to be created by REST API 
	Verify the traditional merge behavior with Gmail 
    Click on FAQ 
    Verify user is able to navigate launch the Click on " Tell us what you think "
    Verify that the user can logout from setting screen 
	Post-Condition : My Philips Account needs to be deleted  


@E2ETesting
Scenario: RefAppJourney03
	Given user is on the User Registration screen
	Given user select the country as "United Kingdom" 
	Then I click on Philips Account
	Then enter email as "gg@sl.in" and password as "Philips@1"
	Then I click on Log In button
	Then accept terms conditions and click on continue
	Then Verify that the user should land to home screen

	Given that the user should be logged out 
	Then Verify that the user should land to home screen
	Then I click on Hamburger Menu Icon
	Then I click on Account Settings from Hamburger Menu
	Then I verify that the My Account has LogIn button
	Then I launch the user registration home screen from Account settings
	Then I click on google+ to login with gmail account 
	Then I enter email as "eno55465@gmail.com" and password as "Philips123" in gmail webview screen 
	Then Verify the traditional merge funtional by entering the gmail password "Philips123"
	Then Verify that the user should land to home screen

	Given user has to launch the support screen
	Then reach FAQs screen
	Then verify each FAQ is clickable and readable
	Then come back to support screen
	Then select "Tell us what you think" from support screen
	Then verify tell us what you think screen
	Then verify that user can logout from Account Settings

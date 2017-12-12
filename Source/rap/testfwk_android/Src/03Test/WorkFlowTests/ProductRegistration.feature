Feature: US12105 - ProductRegistration
	As tester I want to automated e2e scenarios of Product Registration BaseApp in Reference App context for Android
	PreCondition:User should be Registered before the Product Registration should be initiated.
	US11660-Register and De-register a product from Product Registration in an automated way

	US12315 Requirement:
	1a) The system shall provide the ability for a user to login (note: this already exists)
	1b) The system shall provide the ability for a user to logout (Current E2E test case needs update to include log-out from the reference app)
	2a) The system shall provide the ability to register a product (create a separate feature file for the same)

@E2ETesting
Scenario: Register a product
    Given that the application is in logout state
	Then I click on Skip
	Then Verify that the user is in User Registration screen
	Then I click on Philips Account
	Then enter email as "datacore@mailinator.com" and password as "Philips@123"
	Then I click on Log In button
	Then accept terms conditions and click on continue
	Then Verify that the user should land to home screen
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page
	Then I verify if the user has already registered the product using email as "datacore@mailinator.com" and password as "Philips@123",If Yes then Deregister the product
	Then select Register your Product from support screen
	Then I click on Date of Purchase
	Then I select the present date as Date of Purchase
	#Then I select the date of purchase as "1","April","2016"
	Then I click on Register 
	Then I verify that the user is able to see  successfull product message 
	Then I verify that the user is navigated to Support screen on clicking continue
	Then I click on Hamburger Menu Icon
	Then I click on Account Settings from Hamburger Menu
	Then I click on Logout in Account Setting Screen

	  

Scenario: Check the behaviour registering the product that is already registered

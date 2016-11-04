Feature: US12105 - ProductRegistration
	As tester I want to automated e2e scenarios of Product Registration BaseApp in Reference App context for Android
	PreCondition:User should be Registered before the Product Registration should be initiated.

@mytag
Scenario: Register a product
	Given the user is on Reference App Welcome Screen
	Then Verify that the user should land to home screen
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page 
	Then select Register your Product from support screen
	Then I click on Date of Purchase
	Then I select the date of purchase as "1","April","2016"
	Then I click on Register 
	Then I verify that the user is able to see  successfull product message 
	Then I verify that the user is navigated to Support screen on clicking continue

	  

Scenario: Check the behaviour registering the product that is already registered

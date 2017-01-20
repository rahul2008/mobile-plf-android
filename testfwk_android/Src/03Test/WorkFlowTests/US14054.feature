Feature: US14054
As a User verify that the user can skip the welcome screen,
Verify user is able to login with My Philips account with Receive promotional enabled,
Verify user is able see the View product Information of a product 
Verify the behavior of the setting screen when the user has logged in

@Reliability
Scenario: Leak Canary Tool Implementation : App Journey1
 	Given I am on the AppFramework Screen
	Then I click on Skip
	Then Verify that the user is in User Registration screen 
	Then I log in with the email "datacore@mailinator.com" and password "Philips@123"
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page
	Then verify selected product "Sonicare DiamondClean Standard sonic toothbrush heads" detail in view product information with CTN "HX6064/33" 
	Then verify product information (on Philips.com) button
	Then come back to support screen
	Then I click on Hamburger Menu Icon
	Then I click on Settings from the Hamburger Menu List
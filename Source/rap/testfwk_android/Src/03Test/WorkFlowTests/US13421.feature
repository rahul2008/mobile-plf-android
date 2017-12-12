Feature: US13421
As a User login with MyPhilips login, verify the product infromation in the support and settings screen
@E2ETesting
Scenario: Enhancement of coverage : App Journey1
 	Given I am on the AppFramework Screen
	#Given I am on the ReferenceApp Screen
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
	And I verify that under my account the user status is Log out 


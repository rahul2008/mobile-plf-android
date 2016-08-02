Feature: As a USER i want to login into MyPhilipsAccount via Appframework application


@mytag
Scenario: Login into MyPhilips Account and change the account settings
	Given I am on the AppFramework Screen
	Then I click on Skip
	Then Verify that the user is in User Registration screen 
	Then I register using My Philips account
	Then I click on Hamburger Menu Icon
	Then I click on Settings from the Hamburger Menu List
	And I verify that under my account the user status is Log out 
	
	 

Feature: F661 continuous end to end test


@E2ETesting
Scenario: US9960 platform end to end smoke test
 	Given I am on the AppFramework Screen
	Then I click on Skip
	Then Verify that the user is in User Registration screen 
	Then I log in with the email 'datacore@mailinator.com' and password 'Philips@123'
	Then I click on Hamburger Menu Icon
	Then I am on end to end smoke test screen
	Then I enter ID of Reference Device as '70:5A:0F:EC:CF:35'
	When I connect to the BLE reference device and retrieve measurement value
	Then I verify the measurement value on screen shows '30'
	When I get the latest moment from datacore
	Then I verify the moment on screen shows '30'

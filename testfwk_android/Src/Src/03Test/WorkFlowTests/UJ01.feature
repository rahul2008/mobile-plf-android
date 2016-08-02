Feature: User Journey - 1
	Select a Product from the Homepage, Go to Shopping cart, add quantities, Checkout
	Enter the Shipping address, Continue shopping.

@mytag

Scenario: UserJourney-1
    Given I am on the AppFramework Screen
	Then I click on Skip
	Then I Click on phone back button
	Then Verify that the user can see the Home Screen 
	Then I click on Hamburger Menu Icon
	And I click on "Shop" from the Hamburger Menu List
 












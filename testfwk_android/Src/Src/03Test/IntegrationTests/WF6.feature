Feature: WF6
	User is in home screen,selects the product and click buynow,verify moving to shoppingcart page,quantity editable,countinue shopping,click shopping icon,click on checkout  
@mytag
Scenario: WorkFlow-6
	Given User is on the Home Screen
	Given User selects a product and clicks BuyNow
	And  Verify user is taken to Shopping Cart page
    Then Verify product quantity is editable
	When User clicks on continue shopping
	Then Verify user is taken to home screen
	When User  clicking on shopping icon 
	Then Verify user is navigate to shopping cart view
	Then User clicks on checkout
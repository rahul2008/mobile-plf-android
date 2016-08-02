Feature: UJ07
	In order to avoid silly mistake
	As a math idiot
	I want to be told the sum of two numbers

@mytag
Scenario: UserJourney-7
	Given User is on the Home Screen
	Given User is a second time user
	When User selects country store and clicks on Shop Now
	Then verify user is in product catalog view
	Then go to shopping cart
	And select "HX9042/64" from product catalog view
	And select "HX8331/11" from product catalog view
	Then Verify user is in shopping cart view
	Then Verify the product image, name ,price
	Then go to shopping cart
	Then verify delivery and VAT
	Then verify the total number of product list and the total cost
	When user clicks on checkout
	Then verify user is able to delete another address
	Then verify user cannot delete if only one address is present
	Then click on "Deliver to this address"
	Then Verify user is in order summary page
	Then User clicks on Paynow
	Then verify user is on payment screen
	When Valid card data is entered
	Then Verify user is able to complete transaction
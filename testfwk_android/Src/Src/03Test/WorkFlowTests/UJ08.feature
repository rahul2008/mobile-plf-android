Feature: UJ08
	In order to avoid silly mistakes
	As a math idiot
	I want to be told the sum of two numbers

@mytag
Scenario: UserJourney-8
	Given User is on the Home Screen
	Given User is a second time user
	When User selects country store and clicks on Shop Now
	Then verify user is in product catalog view
	And select "HX9042/64" from product catalog view
	And select "HX8331/11" from product catalog view
	Then Verify user is in shopping cart view
	Then verify max quantity of "HX9042/64" matches hybris
	Then verify max quantity of "HX8331/11" matches hybris
	Then verify delivery and VAT
	Then verify the total number of product list and the total cost
	Then user clicks on checkout
	Then click on Deliver to this address
	Then click on Use this payment method
	Then Verify user is in order summary page
	Then delete "Sonicare AirFloss Pro - Interdental cleaner" from cart
	Then press continue shopping
	And select "HX8071/10" from product catalog view
	Then add product to cart
	Then user clicks on checkout
	Then click on Deliver to this address
	Then click on Use this payment method
	Then Verify user is in order summary page
	Then User clicks on Paynow
	When user selects back
	Then Verify user is in shopping cart view

Feature: UJ06
	In order to avoid silly mistakes
	As a math idiot
	I want to be told the sum of two numbers

@mytag
Scenario: UserJourney-6
	Given User is on the Home Screen
	Given User is a second time user
	When User selects country store and clicks on Shop Now
	Then verify user is in product catalog view
	Then go to shopping cart
	And select "HX9042/64" from product catalog view
	And select "HX8071/10" from product catalog view
	Then Verify user is in shopping cart view
	Then Verify the product image, name ,price
	Then go to shopping cart
	When User increments quantity of "Sonicare AdaptiveClean Standard sonic toothbrush heads" to "3"
	When User increments quantity of "Sonicare TongueCare+ Tongue cleaning starter kit" to "4"
	Then verify delivery and VAT
	Then verify the total number of product list and the total cost
	When user clicks on checkout
	Then verify previous shipping address is selected
	#Then verify each address is editable
	Then click on "Deliver to this address"
	Then click on continue
	Then Verify user is in order summary page
	Then User clicks on Paynow
	Then verify user is on payment screen
	When Valid card data is entered
	Then Verify user is able to complete transaction
	Then verify user is in product catalog view

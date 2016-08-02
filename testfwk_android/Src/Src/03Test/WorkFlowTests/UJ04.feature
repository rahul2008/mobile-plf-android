Feature: UJ04
	In order to avoid silly mistakes
	As a math idiot
	I want to be told the sum of two numbers

@mytag
Scenario: UserJourney-4
	Given User is on the Home Screen
	Given User is a first time user
	When User selects country store and clicks on Shop Now
	Then verify user is in product catalog view
	And select "HX8331/11" from product catalog view
	Then Verify user is in shopping cart view
	Then Verify the product image, name ,price
	Then go to shopping cart
	When User increments quantity of "Sonicare AirFloss Pro - Interdental cleaner" to "2"
	Then verify delivery and VAT
	Then verify the total number of product list and the total cost
	When user clicks on checkout
	Then Verify that the user is in address screen
	Then enter valid shipping address and click cancel
	Then Verify user is in address screen
	Then select add a new address
	Then enter valid shipping address and click continue
	Then add new payment method
	When billing address is same as shipping address, click on continue
	Then Verify user is in order summary page
	Then verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel,Totalcost,Vat and totalquantity
	When user clicks on pay now
	Then enter card details that would fail
	And Card payment was not successful
	When user selects back
	Then Verify user is in shopping cart view
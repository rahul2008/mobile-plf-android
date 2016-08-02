Feature: UJ09
	In order to avoid silly mistakes
	As a math idiot
	I want to be told the sum of two numbers

@mytag
Scenario: UserJourney-9
	Given User is on the Home Screen
	Given User is a second time user
	When User selects country store and clicks on Shop Now
	Then verify user is in product catalog view
	Then go to shopping cart
	And select "HX8071/10" from product catalog view
	Then Verify user is in shopping cart view
	Then verify delivery and VAT
	Then verify the total number of product list and the total cost
	Then user clicks on checkout
	Then verify previous shipping address is selected
	Then click on "Deliver to this address"
	Then verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel,Totalcost,Vat and totalquantity
	Then click cancel and return to shopping cart
	Then user clicks on checkout
	Then click on "Deliver to this address"
	Then User clicks on Paynow
	When Valid card data is entered
	Then Verify user is able to complete transaction


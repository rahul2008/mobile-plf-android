Feature: UJ03
	In order to avoid silly mistakes
	As a math idiot
	I want to be told the sum of two numbers

@mytag
Scenario: UserJourney-3
	Given User is on the Home Screen
	And is a first time user
	When User selects country store and clicks on Shop Now
	Then verify user is in product catalog view
	And select "HX9042/64" from product catalog view
	And select "HX8071/10" from product catalog view
	Then Verify shopping cart has "Sonicare AdaptiveClean Standard sonic toothbrush heads"
	Then Verify shopping cart has "Sonicare TongueCare+ Tongue cleaning starter kit"
	When User increments quantity of "Sonicare AdaptiveClean Standard sonic toothbrush heads" to "3"
	When User increments quantity of "Sonicare TongueCare+ Tongue cleaning starter kit" to "4"
	Then verify delivery and VAT
	Then verify the total number of product list and the total cost
	Then user clicks on checkout
	Then Verify that the user is in address screen
	When invalid shipping address is entered
	Then verify error message is seen
	Then add new payment method
	When billing address is different from shipping address
	When invalid billing address is entered
	Then verify error message is seen
	Then verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel,Totalcost,Vat and totalquantity
	Then User clicks on Paynow
	Then verify user is on payment screen
	When Valid card data is entered
	#When session timeout occurs
	#Then verify user gets a session expired message
	#When click back to go to shopping cart
	#Then Verify user is in shopping cart view
	#When user clicks on checkout
	#Then verify previously added address is present
	#When user clicks on deliver to this address
	#Then verify user is able to see payment method
	#When user clicks on use this payment
	#Then Verify user is in order summary page
	#When user clicks on pay now
	#Then verify payment confirmation page

	

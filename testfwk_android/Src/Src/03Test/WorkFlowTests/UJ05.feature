Feature: UJ05
	Repeat user
	Multiple shipping addresses

@mytag
Scenario: UserJourney-5
	Given User is on the Home Screen
	Given User is a second time user
	When User selects country store and clicks on Shop Now
	Then verify user is in product catalog view
	And select "HX9042/64" from product catalog view
	And select "HX8071/10" from product catalog view
	Then Verify user is in shopping cart view
	Then Verify the product image, name ,price
	Then go to shopping cart
	Then verify delivery and VAT
	When user clicks on checkout
	Then Verify that the user is in Select Address screen
	Then verify previous used shipping address is selected
	Then scroll to see other registered shipping addresses
	Then select a different registered shipping address
	Then verify Select Shipping address is present
	#Then Verify that the screen has Continue and Cancel Buttons
	Then click on Deliver to this address
	#below should check if these fields and buttons are present:
	#cc name, cc number, expiry date
	#use payment, add new payment, cancel
	#Then verify card input screen
	#Then press use this payment method
	#Then Verify user is in order summary page
	#Then Press cancel
	#Then verify user gets a cancel message
	#Then Verify user is in order summary page
	Then Let the Billing address is same as Shipping Address and All fields are auto-populated and Click Continue
	Then Press Continue to Reach Ordersummary Screen
	Then verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel,Totalcost,Vat and totalquantity
	Then Click on Cancel in Order Summary

	

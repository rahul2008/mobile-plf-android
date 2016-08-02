Feature: UJ02
	In order to avoid silly mistakes
	As a math idiot
	I want to be told the sum of two numbers

@mytag
Scenario: UserJourney-2
Given User is on the Home Screen
And is a first time user
When User selects country store and clicks on Shop Now
Then verify user is in product catalog view
Then select "HX9042/64" from product catalog view
Then select "HX8071/10" from product catalog view
Then Verify shopping cart icon is "2" in all views
Then go to shopping cart
When User increments quantity of "Sonicare AdaptiveClean Standard sonic toothbrush heads" to "2"
Then Verify shopping cart icon is "3" in all views
Then go to shopping cart
When User increments quantity of "Sonicare TongueCare+ Tongue cleaning starter kit" to "2"
Then Verify shopping cart icon is "4" in all views
Then go to shopping cart
Then verify delivery and VAT
Then verify the total number of product list and the total cost

Then user clicks on checkout
Then Verify that the user is in address screen
Then Enter a valid shipping address and click continue
Then add new payment method
When billing address is different from shipping address
Then enter valid billing address and click continue

Then Press Continue to Reach Ordersummary Screen
Then verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel,Totalcost,Vat and totalquantity
Then User clicks on Paynow

Then verify user is on payment screen
When Invalid card data is entered
Then verify user gets a failed message
When Valid card data is entered
Then Verify user is able to complete transaction


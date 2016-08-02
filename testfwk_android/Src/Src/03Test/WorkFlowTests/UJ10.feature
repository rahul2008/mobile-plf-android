Feature: UJ10
	In order to avoid silly mistakes
	As a math idiot
	I want to be told the sum of two numbers

@mytag
Scenario: UserJourney-10
	   Given User is on the Home Screen
       Given User is a second time user 
       When User selects country store and clicks on Shop Now
       Then verify user is in product catalog view
	   And select "HX9042/64" from product catalog view
       And select "HX9002/64" from product catalog view
	   When User Clicks on Continue Shopping
       Then verify user is in product catalog view
	   Then I go to home screen  
       Then Verify on repeated click of  Buy Now  feature
	   Then Verify the status of shopping icon when the app is sent to back ground and brought to foregound
	   Then Verify the status of shopping icon when the app is closed and relaunched

	   Then click on  shopping cart icon
       Then Verify shopping cart has "Sonicare AdaptiveClean Standard sonic toothbrush heads"
       Then Verify shopping cart has "Sonicare InterCare Standard sonic toothbrush heads"
       When User increments quantity of "Sonicare AdaptiveClean Standard sonic toothbrush heads" to "2"
       When User increments quantity of "Sonicare InterCare Standard sonic toothbrush heads" to "2"
       Then verify delivery and VAT
       Then verify the total number of product list and the total cost
       Then user clicks on checkout
       Then verify previous shipping address is selected
       Then select add a new address
       When invalid shipping address is entered
	   Then verify error message is seen
       Then Enter a valid shipping address and click continue
       When billing address is different from shipping address
       Then user clicks on cancel
       Then Verify user is in shopping cart view
       Then user clicks on checkout
       Then verify previous shipping address is selected
       Then click on Deliver to this address
       When billing address is different from shipping address
	   Then enter valid billing address and click continue
       Then verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel,Totalcost,Vat and totalquantity
	   Then User clicks on Paynow
	   
       Then verify user is on payment screen
       When Invalid card data is entered
       Then verify user gets a failed message
      
      


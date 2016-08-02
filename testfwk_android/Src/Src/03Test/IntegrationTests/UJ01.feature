Feature: User Journey - 1
	Select a Product from the Homepage, Go to Shopping cart, add quantities, Checkout
	Enter the Shipping address, Continue shopping.

@mytag

Scenario: UserJourney-1
Given User is on the Home Screen
Given User selects a product and clicks BuyNow
And  Verify user is taken to Shopping Cart page
And  Verify that correct thumb image of the product and short product description  is added 
And  Verify correct quantity and price are displayed
And  Verify the shopping cart has the claim voucher option
And  Verify the delivery via UPS Parcel price 
And  Verify the TotalCost 
And  Verify Checkout tab is clickable
When User Clicks on Continue Shopping
Then Verify user is taken to home screen
When User Selects the same product and clicks on Buy Now 
Then Verify  buy now  feature  should not increment the product  if it is already added to cart 
Then Verify buy Now  feature  in case of network failure 
Then Verify on repeated click of  Buy Now  feature  
Then Verify the Out of Stock  on clicking the  Buy Now  Option 
Then Verify the product image, name ,price 
Then Click on product and the detail product view page is displayed
Then verify delete button feature
Then verify product quantity is editable
Then verify delivery and VAT
Then verify the total number of product list and the total cost 
Then User click on checkout
Then Verify that the user is in address screen
Then Verify all the user entry fieilds in Address Screen
Then Verify that the screen has Continue and Cancel Buttons
Then Enter a Valid address and Click Continue
Then Let the Billing address is same as Shipping Address and All fields are auto-populated and Click Continue
Then Press Continue to Reach Ordersummary Screen
Then verify user is in Order summary page shippingaddress,Voucher discount,UPS Parcel,Totalcost,Vat and totalquantity
Then User clicks on Paynow
Then verify User able to enter card entries
Then Verify that page display information correctly
 












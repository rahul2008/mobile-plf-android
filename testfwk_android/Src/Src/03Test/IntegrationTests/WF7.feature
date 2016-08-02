Feature: WF7
	User is in second time buyer select product and click buynow,verify navigate to shopping cart,verify previous shopping cart items,verify product image ,
	name price,verify quantity editable,delivery method,total price,vat,claim voucher,clicks on continue shopping,select different product,
	verify total cost and clicks on checkout 
@mytag
Scenario: Workflow 7
Given User is on the Home Screen
Given User is a second time buyer 
And User selects a product and clicks BuyNow
Then Verify the user is navigated to shopping cart
And Verify that the previously shopping cart items are visible if he has selected
Then verify the product image, name ,price 
And Verify the User is able to edit the quantity of the product
And User selects the delivery method
And User Look at the VAT, Total price, Claim voucher
When User Clicks on Continue Shopping
And User selects different  product and clicks on buy now
Then verify the total number of product list and the total cost
And User clicks on checkout
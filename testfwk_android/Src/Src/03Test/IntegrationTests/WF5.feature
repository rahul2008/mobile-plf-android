Feature: WF5
	User is in homescreen,selects the product and click buynow,user moving to shopping cart page,appiles invalid voucher,Vat calculation,revoke voucher,user moving back to home page

@mytag
Scenario: Workflow 5
Given User is on the Home Screen
#And User Logs into application
And User selects a product and clicks BuyNow
And Verify user is taken to Shopping Cart page
Then User applies invalid voucher option
And Verify that the voucher can be applied only once and not multiple times .
And Verify the VAT calculation 
And Verify the user is able to revoke the voucher 

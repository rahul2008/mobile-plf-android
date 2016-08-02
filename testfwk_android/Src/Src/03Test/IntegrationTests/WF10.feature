Feature: WF10
	User is in home screen,clicks on shopping icon,navigate to shopping cart,verify product image,name,price,quantity editable,
	delivery method ,vat,totalprice,claim voucher,clicks on checkout.
@mytag
Scenario: Workflow-10
	Given  User is on the Home Screen
	When User  clicking on shopping icon
	Then Verify the user is navigated to shopping cart
	Then verify the product image, name ,price 
	And Verify the User is able to edit the quantity of the product
    And User selects the delivery method
    And User Look at the VAT, Total price, Claim voucher
	Then verify the total number of product list and the total cost
    And User clicks on checkout

	
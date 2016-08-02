Feature: WF8
	User is in home screen selects the product and click add to cart,Adding product,verify increment product number,clicks on shopping icon,
	verify product image,name,price,quantity editable,delivery method ,vat,totalprice,claim voucher,clicks on checkout.
@mytag
Scenario: Workflow-8
	Given User is on the Home Screen
	When User select a product and click on Add to cart
	Then Verify when the product is added ,the shopping cart display the incremented product number
	When User  clicking on shopping icon
	Then Verify the user is navigated to shopping cart
	Then verify the product image, name ,price
	Then Verify product quantity is editable
	And verify the free  delivery method calculation
	And User Look at the VAT, Total price, Claim voucher
	Then verify the total number of product list and the total cost
    And User clicks on checkout

	
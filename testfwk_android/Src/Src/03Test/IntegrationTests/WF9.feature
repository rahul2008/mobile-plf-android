Feature: WF9
 User is in first time user and clicks on shopping icon,verify shopping cart page,empty cart,clicks on continue shopping,
 selects the product,clicks on add to cart,adding product,verify increment product number,clicks on shopping icon,quantity,
 vat,total price,clicks on checkout
@mytag
Scenario: Workflow 9
Given User is on the HomeScreen
Given User is first time buyer
And user clicks on shopping icon
Then Verify user is navigate to shopping cart view
And Verify that your shopping cart is currently empty appears on Shopping Cart Page
When User Clicks on Countinue Shopping_1
When User select a product and click on Add to cart
Then Verify when the product is added ,the shopping cart display the incremented product number
When User  clicking on shopping icon
Then Verify user is navigate to shopping cart view
And Verify the User is able to edit the quantity of the product
And User selects the delivery method
And User Look at the VAT, Total price, Claim voucher
And verify the total number of product list and the total cost
And User clicks on checkout


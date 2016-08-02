Feature: Workflow3
	User selects the product,click buynow,go to shopping cart,Edit quantity,max quantity,delete,countinue,checkout,go to home page

@mytag

	Scenario: Workflow-3
	Given User is on the HomeScreen
	When User is first time buyer ,select a product and click on buy now 
	Then Verify the user is navigated to shopping cart 
	Then Verify the User is able to edit the quantity of the product 
	Then Verify that max quantity is based on available stock of the product in hybris  
	When Click on menu button next to the product 
	Then Verify the user is able to see the detail info of the product 
	Then Verify that the user is able to delete the product that he does not choose to buy from cart using delete button 
	When User clicks on continue shopping  
	Then verify that it will navigate to test app 
	When User selects different  product and clicks on buy now 
	Then User clicks on checkout 



	
Feature: Workflow2
	Delete Select a Product from the Homepage, Go to Shopping cart, add quantities, Delete the Quantities
	It has to go to homepage

@mytag

	
Scenario:Workflow-2

Given User is on the Home Screen
And Verify user that on test app ,user can see a shopping icon 
And verify user empty cart behaviour
When User select a product and click on Add to cart
Then Verify when the product is added ,the shopping cart display the incremented product number 
When User  clicking on shopping icon 
Then Verify user is navigate to shopping cart view
When User repeated Click on Add to Cart
#Then Verify the status of shopping icon when the app is sent to background
Then Verify the status of shopping icon when the app is closed and relaunched 
Then Verify the status of the shopping cart if the product that he added does not exist

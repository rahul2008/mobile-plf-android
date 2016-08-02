Feature: WF4
	Delete Select a Product from the Homepage, Go to Shopping cart, add quantities, Delete the Quantities
	It has to go to homepage

@mytag
Scenario: WorkFlow-4
	Given User is on the Home Screen
	Given User selects a product and clicks BuyNow
	And  Verify user is taken to Shopping Cart page
	And Verify the delivery via UPS Parcel price
	When User increase and decrease  number of product and see the delivery method calculation 
	Then Verify the free  delivery method calculation 
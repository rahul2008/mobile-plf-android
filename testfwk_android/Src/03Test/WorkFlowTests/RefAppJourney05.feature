Feature: RefAppJourney05 - US14119:Creation of Feature Files For App Journey 2,3,4,5
	Verify that the user can do a purchase by retailer 
    Verify that the Home screen has hamburger menu and can launch any Coco
    Verify that user can login with My Philips account and verify that the Receive promotional is retained if the user has changed .


@E2ETesting
Scenario: RefAppJourney05
	Given that the user should not previously be previously logged in  
	Given that the user is able to see the Splash screen after the launch of the application
	And the user skips the Welcome screen
	Then Verify that the user is in User Registration screen 
	Then I click on Philips Account
	Then enter email as "eno55465@gmail.com" and password as "Philips123"
	Then I click on Log In button
	And the user selects the Receive promotional  
	Then accept terms conditions and click on continue
	Then Verify that the user should land to home screen

	And I click on Hamburger Menu Icon
	Then I launch Philips Shop from hamburger menu
	Then I verify that the product list view is displayed 
	Then I select "HX6064/33" from the product list view
	Then I verify that user is navigated to the detail view of product "HX6064/33"
	Then I click on Buy Now and verify that user is navigated to list of retailer list view 
	Then I select "Philips Online Shop - GB" from the retailer list view
	Then I verify that "Philips Online Shop - GB" webview is launched 
	Then I verify that user can navigate back from webview to retailer list view 
	Then I verify that user can navigate back to product detail view from retailer list view

	And I click on Support from Hamburger Menu List and verify support page
	And I click on Account Settings from Hamburger Menu
	Then I verify that the Receive promotional is enabled in the Account Settings

	
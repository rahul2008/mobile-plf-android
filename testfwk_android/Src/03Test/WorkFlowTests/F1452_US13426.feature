Feature: F1452_US13426
	Enhancement of coverage : App Journey5

@E2ETesting
Scenario: To verify user can purchase by retailer, launch any CoCo from Hamburger menu and to verify Receive Promotional offers is retained for second time login
	Given that the application is in logout state 
	Then I click on Skip
	Then Verify the Mobile App Home Screen with Title "Mobile App Home" and log in with username "gg@sl.in" and password "Philips@1" if required
	Then I click on Hamburger Menu Icon
	Then I launch Philips Shop from hamburger menu
	Then I verify that the product list view is displayed 
	Then I select "HX6064/33" from the product list view
	Then I verify that user is navigated to the detail view of product "HX6064/33"
	Then I click on Buy Now and verify that user is navigated to list of retailer list view 
	Then I select "Philips Online Shop - GB" from the retailer list view
	Then I verify that "Philips Online Shop - GB" webview is launched 
	Then I verify that user can navigate back from webview to retailer list view 
	Then I verify that user can navigate back to product detail view from retailer list view 
	Then I verify that user can navigate back to Product list view from product detail view
	Then I click on "Mobile App Home" from Hamburger Menu List and verify "Mobile App Home" screen
	Then I click on "Settings" from Hamburger Menu List and verify "Account settings" screen
	Then I click on "Support" from Hamburger Menu List and verify "Support" screen
	Then I click on "About" from Hamburger Menu List and verify "About" screen
	Then I click on "Data Sync" from Hamburger Menu List and verify "Data Sync" screen
	Then I click on "Connectivity" from Hamburger Menu List and verify "Connectivity" screen

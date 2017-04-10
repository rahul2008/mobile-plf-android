﻿Feature: US12315 - Philips Shop
	As tester I want to automated e2e User Registration scenarios of BaseApp User Registration Reference App context for Android
	PostCondition:Registered User needs to be deleted after the scenarios are executed.
	US12315 Requirement:
	3.  In App purchase (InApp) -
    3a) The system shall provide the ability to buy one time physical goods

@IAP
Scenario: Verify that user can purchase a product from retailer using "Buy Now"   
	Given that the application is in logout state 
	Then I click on Skip
	Then Verify that the user is in User Registration screen 
	Then I click on Philips Account
	Then enter email as "gg@sl.in" and password as "Philips@1"
	Then I click on Log In button
	Then accept terms conditions and click on continue
	Then Verify that the user should land to home screen
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

	
Scenario: Verify behaviour of "No Retailer" for a selected product.
    Given that the application is in logout state 
	Then I click on Skip
	Then Verify that the user is in User Registration screen 
	Then I click on Philips Account
	Then enter email as "eno55465@gmail.com" and password as "Philips123"
	Then I click on Log In button
	Then accept terms conditions and click on continue
	Then Verify that the user should land to home screen
	Then I click on Hamburger Menu Icon
	Then I launch Philips Shop from hamburger menu
	Then I verify that the product list view is displayed 
	Then I select "HX8331/11" from the product list view
	Then I verify that user is navigated to the detail view of product "HX9042/64"
	Then I click on Buy Now
    Then I verify that user is displayed with a pop- up "No Retailer" 
	Then I verify that on click ok - user remains in the product detail view .

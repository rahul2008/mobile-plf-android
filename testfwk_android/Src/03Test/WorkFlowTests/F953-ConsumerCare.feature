Feature: F953 - Continuation: Extend the e2e automated test suite of the reference platform with reference to Consumer Care
US11725 Automate consumer care module in Reference App context for Android

Scenario: 01 Viewing Product Information

	Given I am on the AppFramework Screen
	Then I click on Skip
	Then Verify the Mobile App Home Screen with Title "Mobile App Home" and log in with username "hubble@mailinator.com" and password "Philips@123" if required
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page
	Then verify selected product "Sonicare DiamondClean Standard sonic toothbrush heads" detail in view product information with CTN "HX6064/33" 
	Then verify product information (on Philips.com) button

Scenario: 02 Verify Live Chat and Email option
	
	Given the user is on Mobile App Home page
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page
	Then select "Contact us" from support screen
	Then verify all options on contact us screen
	Then verify live chat option
	Then verify send email option

Scenario: 03 Verify Find Philips near you feature

	Given the user is on Mobile App Home page
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page
	Then select "Find Philips near you" from support screen
	Then verify find philips near you option

Scenario: 04 Verify Facebook and Twitter feature
	Given the user is on Mobile App Home page
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page
	Then select "Contact us" from support screen
	Then verify on Twitter option
	Then verify on facebook option
	
Scenario: 05 Verify Read FAQs
	Given the user is on Mobile App Home page
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page
	Then reach FAQs screen
	Then verify each FAQ is clickable and readable
	Then come back to support screen

Scenario: 06 Verify Tell us What you think
	Given the user is on Mobile App Home page
	Then I click on Hamburger Menu Icon
	Then I click on Support from Hamburger Menu List and verify support page
	Then select "Tell us what you think" from support screen
	Then verify tell us what you think screen


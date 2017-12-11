Feature: RefAppJourney04 - US14119:Creation of Feature Files For App Journey 2,3,4,5
	Verify that the user can login from Setting screen
	Verify that the user can login with My Philips account 
    Verify the behavior of register the product repeated times 
    Verify the back key behavior in PR screen 


@E2ETesting
Scenario: RefAppJourney04
	Given that user has already registered the product  
    And the user is in home screen after the reference app is launched
    Then I launch the Account Settings 
	And  the user clicks on Login button
	Then user verifies that User Registration home screen is successfully opened
	Then I click on Philips Account
	Then enter email as "eno55465@gmail.com" and password as "Philips123"
	Then I click on Log In button
	Then accept terms conditions and click on continue
	Then Verify that the user should land to home screen

	Given user has to launch the support screen
	Then select Register your Product from support screen
	Then verify that the user is notified that the user has already registered the product
	And the user click on back arrow 
	Then verify that the user is in Support screen 

	
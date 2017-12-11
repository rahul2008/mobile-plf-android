Feature: US14412-Leak Canary Tool : Deployment For App Journey2.
       PreCondition : My Philips Account is created by REST API 
	   Navigate through the 3 screens and click done
	   Verify that the Welcome screen can be swiped right and left 
       Verify the behavior of the setting screen when the user has not logged in 
       Verify user is able to create My Philips account without Receive promotional enabled
       Verify user can initiate the navigation to  send email view page from Help and Support screen 
       Verify Semi-automatic PR giving  future date 
       Verify Semi-automatic PR giving  correct purchase date
	   PostCondition : My Philips Account is deleted by REST API
	   
@Reliability
Scenario: Leak Canary Tool : Deployment For App Journey2.
	Given the User is able to view Welcome screens of the application
	And the user can see option to Skip the Introduction screens
	Then verify that the user can see Skip but  Done should not be visible
	Then the user can see dots (navigation/page control) on the bottom.
	Then validate that the  dots indicate me how many screens are available.
	Then validate that  the dot which is highlighted indicates me on which screen number I am at.


    Then validate that the user can see the next arrow if there are more than one screens
	Then validate that the user do not see Previous arrow when he is on the first screen
    Then validate that the user can click on the next arrow and move to next screen
	Then validate that the user can see Previous arrow if he  have moved to next screen
    Then validate that the user do not see next arrow when he is  on the last screen
	And the user can see an option to mark Done when he is on the last screen.
	Then verify that the user can see Done but skip should not be visible    
	Then validate that the user can click on the previous button and go back to the previous screen	
	
	And the user should be able to move to next screen when he swipe left.
	And the user should be able to move to the previous screen when he swipe right.
	And the user click on Done
	And the user click Phone Back button
	Then Verify that the user should land to home screen

	Given I launch the Account Settings 
	And  the user clicks on Login button
	Then Verify that the user is in User Registration screen
	Then I register using My Philips account
	Then Verify that the user should land to home screen

	Given I launch the Account Settings 
	Then I verify that the My Account has Logout button
	Then I verify that the Receive promotional is not enabled in the Account Settings
	
	Given user has to launch the support screen 
	Then select "Contact us" from support screen
	Then verify all options on contact us screen
	Then verify send email option

	Given user is in the support screen
	#Then I verify if the user has already registered the product using email as "datacore@mailinator.com" and password as "Philips@123",If Yes then Deregister the product
	#Then select Register your Product from support screen
	#Then I click on Date of Purchase
	#Then I select the present date as Date of Purchase
	#Then I select Future Date of Purchase as "1","April","2016" 
	#Then I verify that user is notified that Future Date of Purchase is not allowed .
	#Then I select the date of purchase as "1","April","2016"
	#Then I click on Register 
	#Then I verify that the user is able to see  successfull product message

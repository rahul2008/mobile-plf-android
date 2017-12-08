Feature: 
As a user
I want to Register into Philips Account

@US6114 @welcomescreen @privacy_policy
Scenario: Verify if the user is redirected to proper privacy policy web url
Given I launch my application
When I tap on "Registration Test" button
Then I am on "Log In" page
Then I should see "Privacy Notice" link
When I touch on "Privacy Notice" link
Then I am on Web Browser page


@US6114 @home @verify_unmask_icon
Scenario: Verify if Unmask icon is present for the screens where Password field is persent
Given I launch my application
When I tap on "Registration Test" button
Then I am on "Log In" page
Then I should see "Create Philips account" button
When I tap on "Create Philips account" button
Then I am on "Create Account" page
Then I see unmask icon of password field
Then I go back
Then I should see "Philips account" button
When I tap on "Philips account" button
Then I am on "Log In" page
Then I see unmask icon of password field
 

@v1.1 @welcomescreen @terms_conditions
Scenario: Verify if the user is taken to proper terms and conditions web url
Given I launch my application
When I tap on "Registration Test" button
Then I am on "Log In" page
Then I should see "Terms and Conditions" link
When I touch on "Terms and Conditions" link
Then I am on Web Browser page

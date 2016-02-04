Feature:
  As a user
  I want to Register via Social Account


  @US6114 @Google+_selectTermsAndCondition @reinstall 
  Scenario:Verify if Accept terms and condition toggle is configurable based on propositions in Almost done screen(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    When I tap on "Google" button
    Then I am on "Google" social page
    When I enter already merged id in email field and password
    When I tap on Allow button
    Then I am on "Log In" screen
    Then I toggle Terms & Condition checkbox

  @US6114 @Google+_withoutTermsAndConditions @reinstall
  Scenario:Verify if valid error messages appears if the user ignores checking Terms and condition or Hits the Continue button without entering any details in Almost done screen. (This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    When I tap on "Google" button
    Then I am on "Google" social page
    When I enter already merged id in email field and password
    When I tap on Allow button
    Then I am on "Log In" screen
    Then I Continue
    Then I see "Please accept the Terms and Conditions" text

  @v1.1 @facebook_login @reinstall
  Scenario:Facebook log in
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    When I tap on "Facebook" button
    Then I am on "Facebook" social page
    When I enter values for facebook login
    And I touch facebook login button
    Then I see "Log In" text
    And I see "Almost done!" text

  @v1.1 @facebook_login_without_activating_account @reinstall
  Scenario:Verifying Facebook Login without activating account
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    When I tap on "Facebook" button
    Then I am on "Facebook" social page
    When I enter values for inactivated facebook account
    And I touch facebook login button
    Then I am on the facebook confirmation page

  @US5150 @facebook_error_message @reinstall
  Scenario:Facebook log in
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    When I tap on "Facebook" button
    Then I am on "Facebook" social page
    When I enter values for facebook login
    And I touch facebook login button
    Then I see "Log In" text
    And I see "Almost done!" text
    When I touch on Continue button
    Then I see "Please accept the Terms and Conditions" text


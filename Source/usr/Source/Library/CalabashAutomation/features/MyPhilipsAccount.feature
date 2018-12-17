Feature:
  As a user
  I want to Login via Philips Account

  @v1.1 @philips_account @valid_email_password
  Scenario: Verify philips account with both valid email and password (This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    When I enter valid email and password in philips account
    When I tap on Log In button
    Then I see "Almost done" text
    Then I see checkbox for Terms and conditions
    And I toggle checkbox for Terms & condition
    When I tap on Continue button
    Then I am on "Log In" page
    When I tap on Log Out button
    Then I see "Welcome" page


  @v1.1 @philips_account @valid_email_invalid_password @reinstall
  Scenario: Verify philips account for valid email and invalid password
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    When I enter valid email and invalid password in philips account
    When I tap on Log In button
    Then I see error message stated "The email address or password you entered is incorrect."


  @v1.1 @philips_account @invalid_email_password @reinstall
  Scenario: Verify philips account when user enter invalid email
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    When I enter invalid email and password in philips account
    Then I see error message stated "Invalid email address"


  @v1.1 @philips_account @logout @reinstall
  Scenario: Verify philips account when user logout
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    When I enter valid email and password in philips account
    When I tap on Log In button
    Then I am on "Log In" page
    Then I see "Almost done" text
    Then I see checkbox for Terms and conditions
    And I toggle checkbox for Terms & condition
    When I tap on Continue button
    Then I am on "Log In" page
    When I tap on Log Out button
    Then I see "Welcome" page



  @US6114 @US5150 @philips_account @forgot_button
  Scenario: Verify message comes when hitting forgotten password button
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    And I see "Log in with Philips account" text
    When I enter email in philips account
    When I tap on "Forgotten Password" button
    Then I see Reset Password message

  @US6114 @philips_account @forgot_password
  Scenario: Verify when tapping on 'Forgot password' let the user to reset the password after entering email address
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    And I see "Log in with Philips account" text
    When I tap on "Forgotten Password" button
    Then I see "We will send you an email to reset your password." text


  @US6114 @philips_account @successful_activation
  Scenario: Verify if the almost done screen with terms and condition page pops up if user signs in with the verified email address.(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    And I see "Log in with Philips account" text
    When I enter email and password after successful activation
    When I tap on Log In button
    Then I am on "Log In" page
    Then I see checkbox for Terms and conditions

  @US6114 @philips_account @accept_terms_condition @reinstall
  Scenario: Verify terms and condition of almost done screen(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    And I see "Log in with Philips account" text
    When I enter email and password after successful activation
    When I tap on Log In button
    Then I am on "Log In" page
    Then I see checkbox for Terms and conditions
    And I toggle checkbox for Terms & condition
    When I tap on Continue button
    Then I am on "Log In" page
    When I tap on Log Out button
    Then I see "Welcome" page

  @US6114 @philips_account @reinstall
  Scenario: Verify error message if user forget to accept terms and conditions and hit continue button (This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    And I see "Log in with Philips account" text
    When I enter email and password after successful activation
    When I tap on Log In button
    Then I am on "Log In" page
    Then I see checkbox for Terms and conditions
    When I tap on Continue button
    Then I see error message stated "Please accept the Terms and Conditions"


  @US5150 @philips_account @not_activated_account
  Scenario: Verify the user is able to view the message when user tries to login without verifiying the account
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    When I enter email and password that are not verified
    When I tap on "Philips account" button
    Then I see error message stated "Please verify your email address through the activation link sent to your email account"


  @US6114 @philips_account @sign_in @verify_toggle
  Scenario: Verify if Terms and Condtion toggle is not displayed in My Philips
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Philips account" button
    When I tap on Philips account button
    Then I am on "Log In" page
    And I see "Log in with Philips account" text
    Then I should not see toggle checkbox
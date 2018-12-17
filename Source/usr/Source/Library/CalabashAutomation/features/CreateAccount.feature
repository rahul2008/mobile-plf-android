Feature:
  As a user
  I want to Register via Create Philips Account

  @US6114 @create_account @password
  Scenario: Verify if Password hints are shown when the user select Password Field in Create account screen
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    Then I should see password hints below password field

  @US6114 @create_account @password_checkboxes_fill
  Scenario: Verify if password hints checkboxes fill when chosen password meets the requirement in Create account screen
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    When I enter password fulfilling password hint criteria
    Then I should see green tick against that hint


  @US6114 @create_account @min_password
  Scenario: Verify if password field is accepted only when user enters atleast 2 out of 3 types of characters: Letters, digits or symbols (This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter name,email and min_condition password in create account page
    And I toggle checkbox for Terms & condition
    Then I see "Create Philips account" button is enabled

  @US6114 @create_account @checkbox
  Scenario: Verify checkbox for terms and condition(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    Then I see checkbox for Terms and conditions

  @US6114 @create_account @invalid_password
  Scenario: Verify how the app behaves when entering invalid characters letters smileys etc in password field.(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter name,email and invalid password in create account page
    And I toggle checkbox for Terms & condition
    Then I see "Create Philips account" button is disabled


  @US6114 @create_account @password @unmasking
  Scenario: Verify if password characters are visible when selecting  unmask/eye icon in Create account screen.
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    When I enter credentials
    Then I see unmask icon get highlighted
    When I touch unmask icon of password field
    Then I see unmasked password

  @US6114 @create_account @password @disbale_unmasking
  Scenario: Verify if password characters hides when disabling unmask/eye icon in Create account screen
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    When I enter credentials
    Then I see unmask icon get highlighted
    When I touch unmask icon of password field
    Then I see unmasked password
    When I touch unmask icon of password field
    Then I see masked password

  @US6114 @create_account @existing_user
  Scenario: Verify create account for already existing user(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter name, existing mail and password in create account page
    And I toggle checkbox for Terms & condition
    When I tap Create Philips account button for data registering
    Then I see error message stated "Email address is already in use"

  @US6114 @create_account @social_login @existing_user
  Scenario: Verify create account when user exist for social login (This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter name, existing social mail and password in create account page
    And I toggle checkbox for Terms & condition
    When I tap Create Philips account button for data registering
    Then I see error message stated "Email address is already in use"
    And I see message stating user already exist for philips or social media

  @US6114 @create_account @new_user @valid_mail
  Scenario: Verify create account for new user (This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter name,valid email and password in create account page
    And I toggle checkbox for Terms & condition
    Then I see "Create Philips account" button is enabled
    When I tap Create Philips account button for data registering
    Then I navigate to "Create Account" page

  @US6114 @create_account @invalid_mail
  Scenario: Verify if the error message appearing for entering invalid email address
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter name,invalid email and password in create account page
    Then I see error message stated "Invalid email address"
    And I see "Create Philips account" button is disabled

  @US6114 @create_account @resend_button
  Scenario: Verify resend button greyed out for some time when tapped by user(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter credentials
    And I toggle checkbox for Terms & condition
    When I tap Create Philips account button for data registering
    Then I am on "Create Account" page
    Then I see "Please verify your email" text
    When I tap resend button of create account
    Then I see "Verification email sent" text
    When I tap on "Continue" button
    Then I am on "Create Account" page
    When I tap resend button of create account
    Then Resend button won't work for 5 minutes


  @v1.1 @create_account @valid_name
  Scenario: Verifying valid name for create account page(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter valid name, email and password in create account
    And I toggle checkbox for Terms & condition
    Then I see "Create Philips account" button is enabled
    When I tap Create Philips account button for data registering
    Then I navigate to "Create Account" page


  @v1.1 @create_account @invalid_name
  Scenario: Verifying invalid name for create account page
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter invalid name,email and password in create account
    Then I see error message stated "Field cannot be empty"


  @v1.1 @create_account @select_opt_in
  Scenario: Verifying create account page when market update is slected(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter valid credentials in create account for market updates
    And I toggle checkbox for Terms & condition
    And I toggle checkbox for Receive Philips news
    Then I see "Create Philips account" button is enabled
    When I tap Create Philips account button for data registering
    Then I navigate to "Create Account" page


  @v1.1 @create_account @deselect_opt_in
  Scenario:Verifying create account page when market update is not selected(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter valid credentials in create account for no market updates
    And I toggle checkbox for Terms & condition
    When I tap Create Philips account button for data registering
    Then I navigate to "Create Account" page


  @US6114 @create_account @resend_button @continue_option
  Scenario: Verify what happens when tapping on 'Continue option' in 'Verfication email sent' pop up when email is not verified(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter credentials for verification of continue
    And I toggle checkbox for Terms & condition
    When I tap Create Philips account button for data registering
    Then I am on "Create Account" page
    Then I see "Please verify your email" text
    When I tap on "Resend" button
    Then I see "Verification email sent" text
    When I tap on "Continue" button
    Then I am on "Create Account" page


  @US5150 @create_account @password_guideline
  Scenario: Verify if user is able to view the below the password guideline
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    Then I see "Protect your privacy by choosing a strong password." text

  @US5150 @create_account @activate_message
  Scenario: Verify the user is able to view the message after successful activation(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    When I enter credentials for mail verification
    And I toggle checkbox for Terms & condition
    Then I see "Create Philips account" button is enabled
    When I tap Create Philips account button for data registering
    Then I am on "Create Account" page
    Then I see "Please check your email to activate your account." text


  @US6114 @create_account @ignore_terms_conditions
  Scenario: Verify if the valid error messages appears if the user ignores checking Terms and conditions(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter valid credentials in create account
    Then I see "Create Philips account" button is enabled
    When I tap Create Philips account button for data registering
    Then I see error message stated "Please accept the Terms and Conditions"


  @US6114 @create_account @resend_verification
  Scenario: Verify if the resend button enables if the entered email id is not verified in sign in screen.(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter credentials for resend button verification
    And I toggle checkbox for Terms & condition
    When I tap Create Philips account button for data registering
    Then I am on "Create Account" page
    Then I see "Please verify your email" text
    When I tap resend button of create account
    Then I see "Verification email sent" text


  @US6114 @create_account @resend_button @verify_text @reinstall
  Scenario: Verify if the confirmation pop up for verification email resend should be displayed.(This scenario would fail to identify the element , As the terms and conditions is false in the build provided)
    Given I launch my application
    When I tap on "Registration Test" button
    Then I am on "Log In" page
    Then I should see "Create Philips account" button
    When I tap on "Create Philips account" button
    Then I am on "Create Account" page
    And I see "Create Philips account" button is disabled
    When I enter credentials for verification
    And I toggle checkbox for Terms & condition
    When I tap Create Philips account button for data registering
    Then I am on "Create Account" page
    Then I see "Please verify your email" text
    When I tap on "Resend" button
    Then I see "Verification email sent" text


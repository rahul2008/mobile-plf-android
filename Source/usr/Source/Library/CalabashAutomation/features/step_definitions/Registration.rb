require 'calabash-android/operations'

INSTALLATION_STATE = {
    :installed => false
}

Before do |scenario|
  scenario_tags = scenario.source_tag_names
  if !INSTALLATION_STATE[:installed]
      uninstall_apps
      install_app(ENV['TEST_APP_PATH'])
      install_app(ENV['APP_PATH'])
      INSTALLATION_STATE[:installed] = true
  end

  if scenario_tags.include?('@reinstall')
    clear_app_data
  end

  start_test_server_in_background
end

After do |scenario|
  shutdown_test_server
end


Given(/^I launch my application$/) do
element_exists("* text:'Registration Test'")
end

When(/^I tap on "([^\"]*)" button$/) do |button_name|
 tap_when_element_exists("* {text CONTAINS[c] '#{button_name}'}")
end

Then(/^I am on "([^\"]*)" page$/) do |page_name|
 wait_for( timeout: 5  ) { query("TextView text:'#{page_name}'") }
 end
 
 Then(/^I should see "([^\"]*)" link$/) do |text|
  element_exists("* {text CONTAINS[c] '#{text}'}")
 end
 
 
 Then(/I touch on "([^\"]*)" link$/) do |text|
  touch("* id:'tv_reg_terms_and_condition' {text CONTAINS[c] '#{text}'}")
end


# Then(/I touch on "Privacy Notice" link$/) do 
 # touch("* id:'tv_reg_terms_and_condition'")
#end

Then(/^I am on Web Browser page$/) do
element_exists("* id:'jr_webview'")
end

Then(/^I should see "(.*?)" button$/) do |button_name|
query( "button text:'#{button_name}'" )
end

Then(/^I should see password hints below password field$/) do
element_exists("* id:'tv_icon_text_desc'")
end

When(/^I should see green tick against that hint$/) do
element_exists("* id:'tv_icon_text_desc'")
end

Then(/^I see "(.*?)" button is disabled$/) do |button_name|
query( "button text:'#{button_name}' enabled:'false'" )
end

Then(/^I see "(.*?)" button is enabled$/) do |button_name|
query( "button text:'#{button_name}' enabled:'true'" )
end

Then(/^I see unmask icon get highlighted$/) do
element_exists("* id:'tv_password_mask'")
end

When(/^I touch unmask icon of password field$/) do
touch("* id:'tv_password_mask'")
end

Then(/^I see unmasked password$/) do
element_exists("* id:'et_reg_password' focused:'true'")
step 'I wait for 2 seconds'
end

Then(/^I see masked password$/) do
element_exists("* id:'et_reg_password' focused:'false'")
step 'I wait for 2 seconds'
end

Then(/^I see checkbox for Terms and conditions$/) do
if element_exists("* id:'cb_reg_accept_terms'")
end
end


And(/^I toggle checkbox for Terms & condition$/) do
touch("* id:'cb_reg_accept_terms'")
end

When(/^I tap Create Philips account button for data registering$/) do
tap_when_element_exists("* id:'btn_reg_register'")
end

Then(/^I see error message stated "(.*?)"$/) do |message|
 element_exists("*{text CONTAINS[c] '#{message}'}")
end


And(/^I see message stating user already exist for philips or social media$/) do
element_exists("* id:'tv_reg_email_exist'")
end

Then(/^I navigate to "([^\"]*)" page$/) do |page_name|
wait_for( timeout: 5 ) { query("TextView text:'#{page_name}'") }
end

Then(/^I see "(.*?)" text$/) do |name|
sleep 2
  if not element_exists("*{text CONTAINS[c] '#{name}'}")
  screenshot_embed "The error message"
end
end

Then(/^Resend button won't work for 5 minutes$/) do
element_exists("* id:'btn_reg_resend' enabled:'false'")
end

Then(/^I see Reset Password message$/) do
element_exists("* id:'tv_reg_header_dialog_title'")
end

Then(/^I see "(.*?)" page$/) do |text|
wait_for( timeout: 5  ) { query("TextView text:'#{text}'") }
end

Then(/^I should not see toggle checkbox$/) do
if element_exists("* id:'cb_reg_accept_terms'")
  screenshot_embed
end
end

Then(/^I see unmask icon of password field$/) do
element_exists("* id:'tv_password_mask'")
end

When(/^I tap on Philips account button$/) do
tap_when_element_exists("* id:'tv_reg_provider_name'")
sleep 2
end

When(/^I tap on Log In button$/) do
tap_when_element_exists("* id:'btn_reg_sign_in'")
end

When(/^I tap on Log Out button$/) do
tap_when_element_exists("* id:'btn_reg_sign_out'")
end

When(/^I tap resend button of create account$/) do
tap_when_element_exists("* id:'btn_reg_resend'")
end

When(/^I tap on Continue button$/) do
tap_when_element_exists("* id:'reg_btn_continue'")
end

And(/^I toggle checkbox for Receive Philips news$/) do
touch("* id:'cb_reg_register_terms'")
end

When(/^I enter invalid email address for social login$/) do
sleep 2
touch("* id:'rl_reg_email_field'")
keyboard_enter_text "1106813077it@gmail.com"
hide_soft_keyboard
end

When(/^I enter valid email address for social login$/) do
sleep 2
touch("* id:'rl_reg_email_field'")
keyboard_enter_text "Registration_android@gmail.com"
hide_soft_keyboard
end

Then(/^I am on "(.*?)" social page$/) do |text|
element_exists("* id:'jr_webview' {text CONTAINS[c] '#{text}'}")
sleep 5
end


And(/^I touch facebook login button$/) do
sleep 5
touch("webview css:'#u_0_5'")
end


When(/^I touch continue button$/) do
tap_when_element_exists("* text:'Continue'")
sleep 5
end


Then(/^I touch on Google button$/) do
element_exists("* text:'Google+'")
tap_when_element_exists("* id:'tv_reg_provider_name' text:'Google+'")
sleep 5
end

Then(/^I am on the google page$/) do
element_exists("* id:'jr_webview'")
sleep 5
end

When(/^I enter values in email and password field$/) do
keyboard_enter_text "govind.testlab@gmail.com"
sleep 4
touch("webview css:'#next'")
sleep 3
keyboard_enter_text "govind@123"
sleep 3
end

And(/^I touch on Sign In button$/) do
touch("webview css:'#signIn'")
sleep 5
end

When(/^I touch on allow button$/) do
sleep 3
touch("webView css:'#submit_approve_access'")
sleep 5
end


When(/^I touch on cancel button$/) do 
element_exists("* text:'Cancel'")
tap_when_element_exists("* id:'btn_reg_cancel' text:'Cancel'")
end


And(/^I touch on Log Out button$/) do
tap_when_element_exists("* id:'btn_reg_sign_out'")
end

When(/^I touch toggle box to receive Philips announcements$/) do
element_exists("* id:'cb_reg_receive_philips_news'")
tap_when_element_exists("* id:'cb_reg_receive_philips_news'")
sleep 3
end


When(/^I touch on connect button$/) do 
element_exists("* text:'Connect'")
tap_when_element_exists("* id:'btn_reg_merg' text:'Connect'")
end

When(/^I enter values for facebook login credentials again$/) do
 touch("webview css:'._5ruq'")
sleep 5
q=keyboard_enter_text "govind.testlab@gmail.com"
while q==[]
q = keyboard_enter_text "govind.testlab@gmail.com"
end
hide_soft_keyboard
if q!=[]
touch("webview css:'*'")
hide_soft_keyboard
sleep 3
end
touch("webview css:'#u_0_0'")
keyboard_enter_text "govind@123"
hide_soft_keyboard
sleep 4
end

When(/^I enter values for inactivated facebook account$/) do
sleep 5
touch("webview css:'._5ruq'")
keyboard_enter_text "rahulkchulliparambil@gmail.com"
sleep 3
hide_soft_keyboard
sleep 4

 touch("webview css:'#u_0_0'")
keyboard_enter_text "rahul@12345"
hide_soft_keyboard
sleep 4
end


When(/^I enter values for facebook login$/) do
sleep 5
touch("webview css:'._5ruq'")
keyboard_enter_text "govind.testlab@gmail.com"
sleep 3
hide_soft_keyboard
sleep 4

 touch("webview css:'#u_0_0'")
keyboard_enter_text "govind@123"
hide_soft_keyboard
sleep 4
end


Then(/^I am on the facebook confirmation page$/) do
sleep 5
element_exists("* id:'jr_webview'")
sleep 5
end


And(/^I see check box opt in update from philips$/) do
if not element_exists("* id:'cb_reg_accept_terms',enabled:'true'")
  screenshot_embed
end
end

Then(/^I enter already merged id in email field and password$/) do 
sleep 3
keyboard_enter_text "pankaj.testlab@gmail.com"
sleep 4
touch("webview css:'#next'")
sleep 3
keyboard_enter_text "pankaj@123"
sleep 3
touch("webview css:'#signIn'")
sleep 3
end

Then(/^I tap on Allow button$/) do
sleep 3
touch("webview css:'#submit_approve_access'")
sleep 5
end

Then(/^I Continue$/) do
tap_when_element_exists("* id:'reg_btn_continue' text:'Continue'")
end

Then(/^again I touch on Continue button$/) do
tap_when_element_exists("* id:'reg_btn_continue' text:'Continue'")
end

Given(/^I am on "(.*?)" screen$/) do |text|
element_exists("* {text CONTAINS[c] '#{text}'}")
end

When(/^I enter username or email for registered user$/) do

touch("webview css:'#username_or_email'")
q = keyboard_enter_text "kullir12345@gmail.com"
while q==[]
q = keyboard_enter_text "kullir12345@gmail.com"
end
if q!=[]
touch("webview css:'*'")
hide_soft_keyboard
sleep 3
end
end

When(/^I enter password for registered user$/) do
sleep 3
touch("webview css:'#password'")
keyboard_enter_text "kullir@123"
hide_soft_keyboard
end

When(/^I tap on Authorise app button$/) do
tap_when_element_exists("webview css:'#allow'")
sleep 10
end

Then(/^I am on Log in page of philips$/) do
element_exists("* id:'jr_webview'")
end

When(/^I tap on continue button of login$/) do
tap_when_element_exists("* id:'reg_btn_continue'")
sleep 2
end

When(/^I enter username or email for new user$/) do
touch("webview css:'#username_or_email'")
q = keyboard_enter_text "philipsregis@gmail.com"
while q==[]
q = keyboard_enter_text "philipsregis@gmail.com"
end
if q!=[]
touch("webview css:'*'")
hide_soft_keyboard
sleep 3
end
end

When(/^I enter password for new user$/) do
sleep 3
touch("webview css:'#password'")
keyboard_enter_text "registration@123"
hide_soft_keyboard
end

When(/^I enter username or email not activated$/) do
touch("webview css:'#username_or_email'")
q = keyboard_enter_text "govind.testlab@gmail.com"
while q==[]
q = keyboard_enter_text "govind.testlab@gmail.com"
end
if q!=[]
touch("webview css:'*'")
hide_soft_keyboard
sleep 3
end
end

When(/^I enter password not activated$/) do
sleep 3
touch("webview css:'#password'")
keyboard_enter_text "registration@123"
hide_soft_keyboard
end

When(/^I tap continue button for Verification$/) do
sleep 2
tap_when_element_exists("* id:'btn_reg_continue'")
end

When(/^I touch on Continue button$/) do
tap_when_element_exists("* text:'Continue'")
sleep 5
end
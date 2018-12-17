
Then(/^I should see the "(.*?)" button$/) do |name|
element_exists("button marked:'#{name}'")
end	

Then(/^I touch the "(.*?)" button$/) do |name|
tap_when_element_exists("button marked:'#{name}'")
end	
  
Then(/^I should see "(.*?)" option$/) do |text|
element_exists("#{text}")
end	

Then(/^I touch the "(.*?)" option$/) do |text|
tap_when_element_exists("#{text}")
end	

Then(/^I click on DigitalCare$/) do
check_element_exists("* id:'launchDigitalCare'")
  touch("* id:'launchDigitalCare'")
end
When(/^I click on ProductReview$/) do
check_element_exists("* id:'tellus_PhilipsReviewButton'")
  touch("* id:'tellus_PhilipsReviewButton'")
end

Then(/^I click on Livechat$/) do
check_element_exists("* id:'contactUsChat'")
  touch("* id:'contactUsChat'")
end
Then(/^I click on Accept$/) do
check_element_exists("* id:'fragment_product_review_ok_button'")
  touch("* id:'fragment_product_review_ok_button'")
end
Then(/^I rate the app$/) do
check_element_exists("* id:'review_write_rate_product_ratingBar'")
  touch("RatingBar", :method_name=>'setProgress')
end


Then(/^I click on Chatnow$/) do
check_element_exists("* id:'chatNow'")
  touch("* id:'chatNow'")
end

Then(/^I touch the "([^\"]*)" text$/) do |text|
tap_when_element_exists("*{text CONTAINS[c] '#{text}'}")
end

Then(/^I should not see "(.*?)" option$/) do |contentDescription|
element_exists("* contentDescription:'Webpage not available'")
end
 
Then(/^I click on back$/)do
  touch("* id:'back_to_home_img'")
end
  

Then(/^I select"([^\"]*)" from "([^\"]*)"$/)do |item_identifier, spinner_identifier|
spinner = query("android.widget.Spinner marked:'#{spinner_identifier}'")		
if spinner.empty?	
tap_when_element_exists("android.widget.Spinner * marked:'#{spinner_identifier}'")	
else
touch(spinner)	
end	
tap_when_element_exists("android.widget.PopupWindow$PopupViewContainer * marked:'#{item_identifier}'")	
end
Then(/^I touch on the spinner2$/)do
  touch("* id:'spinner2'")
  end
  
Then(/^I touch on the spinner1$/)do
  touch("* id:'spinner1'")
  end  

Then(/^I touch on "(.*?)" country$/) do |text|
q=query("* {text CONTAINS[c] '#{text}'}")
while q==[]
scroll_down
q=query("* {text CONTAINS[c] '#{text}'}")
end
if q!=[]
tap_when_element_exists("* {text CONTAINS[c] '#{text}'}")
end
end

Then(/^I touch on "(.*?)" language$/) do |text|
q=query("* {text CONTAINS[c] '#{text}'}")
while q==[]
scroll_down
q=query("* {text CONTAINS[c] '#{text}'}")
end
if q!=[]
tap_when_element_exists("* {text CONTAINS[c] '#{text}'}")
end
end

Then(/^I tap the "([^\"]*)" text$/) do |text|
  tap_when_element_exists("* {text CONTAINS[c] '#{text}'}")
end

Then(/^I enter text into summary field$/) do 
res=query("* id:'summary_verified_field'")
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'summary_verified_field'")
 keyboard_enter_text "Not bad"
 check_element_exists("* id:'reviewSummaryIconValid'")
end
end
Then(/^I enter text into summary box$/) do 
res=query("* id:'summary_verified_field'")
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'summary_verified_field'")
 keyboard_enter_text "excellent"
 hide_soft_keyboard()
 check_element_exists("* id:'reviewSummaryIconValid'")
end
end
Then(/^I enter text into review field$/) do 
res=query("* id:'review_write_rate_product_header_description'")
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'review_write_rate_product_header_description'")
 keyboard_enter_text "dsbsnbcfsmbfmssvbds,bvdxzbvdvd,svbds,bvsdbvsdhhddhvdhvdhvdhbdhvxmbvsdvmxdhbvmcmbsmvbdbxbhbvmsdbvmsvxhbvsdhbsdv"
 hide_soft_keyboard()
end
end

Then(/^I enter text into review box$/) do 
res=query("* id:'review_write_rate_product_header_description'")
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'review_write_rate_product_header_description'")
 keyboard_enter_text "working with the markets in the region on POC's which are funded by the markets which ultimately create value to the business "
 hide_soft_keyboard()
end
end
Then(/^I enter text into name field$/) do 
res=query("* id:'name_verified_field'")
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'name_verified_field'")
 keyboard_enter_text "last couple"
 hide_soft_keyboard()
 check_element_exists("* id:'reviewNameIconValid'")
end
end

Then(/^I enter text into name box$/) do 
res=query("* id:'name_verified_field'") 
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'name_verified_field'")
 keyboard_enter_text "years as "
 hide_soft_keyboard()
 check_element_exists("* id:'reviewNameIconValid'")
end
end
Then(/^I enter text into email field$/) do 
scroll_down
res=query("* id:'email_verified_field'")
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'email_verified_field'")
 keyboard_enter_text "harwafj@gmail.com"
 hide_soft_keyboard()
 check_element_exists("* id:'reviewEmailIconValid'")
end
end

Then(/^I enter text into mail area$/) do 
scroll_down
res=query("* id:'email_verified_field'")
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'email_verified_field'")
 keyboard_enter_text "thsudg2hff9@hmail.com"
 hide_soft_keyboard()
 check_element_exists("* id:'reviewEmailIconValid'")
end
end
Then(/^I enter text into mail field$/) do 
scroll_down
res=query("* id:'email_verified_field'")
if res.empty?
 fail(msg="Error. Text box not found.") 
 else 
 touch("* id:'email_verified_field'")
 keyboard_enter_text "phwerhsu@gmail.com"
 hide_soft_keyboard()
 check_element_exists("* id:'reviewEmailIconValid'")
end
end

Then(/^I touch the ctn$/)do
touch("* id:'ctn'")
clear_text_in("* id:'ctn'")
end

Then(/^I enter text into product field$/) do
  keyboard_enter_text "BRE201/00" 
  hide_soft_keyboard()
end 

Then(/^I enter text into ctn area$/) do
  keyboard_enter_text "S9711/31" 
  hide_soft_keyboard()
end 
Then(/^I enter into ctn area$/) do
  keyboard_enter_text "HP6521/01" 
  hide_soft_keyboard()
end
Then(/^I enter into ctn area1$/) do
  keyboard_enter_text "HP6519/01" 
  hide_soft_keyboard()
end
Then(/^I enter into ctn area2$/) do
  keyboard_enter_text "HD2079/01" 
  hide_soft_keyboard()
end
Then(/^I enter into ctn area3$/) do
  keyboard_enter_text "HP6519/01" 
  hide_soft_keyboard()
end
Then(/^I enter into ctn area4$/) do
  keyboard_enter_text "HP6519/01" 
  hide_soft_keyboard()
end
Then(/^I enter into ctn area5$/) do
  keyboard_enter_text "SC2009/00" 
  hide_soft_keyboard()
end
Then(/^I enter into ctn area6$/) do
  keyboard_enter_text "HP6519/01" 
  hide_soft_keyboard()
end

Then(/^I enter into ctn area10$/) do
  keyboard_enter_text "SC2008/11" 
  hide_soft_keyboard()
end

Then(/^I enter into ctn area11$/) do
  keyboard_enter_text "S50420/04" 
  hide_soft_keyboard()
end

Then(/^I enter text into product area$/) do
  keyboard_enter_text "S5070/04" 
  hide_soft_keyboard()
end 

Then(/^I enter text into product text$/) do
  keyboard_enter_text "S9721/84" 
  hide_soft_keyboard()
end 
Then(/^I toggle the checkbox$/) do 
tap_when_element_exists("* id:'review_write_rate_product_terms_checkbox'")
check_element_exists("* id:'your_product_review_send_button'")
check_element_exists("* id:'your_product_review_cancel_button'")
touch("* id:'your_product_review_send_button'")
wait_for_element_exists("* id:'your_product_review_preview_send_button'")
touch("* id:'your_product_review_preview_send_button'")
wait_for_element_exists("* id:'your_product_review_thankyou_button'")
touch("* id:'your_product_review_thankyou_button'")
end

Then(/^I toggle the check$/) do 
tap_when_element_exists("* id:'review_write_rate_product_terms_checkbox'")
check_element_exists("* id:'your_product_review_send_button'")
check_element_exists("* id:'your_product_review_cancel_button'")
touch("* id:'your_product_review_send_button'")
wait_for_element_exists("* id:'your_product_review_preview_send_button'")
end

Then(/^I edit the text view$/) do 
tap_when_element_exists("* id:'review_write_rate_product_terms_checkbox'")
check_element_exists("* id:'your_product_review_send_button'")
check_element_exists("* id:'your_product_review_cancel_button'")
touch("* id:'your_product_review_send_button'")
wait_for_element_exists("* id:'your_product_review_preview_cancel_button'")
wait_for_element_exists("* id:'your_product_review_preview_send_button'")
touch("* id:'your_product_review_preview_send_button'")
wait_for_element_exists("* id:'your_product_review_thankyou_button'")
touch("* id:'back_to_home_img'")
touch("* id:'your_product_review_preview_cancel_button'")
check_element_exists("* id:'action_bar_title'")
end	



																						
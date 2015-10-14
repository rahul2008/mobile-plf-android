
Then(/^I should see the "(.*?)" button$/) do |name|
element_exists("button marked:'#{name}'")
end	

Then(/^I touch the "(.*?)" button$/) do |name|
tap_when_element_exists("button marked:'#{name}'")
end	
  
Then(/^I should see "(.*?)" option$/) do |text|
element_exists("#{text}")
end	

Then(/^I click on DigitalCare$/) do
check_element_exists("* id:'launchDigitalCare'")
  touch("* id:'launchDigitalCare'")
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











																																	
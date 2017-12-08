Feature:To test for various test cases in Digital Care


@spinner
Scenario:To test the functionality of the spinner identifier to select the language

Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds

@Launch
Scenario:To launch the DigitalCare as Activity
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare 
Then I should see "Support" option
Then I wait for 2 seconds


@Support2
Scenario: To check the contents that are present when the app is launched
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option

@contact
Scenario:Contact us
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "Contact us" button
Then I wait for 4 seconds
Then I should see "On Twitter" option
Then I should see "On Facebook" option
Then I should see "Live chat" option
Then I should see "Send email" option
Then I should see the "Call 1800 102 2929 " button

@product
Scenario:To view the product information
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "View product information" button

@map
Scenario:To check the location of the store
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "Find Philips near you" button

@readFAQ
Scenario:To read the FAq's
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "View FAQs" button
Then I should see "View FAQs" option
Then I wait for 5 seconds
Then I should not see "Webpage not available" option

@appreview
Scenario:To check for app-review feature
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "Tell us what you think" button
Then I should see the "Tell us what you think" option
Then I should see the button "Tell us what you think" button

@signing
Scenario:To check for sign into my philips feature
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "Sign into My Philips" button

@livechat
Scenario:To check for live chat option
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option 
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "Contact us" button
Then I wait for 4 seconds
Then I should see "On Twitter" option
Then I should see "On Facebook" option
Then I should see "Live chat" option
Then I should see "Send email" option
Then I should see the "Call 1800 102 2929 " button
Then I wait for 5 seconds
Then I click on Livechat
Then I should see "Chat with Philips" option
Then I should see the "Chat now" button
Then I should see the "Cancel" button
Then I click on Chatnow
Then I should see "Chat now" option

@NavigatingBack
Scenario:Navigating to the previous screen
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "Contact us" button
Then I wait for 4 seconds
Then I should see "On Twitter" option
Then I should see "On Facebook" option
Then I should see "Live chat" option
Then I should see "Send email" option
Then I should see the "Call 1800 102 2929 " button
Then I wait for 5 seconds
Then I click on Livechat
Then I should see "Chat with Philips" option
Then I should see the "Chat now" button
Then I should see the "Cancel" button
Then I click on Chatnow
Then I should see "Chat now" option
Then I click on back



@appreview1
Scenario:To check for appreview
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I should see "Tell us what you think" option
Then I should see the "Write an app-review" button


@facebook

Scenario:Checking the facebook option
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I select "India (in)" from "spinner2"
Then I wait for 3 seconds
Then I click on DigitalCare
Then I should see "Support" option
Then I should see "View product information" option
Then I should see "View FAQs" option
Then I should see "Contact us" option
Then I should see "Find Philips near you" option
Then I should see "Tell us what you think" option
Then I should see "Sign into My Philips" option
Then I touch the "Contact us" button
Then I wait for 4 seconds
Then I should see "On Twitter" option
Then I should see "On Facebook" option
Then I should see "Live chat" option
Then I should see "Send email" option
Then I should see the "Call 1800 102 2929 " button
Then I wait for 5 seconds
Then I touch the "On Facebook" button
Then I should see "Contact us" option
Then I wait for 5 seconds


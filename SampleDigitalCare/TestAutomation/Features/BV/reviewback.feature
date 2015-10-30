Feature:Submitting the product review and editing after going to the Thankyou page 
@review11
Scenario:To submit the product review

Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
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
Then I wait for 5 seconds
When I click on ProductReview
Then I should see "Write a review" option
Then I click on Accept
Then I rate the app
Then I enter text into summary box
Then I enter text into review box
Then I enter text into name box
Then I enter text into mail field
Then I edit the text view


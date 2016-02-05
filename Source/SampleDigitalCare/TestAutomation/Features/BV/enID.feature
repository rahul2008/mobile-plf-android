Feature:Submitting the product review with EN-ID combination
@enID
Scenario:To submit the product review by changing the language to english and country to Indonesia
Given I see the text "DigitalCareSample"
Then I wait for 2 seconds
Then I should see the "Launch DigitalCare as Activity" button
Then I should see the "Launch DigitalCare as Fragment" button
Then I select "English (en)" from "spinner1"
Then I touch on the spinner2
Then I touch on "ID" country
Then I touch the ctn
Then I enter text into product area
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
Then I enter text into summary field
Then I enter text into review field
Then I enter text into name field
Then I enter text into mail area
Then I toggle the check


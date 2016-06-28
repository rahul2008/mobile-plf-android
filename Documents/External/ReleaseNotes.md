
================================================================================
Release notes for User Registration Android 
================================================================================
Version 6.0.4     Date : 28-06-2016
--------------------------------------------------------------------------------
Components and versions in this release

regidtartionApi : 6.0.5
localeMatch : 1.1.1
tagging :2.0.0
hsdp :1.0.3
ntpCLient :1.0.3
jump : 5.0.3
coppa:1.0.0
jackson-annotations : 2.6.0
org.apache.http.legacy :1.0.0
jackson-core : 2.6.1
jackson-databind: 2.6.1
adobeMobileLibrary :4.5.1



--------------------------------------------------------------------------------
### New Features 
¬	HSDP login mechanism is changed 
¬	Reverted the HSDP Hot fix.



--------------------------------------------------------------------------------
### Known issues:
DE11150	[uGrow, DE1509] PR: Email verification: Click on verify and activate link (2nd time) returns account verification failed
DE11177	[uGrow, DE1521]: PR: After logging in the 'Almost done' screen is shown 2 times
DE11668	[uGrow Android] PR- Forgotten password mail 'Reset my password' link opens verify account page
DE9110	[uGrow iOS] PR: (Registration Component); Refreshing the token fails after the access token has expired
DE10773	[uGrow, DE1377] PR: unclear errors in User Registration
DE10952	[uGrow, DE1474]: Social login doesn't work with the following login providers: instagram, sinawiebo, QQ, VK, amazon
DE9283	Alignment for homepage should be right aligned for Arabic language
DE10906	HSDP 1 .6: The verify and activate leads to error message page 'No resource found'
DE10937	Traditional merge -Tap on Continue we can see message "That email address is already in use"
DE11060	Dual spinners , Dual error messages displayed while we tap on I have activated my account and resend button in parallel
DE11072	After we have have successfully reset password, "Your new password is set  Try to log into your Philips account with your new password" leads to No resource found
DE11266	Coppa - The 'Cancel' and 'Ok' button looks hidden might look complicated for some android users.
DE11628	Coppa: Simplified Chinese : Verify and activate link leading to '404 Not Found' page.
DE11635	The email ID which is provided for reset password is not recognised when user tries reset the password.
DE11642	Coppa : Enter the Year of birth as 0000 and after tap on ok we can see value displayed is 1956
DE11643	Coppa : UI Glitch is observed in disagree first,second consent
DE11648	Coppa : Trying to reset password for user tap on back button user details must be cleared similar to iOS
DE11649	Coppa : Arabic- localization issues
DE11650	Coppa : Arabic- Implementation of the I am under 16 and over 16 is not mirror as per the arabic language (Comparison can be observed with iOS implementation )
DE8086	Merge when Twitter is second social provider for Android	
DE8157	Merge screen(UR4ee) appears twice while we perform the merge between social acccounts	
DE8531	Email Field is seen in Almost done screen after pressing back option from 'Please verify your email screen' whereas on iOS email field is not shown.	
DE8866	UI Issue-Text in the text box is not displayed correctly(Arabic language-Create My Philips, My Philips screens)	
DE8867	User previously entered data  is not cleared when we tap on back button from My Philips Screen	
DE9090	Invalid error is triggered when tapping on resend option after verifying the email.	
DE9499	Incorrect account activation response to the user in the mobile web view after tapping on 'Verify and activate' via mobile	
DE10842	Approval screen pops up for a while even after accepting the first concent or 24 Hours concent.	
DE10908	HSDP 1.6 : List of social networking logos are observed when tapping on Facebook/Google Plus	
DE11318	Coppa: The network error message is observed in Approval screen when tapping on Continue button in Log In screen.	
================================================================================




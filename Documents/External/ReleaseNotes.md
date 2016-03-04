
================================================================================
Release notes for User Registration Android 
================================================================================
Version 5.0.0.rc.1     Date : 04-03-2016
--------------------------------------------------------------------------------
Components and versions in this release

regidtartionApi : 5.0.0.rc.1
localeMatch : 1.1.1
tagging :2.0.0
hsdp :1.0.2
ntpCLient :1.0.0
jump : 5.0.3
jackson-annotations : 2.6.0
org.apache.http.legacy :1.0.0
jackson-core : 2.6.1
jackson-databind: 2.6.1
adobeMobileLibrary :4.5.1



--------------------------------------------------------------------------------
### New Features 
1.Security implementation by encryption 
2.HSDP Traditional and Social implementation
3.Dynamic configuration
4.Code quality improvements
5.Removed Twitter support
6.Progaurd Enhancements
7.Bug fixes
8.Artifactory Supoort





--------------------------------------------------------------------------------
### Bugs fixed:

DE8768	Non Supported language (hindi) selection displayed unexpected language exceptions
DE9120	Provide proguard configuration and include it to library
DE9465	uGrow [Android][User registration] returns "user signed on" while hsdp GUID is still null
DE9657	Insecure Data Storage in Local Files
DE10121	DE9963- [uGrow Android] Random Crash observed when the app is not even running on the device.
DE5669	INT:UGrow-Error message appears in landscape	
DE5670	INT:UGrow-Dutch user registration screens do not contain Dutch text but English text with prefix nl	
DE5742	INT:uGrow-Unable to determine whether user was successfully logged in	
DE5802	INT: uGrow: clientID in  [JRCaptureData sharedCaptureData] is nil, crashing uGrow app (iOS), after phone restart	
DE6600	Android: crash at instantiation
DE7060	Coppa related with IT isseues
DE8266	Theming registration component
DE8387	Forgot Password button does not give feedback
DE9067	[uGrow] Welcome text is not as per the design (It is â€œWelcome!â€ In the design but it is â€œWelcomeâ€ in our App)
DE9068	[uGrow] Observed a crash while navigating from Sign-in screen
DE9069	[uGrow] When we select Signout option in User Registration component, No callbacks are seen for Android.
DE9070	[uGrow] Close icon should not be present in Welcome Sign In screen for Android
DE9204	User registration component prevents us from enabling minify (proguard)
DE9212	[uGrow Android] Continue button to be added in User Registration Module
DE9241	[BOND] Space is getting added as many times we tap on forgot password.
DE9242	[BOND] Warning message is cropped in 'Please verify your email screen'
DE9255	[uGrow Android] Crash while signin in User registration
DE9302	[uGrow, DE796] [Registration component] Copyright notice in verification mail not correct
DE9303	[uGrow, DE795] [Registration Component] Reset password is not working
DE9356	[uGrow, DE830] [User Registration] Not able to login with existing account: signature expired
DE9357	[uGrow, DE842] [User registration] Ability to log to file
DE9466	UGrow [Android][User registration] does not allow users to log in that have manually set their system clock to a time in the past
DE9468	uGrow: Philips UIkit 3.0.1 is currently not supported in the registration component.
DE9534	HealthyDrinks_Android[0.2.0] - App gets crashed on creating the new user registration.
DE9559	Android 6: Privacy Notes and Terms and Conditions links are not tap-able
DE9691	Digital care module Language does not change
DE9733	[uGrow DE968]: Additional robustness check needed in uGrow app before deciding that a user is logged in
DE9892	[uGrow Android] App crash when App is running background and start data synchronization(data synchronization using user profile data like user id)
DE9963	[uGrow Android] Random Crash observed when the app is not even running on the device.
DE10090	[uGrow Android] App crash observed when the User consent was accepted in the offline case.
DE10093	[uGrow Android] PR - App navigates back to intro screen after creation of a new user

--------------------------------------------------------------------------------
### Known issues:
DE6791	Registration component doesn't handle low memory conditions
DE8157	Merge screen(UR4ee) appears twice while we perform the merge between social acccounts
DE8866	UI Issue-Text in the text box is not displayed correctly(Arabic language-Create My Philips, My Philips screens)
DE8867	User previously entered data  is not cleared when we tap on back button from My Philips Screen
DE9090	Invalid error is triggered when tapping on resend option after verifying the email.
DE9499	Incorrect account activation response to the user in the mobile web view after tapping on 'Verify and activate' via mobile
DE10269	The user is not shown with any network error message and the app just keep showing loading indicator and stops in the Low network
DE8086	Merge when Twitter is second social provider for Android
DE9276	Error message is shown in english when the device language is set to Deutsch, France, Simplified Chinese, Traditional Chinese, Portugese, Russian, Arabic, Hindi
DE9283	Alignment for homepage should be right aligned for Arabic language
DE9328	[Multicooker]Screen transition animation customization is not allowed for Registration horizontal component
DE9869	'Forgotten Password' text font is small
DE9894	Uuid in Janrain and Logcat of Eclipse are different
DE9928	Login page fails to appear when tapping on Facebook as its giving an error message saying 'You are not logged in.........'
DE10025	Extra line division between welcome and logged in content after login
DE10134	Production - COPPA - Error occurred while processing the request when tapping on â€˜Confirm account and Concentâ€™ in email
DE10136	COPPA: The message â€˜Verification code has been expired' is observed when accepting the second Concent which received after 24 Hours.
DE10137	COPPA - Error occurs when not accepting the Concent received in email.
DE10257	Failed to refresh access token after changing the device date to Future/Past also logs are not getting displayed in the logcat
DE10280	[HSDP]The Refresh access token test button fails to function after killing and re-launching the app.
DE10301	The app keeps showing loading animation when tapping on opt in/opt out option in the logout screen in the Low Network/Proxy enabled network.
DE10302	The app keeps refreshing and shows the message Failed to refresh access token after keeping the app overnight with the user logged in My Philips.
DE10341	While logging out the my philips account if we instantly tap on continue button, notice the user is not logged out (while the app is still loading logout action).
DE10345	While My Philips Account login ,we can observe on tap of login button after the spinner has started . Login button would be highlighted rather greyed out
DE10369	COPPA Production: The message â€˜Verification code has been expired' is observed when accepting the the first concent

DE10343	[uGrow] Registration dynamic configuration issues.
DE10388	[uGrow Android] App crash : While logging out @ the My account screen, pressing on the device back key shows the empty time line - later tapping on the Options/try to save any tracker entries.
DE10343	[uGrow] Registration dynamic configuration issues.
DE10388	[uGrow Android] App crash : While logging out @ the My account screen, pressing on the device back key shows the empty time line - later tapping on the Options/try to save any tracker entries.

================================================================================




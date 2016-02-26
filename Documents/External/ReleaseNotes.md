
================================================================================
Release notes for User Registration Android 
================================================================================
Version v4.1.1.rc.4     Date : 26-02-2016
--------------------------------------------------------------------------------
Components and versions in this release

regidtartionApi : 4.1.1.rc.4
localeMatch : 1.1.1
tagging :2.0.0
hsdp :1.0.2
ntpCLient :1.0.0
jump : 5.0.1
jackson-annotations : 2.6.0
org.apache.http.legacy :1.0.0
jackson-core : 2.6.1
jackson-databind: 2.6.1
adobeMobileLibrary :4.5.1



--------------------------------------------------------------------------------
### New Features 
1.	Bug fixes 
2.  Encryption Janrain preference file



--------------------------------------------------------------------------------
### Bugs fixed:

DE5669 : INT:UGrow-Error message appears in landscape
DE5670 : INT:UGrow-Dutch user registration screens do not contain Dutch text but English text with prefix nl
DE5742 : INT:uGrow-Unable to determine whether user was successfully logged in
DE5802 : INT: uGrow: clientID in  [JRCaptureData sharedCaptureData] is nil, crashing uGrow app (iOS), after phone restart
DE6600 : Android: crash at instantiation
DE7060 : Coppa related with IT isseues
DE8266 : Theming registration component
DE8387 : Forgot Password button does not give feedback
DE9067 : [uGrow] Welcome text is not as per the design (It is â€œWelcome!â€ In the design but it is â€œWelcomeâ€ in our App)
DE9068 : [uGrow] Observed a crash while navigating from Sign-in screen
DE9069 : [uGrow] When we select Signout option in User Registration component, No callbacks are seen for Android.
DE9070 : [uGrow] Close icon should not be present in Welcome Sign In screen for Android
DE9204 : User registration component prevents us from enabling minify (proguard)
DE9212 : [uGrow Android] Continue button to be added in User Registration Module
DE9241 : [BOND] Space is getting added as many times we tap on forgot password.
DE9242 : [BOND] Warning message is cropped in 'Please verify your email screen'
DE9255 : [uGrow Android] Crash while signin in User registration
DE9302 : [uGrow, DE796] [Registration component] Copyright notice in verification mail not correct
DE9303 : [uGrow, DE795] [Registration Component] Reset password is not working
DE9356 : [uGrow, DE830] [User Registration] Not able to login with existing account: signature expired
DE9357 : [uGrow, DE842] [User registration] Ability to log to file
DE9404 : [Polaris] iOS --> Problem with integration Integrate User Registration
DE9466 : UGrow [Android][User registration] does not allow users to log in that have manually set their system clock to a time in the past
DE9468 : uGrow: Philips UIkit 3.0.1 is currently not supported in the registration component.
DE9534 : HealthyDrinks_Android[0.2.0] - App gets crashed on creating the new user registration.
DE9559 : Android 6: Privacy Notes and Terms and Conditions links are not tap-able
DE9691 : Digital care module Language does not change
DE9711 : Orlando-iOS] Account text is in English for Dutch language while creating an User registration
DE9723 : [Orlando:AndroidPhone] : Sign out is not working as expected ,after sign out ,and try tapping on google+,we still see the same login account
DE9733 : [uGrow DE968]: Additional robustness check needed in uGrow app before deciding that a user is logged in
DE9892 : [uGrow Android] App crash when App is running background and start data synchronization(data synchronization using user profile data like user id)
DE9917 : [BLR: Android Localization] User is unable to register the handle
DE9963 : [uGrow Android] Random Crash observed when the app is not even running on the device.
DE10090:[uGrow Android] App crash observed when the User consent was accepted in the offline case.
DE10093: [uGrow Android] PR - App navigates back to intro screen after creation of a new user

--------------------------------------------------------------------------------
### Known issues:
DE6791	: Registration component doesn't handle low memory conditions
DE8157	: Merge screen(UR4ee) appears twice while we perform the merge between social acccounts
DE8866	: UI Issue-Text in the text box is not displayed correctly(Arabic language-Create My Philips, My Philips screens)
DE8867	: User previously entered data  is not cleared when we tap on back button from My Philips Screen
DE9090	: Invalid error is triggered when tapping on resend option after verifying the email.
DE8086	: Merge when Twitter is second social provider for Android
DE9276	: Error message is shown in english when the device language is set to Deutsch, France, Simplified Chinese, Traditional Chinese, Portugese, Russian, Arabic, Hindi
DE9283	: Alignment for homepage should be right aligned for Arabic language
DE9328	: [Multicooker]Screen transition animation customization is not allowed for Registration horizontal component
DE9869	: 'Forgotten Password' text font is small
DE9894	: Uuid in Janrain and Logcat of Eclipse are different
DE9896	: Service error-message coming on Log In screen of Myphilips account
DE9928	: Login page fails to appear when tapping on Facebook as its giving an error message saying 'You are not logged in.........'
DE10025	: Extra line division between welcome and logged in content after login
DE10134	: COPPA - Error occurred while processing the request when tapping on â€˜Confirm account and Concentâ€™ in email
DE10136	: COPPA: The message â€˜Verification code has been expired' is observed when accepting the second Concent which received after 24 Hours.
DE10137	: COPPA - Error occurs when not accepting the Concent received in email.
DE10257	: Failed to refresh access token after changing the device date to Future/Past also logs are not getting displayed in the logcat
DE10269	: The user is not shown with any network error message and the app just keep showing loading indicator and stops in the Low network
DE10280	: [HSDP]The Refresh access token test button fails to function after killing and re-laucnching the app.
================================================================================




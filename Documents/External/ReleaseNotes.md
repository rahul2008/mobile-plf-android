
================================================================================
Release notes for User Registration Android 
================================================================================
Version 7.0.0     Date : 26-07-2016
--------------------------------------------------------------------------------
Components and versions in this release

•	jump.aar[v.6.0.3]
•	localeMatch.aar[v.2.1.0]
•	registrationApi.aar[v.7.0.0]
•	hsdp.aar[v.1.0.4]
•	servertime.aar[v.1.0.3]
•	adobeMobileLibrary.jar[v.4.8.3]
•	org.apache.http.legacy.jar
•	jackson-annotations.jar[v.2.6.0]
•	jackson-core.jar[v.2.6.1]
•	jackson-databind.jar[v.2.6.1]
•	okhttp-2.5.0.jar
•	okhttp-apache-2.5.0.jar
•	okio-1.6.0.jar
•	AppInfar.aar[v.1.1.0]
•	prx.arr[v.2.2.0]
•	gson.jar[v.2.2.2]
•	volley.aar[v.1.0.0]



--------------------------------------------------------------------------------
### New Features 

¬	COPPA Localization is completed.
¬	AppInfra component is consumed for Logging ,tagging and locale match sub components in registration STANDARD and COPPA modules. 
¬	COPPA new flow is implemented.
¬	HSDP Social sign is provided with the access token log in rather using login with password.
¬	HSDP refresh secrete for traditional and social HSDP sign on.
¬	Code quality improvements with TICS score 80.48%
¬	Bug fixes




--------------------------------------------------------------------------------
### Known issues:
DE12229	Text on the forgot password pop over not displayed
DE12408	[uGrow Android] Not able to Merge accounts (Philips and Facebook account)
DE11689	PR: Registaration - Verification eemail gives error message
DE11951	[uGrow Android] On Minimize/Maximize in login screen with continue button enabled, app navigates to Account settings screen
DE8086	Merge when Twitter is second social provider for Android
DE8157	Merge screen(UR4ee) appears twice while we perform the merge between social acccounts
DE8531	Email Field is seen in Almost done screen after pressing back option from 'Please verify your email screen' whereas on iOS email field is not shown.
DE8866	UI Issue-Text in the text box is not displayed correctly(Arabic language-Create My Philips, My Philips screens)
DE8867	User previously entered data  is not cleared when we tap on back button from My Philips Screen
DE9499	Incorrect account activation response to the user in the mobile web view after tapping on 'Verify and activate' via mobile
DE10908	HSDP 1.6 : List of social networking logos are observed when tapping on Facebook/Google Plus
DE11645	Coppa : Some inputs are invalid null displayed when the user in german
DE12059	Coppa(Deny Consent) - All the headers related to coppa deny consent are not bold when compared to design.
DE12117	HSDP (Eval) : Refresh token displayed for traditional login.
DE12144	China Mobile Number (Create account) - Error message fails to appear when entering the invalid code in ur_02_03 screen.
DE12145	China Mobile Number (Create account) - The app keeps loading when tapping on resend after entering some code.
DE9283	Alignment for homepage should be right aligned for Arabic language
DE10937	Traditional merge -Tap on Continue we can see message "That email address is already in use"
DE11072	After we have have successfully reset password, "Your new password is set  Try to log into your Philips account with your new password" leads to No resource found
DE11266	Coppa - The 'Cancel' and 'Ok' button looks hidden might look complicated for some android users.
DE11635	The email ID which is provided for reset password is not recognised when user tries reset the password.
DE11642	Coppa : Enter the Year of birth as 0000 and after tap on ok we can see value displayed is 1956
DE11643	Coppa : UI Glitch is observed in disagree first,second consent
DE11648	Coppa : Trying to reset password for user tap on back button user details must be cleared similar to iOS
DE11649	Coppa : Arabic- localization issues
DE11650	Coppa : Arabic- Implementation of the I am under 16 and over 16 is not mirror as per the arabic language (Comparison can be observed with iOS implementation )
DE11853	Social Login/Merge/My Philips Login fails to work in developer Mode.
DE11994	Staging : Account activation is not possible.
DE12011	Coppa(Deny Consent) - UI is not according to the design spec
DE12142	Performance - Blank screen is displayed before loading the page when logged in user taps on registration button
DE12203	HSDP/Standard: Facebook/Google Plus - The app keeps loading and does not let user to sign in sometimes reverts back to welcome screen.
DE12228	China Mobile Number (Create account/Login) - The Enter mobile number field accepts characters/Symbols in between the digits.
DE12261	Coppa(Deny Consent) - UI is not according to the design spec for CM-2 and CM-2.2 screens
DE12266	Coppa(Deny Consent) - Samsung Tab 4 (12 inch) UI and texts looks shrinked to the middle of the screen.
DE12277	Coppa(Deny Consent) - UI glitch when tapping on Continue option in Parental access screen.
DE12314	Coppa(Deny Consent) - "The token you passed was not valid" message is displayed when user taps on continue button.
DE12360	Dev environment - Receive marketing email status is showing as "False" in janrain even if the user accepts it while creating
DE12364	Coppa Deny Consent (Localisation) - "Verify and Activate" link not working for Non-US region
DE12422	Developer Mode [Coppa] - Observations/issues
DE12429	Coppa (Deny Consent) - CM 4.1 - The description text should be changed to 'Your Initial request' instead of 'Now' as per the new design.
DE12451	Coppa Deny Consent - The continue button fails to work and proceed to go to 'First Consent screen'.
=============================================================




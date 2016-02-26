
================================================================================
Release notes for User Registration Android 
================================================================================
Version v4.1.1.rc.4     Date : 05-02-2016
--------------------------------------------------------------------------------
Components and versions in this release


com.philips.cdp:registration:v4.1.1.rc.4 

--------------------------------------------------------------------------------
### New features:
1.	Security implementation by encryption 
2.	HSDP Traditional and Social implementation
3.	Dynamic configuration
4.	Code quality improvements

--------------------------------------------------------------------------------
### Bugs fixed:

1.  DE8154	Sign in failed-An error occurred while attempting to sign in
2.  DE9120	Provide proguard configuration and include it to library
3.  DE9250	Not able to go through the create account flow on a HTC Desire
4.  DE9465	uGrow [Android][User registration] returns "user signed on" while hsdp GUID is still null
5.  DE9499	Incorrect account activation response to the user in the mobile web view after tapping on 'Verify and activate' via mobile
6.  DE9573	UGrow [Android][User registration] does not allow users to log in that have manually set their system  clock to a time in the past- Android
7.  DE9655	Insecure Data Storage in Local Files
8.  DE9690	By default T&C need to be hidden. Only privacy policy need to be shown.
9.  DE9728	'Unauthorised access' message pops up and user is unable to login even entering valid credentials.
10. DE9732	The message 'Failed to contact to the server...' when tapping on 'I have activated my account'.
11. DE9736	Social merge not working - not taking user to UR-5a screen
12. DE9871	HSDP: The error message 'Signature expired.Please regenerate' message pops up when login with the exiting account via philips
--------------------------------------------------------------------------------
### Known issues:
1	Activation blocked by Ghostery in firefox and waterfox.
2	HSDP configurations not work sometime whenever HSDP server is down.
3	Email verification not working in Acceptance during Load & Performance testing in progress.
================================================================================
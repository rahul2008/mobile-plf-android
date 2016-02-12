PhilipsUIKit for Android Release Notes
======================================
Version - 2.0.0                               Date:12-02-2015
---------------------------------------------------------------
Feature completed 

Action bar / Toolbar
Social Icon
Spring Board
Lists
Inline Forms
Springboard Buttons and Springboard Buttons with icon
Progress bar / spinner
Controls
Tab bar
Modal Alert
Pop Overmenu
Cards
Favorites
Picker View



Version - 1.2.0                               Date:28-01-2015
---------------------------------------------------------------
Supporting gradle version 1.5.0

Add below line inside defultConfig in app build.gradle file 
defaultConfig {

    generatedDensities = []
}

This will block generating PNG from vector resource.
UIKIT support backward compatibility for vector support.

	It can be used via programmatically.
	Example: -> Drawable d = 
	com.philips.cdp.uikit.drawable.VectorDrawable.create(context, R.drawable.xyz);

	imageView.setImageDrawable(d);
	
### Bugs Fixed

DE9720 : Crash issue fixed for PUI switch.



Version - 1.1.0                               Date:18-01-2015
---------------------------------------------------------------
Documentation available in java doc format.

Feature completed 
1.	Hamburger menu
2.	About Screen

New feature under development:
1.	Modal alert
2.	Inline forms
3.	Cards
4.	Favorites
5.	Picker View
6.	Springboard Buttons and Springboard Buttons with icon
7.	Progress bar / spinner
8.	Controls



Version - 1.0.0								Date:04-12-2015	
----------------------------------------------------------------
Git: http://pww.cljenkins.pic.philips.com:9000/scm/git/hor-philipsuikit-android 

First release of Android UIKit.

Please refer integration document (BasicSetUp.docx in the archive) for using themes and other various UI components.

Completed features:
1.	Themes: Dark Blue, Bright Orange, Bright aqua and Light green.
2.	Buttons
3.	Action buttons
4.	Up button with action bar
5.	Hamburger menu (minor issues)
6.	Rating Star
7.	Notification Labels
8.	Tab bar (minor issues)
9.	Action Icons
10.	Input text fields.
11.	Social Icons
12.	Dot Navigation
13.	Image navigation
14.	Splash Screen
15.	About Screen (minor issues)
16.	Slider
17.	Range Slider
18.	Switches
19.	Notification bar (pop over alert)
20.	Lists (minor issues) 

Feature under development:
9.	Popover menu in action bar
10.	Spring board
11.	Modal alert
12.	Inline forms


Improvement Plans: Improvement of documentation in Javadoc/ unified html.


### Bugs Fixed



Version 0.2.0
------------------

### Bugs Fixed

Fixed missing font file.


Version 0.1.0
------------------

### Bugs Fixed


### New Features

* [EHUFA-11] Buttons are now part of the library-provided theming.
* [EHUFA-16] Library provides several splash screen templates.
* [EHUFA-21] ActionButtons are now part of the library-provided theming.
* [EHUFA-10] Screen backgrounds are now part of the library-provided theming.

### Bugs Fixed

* 

### Known Issues

* 

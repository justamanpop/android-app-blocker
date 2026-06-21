## Chrome killer
When Google Chrome is in the foreground, shows an overlay blocking interaction with it.

### Why?
Chrome on android does not support extensions, so could not use LeechBlock to restrict usage.
It is also a system app and so cannot be uninstalled, only disabled.

Existing app blockers can be bypassed by removing Chrome from list of blocked apps. This app
lets chrome be blocked without an easy way to remove the block (except uninstalling)

### Permissions needed
Needs Accessibility service permission, display notification permission (to run as a foreground service so
the service does not get suspended by android for battery optimization) and display over other apps permissions. 
The app automatically detects if  these permissions are missing and gives you buttons to click to guide you to 
appropriate setting to grant them

### Using it to block other apps
Currently it blocks a hardcoded list of apps, they are in `getBlockedPackageNames()` function in `ForegroundAppService.kt`.
Add package names  as needed, or replace implementation with anything else, like reading from local prefs, calling an API,
etc. to suit your needs.

Note: To get an android app's package name check its URL in the Google Play Store web browser, where the package name appears right after the id= parameter. 
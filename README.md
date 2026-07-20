## App blocker

### Function
When a blocked app is in the foreground, shows an overlay blocking interaction with it.

### Permissions needed
Needs Accessibility service permission, display notification permission (to run as a foreground service so
the service does not get suspended by android for battery optimization) and display over other apps permissions. 

The app automatically detects if  these permissions are missing and gives you buttons to click to guide you to 
appropriate setting to grant them, except for accessibility permission, which needs an extra step.

For accessibility permission, need to go to Settings > Apps > App Blocker > Click on 3 dots in top right corner > Click allow first,
then follow instructions in app to grant the permission.

### Planned Changes
- Make list of apps on system searchable in the add to block list screen
- Add some friction to removal of app from block list so that one can't simply remove an app when an urge hits
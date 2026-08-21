## App blocker

### Function
Create block sets containing list of apps to block and what days and time they are active in. 
When an app in an active block set is in the foreground, shows an overlay blocking interaction with it.

### Permissions needed
Needs Accessibility service permission, display notification permission (to run as a foreground service so
the service does not get suspended by android for battery optimization) and display over other apps permissions. 

The app automatically detects if  these permissions are missing and gives you buttons to click to guide you to 
appropriate setting to grant them, except for accessibility permission, which needs an extra step.

For accessibility permission, need to go to Settings > Apps > App Blocker > Click on 3 dots in top right corner > Click allow first,
then follow instructions in app to grant the permission.

### Planned Changes
- Add better visual indication that something is not deletable (tooltip with when it's available to edit maybe)
- Add info somewhere that this app does not allow to undo/change blocked app for x amount of time. Maybe a one time welcome screen or a help button
- Add tooltips/improve UX for initial grant permissions modal for each permission
- Make edit "lock" time a configurable setting
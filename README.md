## App blocker

### Function
Create block sets containing list of apps to block and what days and time they are active in. 
When an app in an active block set is in the foreground, shows an overlay blocking interaction with it.

### Showcase
Manage sets of blocked apps in block sets
<img width="757" height="1600" alt="image" src="https://github.com/user-attachments/assets/bb59e60d-5f17-401b-bed7-3557d8aa358a" />

<img width="777" height="1600" alt="image" src="https://github.com/user-attachments/assets/2577b384-70a9-4333-8217-f7c86bd9e321" />

Search and add apps to block sets
<img width="757" height="1600" alt="image" src="https://github.com/user-attachments/assets/b38d4f3d-ecf9-44cb-b6f7-d5079a767998" />

Configure "lock" durations in settings that prevent you from modifying block sets and apps after adding them to a list for a certain period of time. Meant to stop you from simply undoing blocks in a moment of desire/weakness:
<img width="763" height="1600" alt="image" src="https://github.com/user-attachments/assets/be88a618-323b-4a2b-a746-ffc2d80a1cdc" />

lock icons indicate you cannot edit:
<img width="764" height="1600" alt="image" src="https://github.com/user-attachments/assets/65499a29-e395-4688-8ebe-89fd8a7082d6" />



### Permissions needed
Needs Accessibility service permission, display notification permission (to run as a foreground service so
the service does not get suspended by android for battery optimization) and display over other apps permissions. 

The app automatically detects if  these permissions are missing and gives you buttons to click to guide you to 
appropriate setting to grant them, except for accessibility permission, which needs an extra step.

For accessibility permission, need to go to Settings > Apps > App Blocker > Click on 3 dots in top right corner > Click allow first,
then follow instructions in app to grant the permission.

### Planned Changes
- Add lock time for updating settings itself
- Add info somewhere that this app does not allow to undo/change blocked app for x amount of time. Maybe a one time welcome screen or a help button
- Add tooltips/improve UX for initial grant permissions modal for each permission

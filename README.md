## App blocker

### Function
Create block sets containing list of apps to block and what days and time they are active in. 
When an app in an active block set is in the foreground, shows an overlay blocking interaction with it.

---
### Showcase
#### Manage sets of blocked apps in block sets
<br>
<img width="757" height="1600" alt="image" src="https://github.com/user-attachments/assets/bb59e60d-5f17-401b-bed7-3557d8aa358a" />
<br>
<br>
<img width="777" height="1600" alt="image" src="https://github.com/user-attachments/assets/2577b384-70a9-4333-8217-f7c86bd9e321" />
<br>
<br>

#### Search and add apps to block sets
<br>
<img width="757" height="1600" alt="image" src="https://github.com/user-attachments/assets/b38d4f3d-ecf9-44cb-b6f7-d5079a767998" />
<br>
<br>

##### Configure "lock" durations in settings. They prevent you from:
- modifying block sets right after create/update so that they cannot be inactivated/deleted for a certain duration.
- removing blocked apps from block sets after adding them for a certain duration.
- Modifying settings itself to reduce lock durations to zero, and so modify the other settings. 

Meant to stop you from simply disabling a blocked app in a moment of desire/weakness.

<img width="773" height="1600" alt="image" src="https://github.com/user-attachments/assets/5f3757d0-4b41-43fa-86ff-18390261b1db" />
<br>
<br>

#### lock icons indicate you cannot edit:
<img width="764" height="1600" alt="image" src="https://github.com/user-attachments/assets/65499a29-e395-4688-8ebe-89fd8a7082d6" />
<br>

---
### Permissions needed
Needs Accessibility service permission, display notification permission (to run as a foreground service so
the service does not get suspended by android for battery optimization) and display over other apps permissions. 

The app automatically detects if  these permissions are missing and gives you buttons to click to guide you to 
appropriate setting to grant them, except for accessibility permission, which needs an extra step.

For accessibility permission, need to go to Settings > Apps > App Blocker > Click on 3 dots in top right corner > Click allow first,
then follow instructions in app to grant the permission.

### Planned Changes
- Add tooltips/improve UX for initial grant permissions modal for each permission
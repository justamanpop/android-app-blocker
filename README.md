## Chrome killer
When Google Chrome is in the foreground, shows an overlay blocking interaction with it.

### Why?
Chrome on android does not support extensions, so could not use LeechBlock to restrict usage.
It is also a system app and so cannot be uninstalled, only disabled.

Existing app blockers can be bypassed by removing Chrome from list of blocked apps. This app
lets chrome be blocked without an easy way to remove the block (except uninstalling)

### Permissions needed
Needs Accessibility service permission and display over other apps permissions. The app automatically detects if
these permissions are missing and gives you buttons to click to guide you to appropriate setting to grant them

### Using it for other apps
Replace `com.android.chrome` in ForegroundAppService.kt with package name of whatever app you want to block.
To block a list of apps, simply create a list of package names to block, then make the condition from
`if(packageName == "com.android.chrome")` to `if(packageNameList.contains(packageName))`
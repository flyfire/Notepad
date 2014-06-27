00_GettingStarted
===================
## Building Your First App ##
+ ``android list targets``
+ ``android create project --target <target-id> --name MyFirstApp --path <path-to-workspace>/MyFirstApp --activity MainActivity --package com.example.myfirstapp``
+ ``ant debug``
+ ``adb install bin/MyFirstApp-debug.apk``
+ ``android avd``

## Adding the Action Bar ##
+ All action buttons and other items available in the action overflow are defined in an XML menu resource. To add actions to the action bar, create a new XML file in your project's ``res/menu/`` directory.Add an ``<item>`` element for each item you want to include in the action bar. 
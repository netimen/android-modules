# Android Modules Sample App
This is a very simple maps app. It has two main functions: user can search for a place and measure distances.

In Android terms we have a single activity with the maps view on it and some additional UI for user to perform desired actions. The obvious solution would be to put all the code in the DemoActivity class, because we need the GoogleMap to perform all the functions. But actually the measuring of distances and searching for places are quite independendent tasks, so we put them to separate submodules using the Android Modules library

![Alt text](mapsdemoscreenshot.png?raw=true "app screenshot")

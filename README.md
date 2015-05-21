# Android Modules
A library allowing to split large classes into several independent modules. This is especially useful when we have some activity or fragment heavily using a single UI item such as [WebView](https://developer.android.com/reference/android/webkit/WebView.html) or [MapView](https://developer.android.com/reference/com/google/android/gms/maps/MapView.html) and performing several logically independent tasks on them.

**Android Modules** offer a simple way to split the code into several independent units (let's call them *submodules*). Benefits are:
 1. Code readability and maintainability is improved
 2. Code reuse
 3. We can easily combine submodules, replace them or turn them off, so we can provide several "flavors" of the functionality

Like in [Android Annotations](https://github.com/excilys/androidannotations) all the code is generated and reflection isn't used, so we don't get much performance impact. Also the library uses only [3 very small classes](annotations/src/main/java/com/netimen/androidmodules/helpers) to function, so it doesn't add much to the APK size.

## Release Notes
Version **1.2.2** is out. Supports [Android Annotations](https://github.com/excilys/androidannotations) **3.3.1**

## API usage
```java
@EModule(submodules={FindPlaceSubmodule.class, CalcDistanceSubmodule.class, UserActionsSubmodule.class})
public class MapsActivity extends Activity {
}
```

Now we can put all the code into submodule classes, which must be annotated with [@EBean](https://github.com/excilys/androidannotations/wiki/Enhance-custom-classes)
```java
@EBean
public class UserActionsSubmodule {
    @Inject
    Bus bus; // submodules don't call each other's methods directly and use Bus to communicate.

    @Click
    void clear() {
        bus.event(new ClearMap());
    }
}

@EBean
public class FindPlaceSubmodule {

    @ViewById
    SearchView search;

    @Event
    void clearMap() { // subscribes to ClearMap event issued by UserActionsSubmodule
        search.clearFocus();
    }
    ... // some code related to finding places, adding markers etc.
}

@EBean
public class CalcDistanceSubmodule {

    @ViewById
    TextView distance;

    @Event
    void clearMap() { // subscribes to ClearMap event issued by UserActionsSubmodule
        distance.setText(null);
    }
    ... // some code related to distance calculation
}
```
## Installation
Almost the same as [Android Annotations](https://github.com/excilys/androidannotations/wiki/Building-Project-Gradle)

```groovy
buildscript {
    repositories {
      mavenCentral()
    }
    dependencies {
        // replace with the current version of the Android plugin
        classpath 'com.android.tools.build:gradle:1.2.3'
        // replace with the current version of the android-apt plugin
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
    }
}

repositories {
    jCenter()
}

apply plugin: 'com.android.application'
apply plugin: 'android-apt'

dependencies {
    compile 'org.androidannotations:androidannotations-api:3.3.1'
    compile 'com.netimen:android-modules-api:1.2.2'
    apt 'com.netimen:android-modules-apt:1.2.2' // we replace Android Annotations processor with Android Modules processor
}

apt { // exactly same arguments as for Android Annotations
    arguments {
        //noinspection GroovyAssignabilityCheck
        androidManifestFile variant.outputs[0].processResources.manifestFile
    }
}

android {
    compileSdkVersion 19
    buildToolsVersion "22.0.1"

    defaultConfig {
        minSdkVersion 9
        targetSdkVersion 19
    }

}
```
## Working Sample
Please see [this project](https://github.com/netimen/android-modules-demo)

## Relations to Android Annotations library
**Android Modules** is inspired by and is only possible thanks to [Android Annotations](https://github.com/excilys/androidannotations).

But here comes the complex part. Due to java limitations if Android Annotations library genreated a MapsActivity_.java file, no other annotation processing library can modify it. So to use simultaneously Android Modules and Android Annotations I had to plug into the Android Annotations library. Currently this solution is not officially supported by Android Annotations and is a little bit hacky, but hopefully they will provide plugin system in a future release


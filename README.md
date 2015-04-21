# Android Modules
A library allowing to split large classes into several independent modules. This is especially useful when we have some activity or fragment heavily using a single UI item such as [WebView](https://developer.android.com/reference/android/webkit/WebView.html) or [MapView](https://developer.android.com/reference/com/google/android/gms/maps/MapView.html) and performing several logically independent tasks on them.

**Android Modules** offer a simple way to split the code into several independent units (let's call them *submodules*). Benefits are:
 1. Code readability and maintainability is improved
 2. Code reuse
 3. We can easily combine submodules, replace them or turn them off, so we can provide several "flavors" of the functionality

Like in [Android Annotations](https://github.com/excilys/androidannotations) all the code is generated and reflection isn't used, so we don't get much performance impact. Also the library uses only [3 very small classes](annotations/src/main/java/com/netimen/androidmodules/helpers) to function, so it doesn't add much to the APK size.

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
Same as [Android Annotations](https://github.com/excilys/androidannotations/wiki/Building-Project-Gradle)

```groovy
buildscript {
    repositories {
      mavenCentral()
    }
    dependencies {
        // replace with the current version of the Android plugin
        classpath 'com.android.tools.build:gradle:1.1.3'
        // replace with the current version of the android-apt plugin
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

apply plugin: 'com.android.application'
apply plugin: 'android-apt'
def AAVersion = 'XXX'

dependencies {
    apt "org.androidannotations:androidannotations:$AAVersion"
    compile "org.androidannotations:androidannotations-api:$AAVersion"
}

apt {
    arguments {
        androidManifestFile variant.outputs[0].processResources.manifestFile
        // if you have multiple outputs (when using splits), you may want to have other index than 0

        resourcePackageName 'com.myproject.package'

        // If you're using Android NBS flavors you should use the following line instead of hard-coded packageName
        // resourcePackageName android.defaultConfig.applicationId

        // You can set optional annotation processing options here, like these commented options:
        // logLevel 'INFO'
        // logFile '/var/log/aa.log'
    }
}

android {
    compileSdkVersion 19
    buildToolsVersion "22.0.1"

    defaultConfig {
        minSdkVersion 9
        targetSdkVersion 19
    }

    // This is only needed if you project structure doesn't fit the one found here
    // http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Project-Structure
    sourceSets {
        main {
            // manifest.srcFile 'src/main/AndroidManifest.xml'
            // java.srcDirs = ['src/main/java', 'build/generated/source/apt/${variant.dirName}']
            // resources.srcDirs = ['src/main/resources']
            // res.srcDirs = ['src/main/res']
            // assets.srcDirs = ['src/main/assets']
        }
    }
}
```

Inspired by and thanks to [Android Annotations](https://github.com/excilys/androidannotations)


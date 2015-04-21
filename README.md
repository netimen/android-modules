# Android Modules
A library allowing to split large classes into several independent modules. This is especially useful when we have some activity or fragment heavily using a single UI item such as [WebView](https://developer.android.com/reference/android/webkit/WebView.html) or [MapView](https://developer.android.com/reference/com/google/android/gms/maps/MapView.html) and performing several logically independent tasks on them.

**Android Modules** offer a simple way to split the code into several independent units (let's call them *submodules*). Benefits are:
1. Code readability and maintainability is improved
2. Code reuse
3. We can easily combine submodules, replace them or turn them off, so we can provide several "flavors" of the functionality

Like in [Android Annotations](https://github.com/excilys/androidannotations) all the code is generated and reflection isn't used, so we don't get any performance impact. Also the library uses only [3 very small classes](annotations/src/main/java/com/netimen/androidmodules/helpers) to function, so it doesn't add much to the APK size.

```java
@EModule(submodules={UrlLoadModule.class, FindInPageModule.class})
public class WebFragment extends Fragment {
	
}
```
Inspired by and thanks to [Android Annotations](https://github.com/excilys/androidannotations)


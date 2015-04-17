# android-modules
A library allowing to split large classes into several independent modules

```java
@EModule(submodules={UrlLoadModule.class, FindInPageModule.class})
public class WebFragment extends Fragment {
	
}
```
Thanks to https://github.com/excilys/androidannotations


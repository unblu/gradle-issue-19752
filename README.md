# Reproducer for gradle issue #19752

See: https://github.com/gradle/gradle/issues/19752

This project has 2 gradle build.

* A java library located inside `extra/userLib/`
* The main build (almost empty) located at the root of the repository

The second build is including the first:

* Include is defined in `setting.gradle`
* And the task `build` of the main build is configured to depend on the `build` task of `userLib` (see `build.gradle`) 

This is the way to force some paths to be displayed in the javadoc of the generated java class.

With `grep -R "This dependency was declared" .` you can locate the corresponding file. On my machine:

```
./.gradle/7.3/dependencies-accessors/3e6a92b4cd1989abb064a84d8f5e3e0954830c8c/sources/org/gradle/accessors/dm/LibrariesForLibs.java
```

And inside this `LibrariesForLibs.java` you see the problematic path in the javadoc:

```java
        /**
         * Creates a dependency provider for assertj (org.assertj:assertj-core)
         * This dependency was declared in settings file 'extra/userLib/settings.gradle'
         */
        public Provider<MinimalExternalModuleDependency> getAssertj() { return create("assertj"); }
```

`extra/userLib/settings.gradle` on my linux machine is turned to `extra\userLib\settings.gradle` on windows. As explained in the original issue `\u` is problematic.

In the GitHub action run `build-on-Windows` the error can be seen (🔴 build):

```
FAILURE: Build failed with an exception.
* What went wrong:
org.gradle.api.internal.catalog.GeneratedClassCompilationException: Unable to compile generated sources:
  - File LibrariesForLibs.java, line: 34, illegal unicode escape
  - File LibrariesForLibs.java, line: 64, illegal unicode escape
  - File LibrariesForLibs.java, line: 70, illegal unicode escape
  - File LibrariesForLibs.java, line: 84, illegal unicode escape
> Unable to compile generated sources:
    - File LibrariesForLibs.java, line: 34, illegal unicode escape
    - File LibrariesForLibs.java, line: 64, illegal unicode escape
    - File LibrariesForLibs.java, line: 70, illegal unicode escape
    - File LibrariesForLibs.java, line: 84, illegal unicode escape
```

The other build step using linux is working well (🟢 build)

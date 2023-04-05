# kotlinx-uuid

> #### This is a fork from https://github.com/cy6erGn0m/kotlinx-uuid, released under Apache 2.
> #### The main implementation was thankfully provided by [Sergey Mashkov (cy6erGn0m)](https://github.com/cy6erGn0m)!

`kotlinx-uuid` is a multiplatform (MPP) [Kotlin](https://kotlinlang.org) library introducing support
for [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier).

- [Source code](https://github.com/hfhbd/kotlinx-uuid)
- [Docs](https://uuid.softwork.app)

The main class `UUID` is serializable out of the box, so the library depends
on [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization). If you don't need serialization, you don't
need to apply the plugin.

Supported platforms are `jvm`, `js(IR)` and all tier 1, 2 and 3 native targets except the following:

```
js(LEGACY)

// tier 3
androidNativeArm32()
androidNativeArm64()
androidNativeX86()
androidNativeX64()

watchosDeviceArm64()
```

## Install

This package is uploaded to `mavenCentral`.

````kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("app.softwork:kotlinx-uuid-core:LATEST")
}
````

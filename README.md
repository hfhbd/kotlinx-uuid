[![JetBrains incubator project](https://jb.gg/badges/incubator.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub) 
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) 

> ## This library is not yet published 
> and it's sources published for review purposes
> you can't use it for now

# kotlinx-uuid

> ##### This library is experimental, and it's not recommended for production at the moment.

kotlinx-uuid is a multiplatform (MPP) [Kotlin](https://kotlinlang.org) library 
introducing support for [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier).

The main class `UUID` is serializable out of the box, so the library 
depends on [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization).
If you don't need serialization, you don't need to apply the plugin.

Supported platforms are:
- JVM (Java 6+)
- JavaScript (including the new IR backend)
- Native:
    - MacOS X64, _iOS ARM32 (untested)_, _iOS ARM64 (untested)_
    - Linux X64, _ARM64 (untested)_

> *untested means that on that platform we didn't run any 
> tests, just compiled.

## Including

```kotlin
val commonMain by getting {
    dependencies {
        implementation("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:$version")
    }
}
```

> In a regular (non-MPP) project you don't need the first line, 
> only the dependencies block.

## Usage

#### Creating from a UUID string

```kotlin
val uuid = UUID("1b3e4567-e99b-13d3-a476-446657420000")
val guid = UUID("{1b3e4567-e99b-13d3-a476-446657420000}")
```

#### Generating UUID4 using random

```kotlin
// using the default insecure kotlin.Random
val uuid = UUID.generateUUID()

// use secure random (just like java.util.UUID does)
val secure = UUID.generateUUID(SecureRandom().asKotlinRandom())
```

#### Serializing (kotlinx.serialization)

There are two serializers for `UUID`: the default one and the binary.

The default serializer does always serialize UUIDs as string primitives.

```kotlin
Json.encodeToString(uuid) == "\"1b3e4567-e99b-13d3-a476-446657420000\""
```

The additional serializer is useful for binary formats. 
Because they are not human-readable, and it's possible to reduce size.

```kotlin
val bytes = Protobuf.encodeToByteArray(BinarySerializer, uuid)
```

## Using with ktor

Include `ktor-server-uuid` artifact:

```kotlin
val jvmMain by getting {
    dependencies {
        implementation("org.jetbrains.kotlinx.experimental:ktor-server-uuid:$version")
    }
}
```

Install converter:

```kotlin
install(DataConversion) {
    uuid()
}
```

Unfortunately, ktor [doesn't provide (KTOR-1309)](https://youtrack.jetbrains.com/issue/KTOR-1309) 
any way to plug converters automatically, so you need to configure it manually.
Also, in some cases, like delegating to call parameters, it will not work
and there is no workaround at the moment.

## Migrating from java.util.UUID

Apply the following steps:

1. Remove all imports of `import java.util.UUID`
2. Add imports for UUID
```kotlin
import kotlinx.uuid.UUID
import kotlinx.uuid.*
```

3. Follow deprecations and apply all suggested replacements.

For example:
```kotlin
import java.util.UUID

fun f(id: String): UUID = UUID.fromString(id)
```

Becomes

```kotlin
import kotlinx.uuid.UUID

fun f(id: String) = UUID(id)
```

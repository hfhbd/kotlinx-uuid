[![JetBrains incubator project](https://jb.gg/badges/incubator.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub) 
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/cy6ergn0m/uuid/kotlinx-uuid/images/download.svg?version=0.0.1) ](https://bintray.com/cy6ergn0m/uuid/kotlinx-uuid/0.0.1/link)

# kotlinx-uuid

> ##### This library is experimental, and it's future support is not guaranteed.

##### Table of Contents  
- [Including](#including)
- [Usage](#usage): [from string](#creating-from-a-uuid-string),
  [generating](#generating-uuid4-using-random), 
  [hashing](#generating-uuid5-using-hash)
- [Serialization](#serializing-kotlinxserialization)
- [Using with ktor](#using-with-ktor)
- [Migrating from `java.util.UUID`](#migrating-from-javautiluuid)
- [Using with Exposed](#using-with-exposed)
- [Using with Gson](#using-with-gson)
- [Using with Jackson](#using-with-jackson)

kotlinx-uuid is a multiplatform (MPP) [Kotlin](https://kotlinlang.org) library 
introducing support for [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier).

The main class `UUID` is serializable out of the box, so the library 
depends on [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization).
If you don't need serialization, you don't need to apply the plugin.

Supported platforms are:
- JVM (Java 6+)
- JavaScript (including the new IR backend)
- Native:
    - MacOS X64, _iOS ARM64 (untested)_
    - Linux X64, _ARM64 (untested)_

> *untested means that on that platform we didn't run any 
> tests, just compiled.

## Including

```kotlin
repositories {
    maven("https://dl.bintray.com/cy6ergn0m/uuid")
}

val commonMain by getting {
    dependencies {
        implementation("org.jetbrains.kotlinx.experimental:kotlinx-uuid-core:0.0.1")
    }
}
```

> In a regular (non-MPP) project you don't need the line with "commonMain", 
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
#### Generating UUID5 using hash

kotlinx-uuid provides the ability to generate
uuids by hashing names (Only SHA-1 is supported at the moment).

```kotlin
val appNamespace = UUID("my-app-uuid")
val agentId = UUID.generateUUID(appNamespace, "agentId")
```

The other alternative is to generate UUID by hashing 
bytes (similar to `java.util.UUID.nameUUIDFromBytes`).

```kotlin
val uuid = UUID.generateUUID(bytes)
```

> Note that unlike `java.util.UUID`, kotlinx's generateUUID
> doesn't support MD5, so the blind migration
> from Java to kotlinx-uuid may lead to changing UUIDs.

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
repositories {
    maven("https://dl.bintray.com/cy6ergn0m/uuid")
}

val jvmMain by getting {
    dependencies {
        implementation("org.jetbrains.kotlinx.experimental:ktor-server-uuid:0.0.1")
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

> Note that unlike `java.util.UUID.nameUUIDFromBytes`, kotlinx's generateUUID
> doesn't support MD5, so the blind migration
> from Java to kotlinx-uuid may lead to changing UUIDs.
> This is why it is marked as error, so you need to think
> first, if you can simply replace it or not.

## Using with Exposed

[Exposed](https://github.com/JetBrains/Exposed) is an ORM framework for Kotlin. 
It has support for `java.util.UUID`, but to get kotlinx-uuid supported you need 
to include the corresponding dependency and use DSL functions:

```kotlin
repositories {
    maven("https://dl.bintray.com/cy6ergn0m/uuid")
}

dependencies {
    implementation("org.jetbrains.kotlinx.experimental:exposed-uuid:0.0.1")
}
```

When declaring a table having UUID as Primary Key:
```kotlin
// SQL DSL
object MyTable : KotlinxUUIDTable() {
    // there is "id" property with the kotlinx-uuid type
}

// DAO API
class MyTableEntity(id: EntityID<UUID>): KotlinxUUIDEntity(id) {
    companion object: KotlinxUUIDEntityClass<MyTableEntity>(MyTable)
    
} 
```

To declare a regular column, use `kotlinxUUID` function:
```kotlin
object MyTable : Table() {
    val something = kotlinxUUID("SOME_COLUMN")
}
```

Unfortunately, there is a function called `uuid` in the base class, 
inside of the exposed core, this is why we can't overwrite/override it 
so it may lead to confusion. The function `uuid` only works with `java.util.UUID`:

```kotlin
object MyTable : Table() {
    val column1 = kotlinxUUID("C1") // kotlinx.uuid.UUID
    val column2 = uuid("C2") // java.util.UUID
}
```

## Using with Gson

To use [UUID] with [Gson](https://github.com/google/gson)
you need to register the serializer:

```kotlin
repositories {
    maven("https://dl.bintray.com/cy6ergn0m/uuid")
}

dependencies {
    implementation("org.jetbrains.kotlinx.experimental:gson-uuid:0.0.1")
}
```

```kotlin
val gson = GsonBuilder().registerUUID().create()
```

After that [UUID] will be serialized to JSON string primitives.

## Using with Jackson

To use [UUID] with [Jackson](https://github.com/FasterXML/jackson-databind)
you need to register the module:

```kotlin
repositories {
    maven("https://dl.bintray.com/cy6ergn0m/uuid")
}

dependencies {
    implementation("org.jetbrains.kotlinx.experimental:jackson-module-uuid:0.0.1")
}
```

```kotlin
val jackson = ObjectMapper().uuid()

val json = jackson.writeValueAsString(
    UUID.generateUUID()
)
```

After that [UUID] will be serialized to JSON string primitives.

Having jackson-module-kotlin is recommended but not required.

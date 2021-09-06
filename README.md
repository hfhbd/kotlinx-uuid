# Kotlinx-UUID

> #### This is a fork from https://github.com/cy6erGn0m/kotlinx-uuid, released under Apache 2.
> #### The main implementation was thankfully provided by [cy6erGn0m](https://github.com/cy6erGn0m)!

`kotlinx-uuid` is a multiplatform (MPP) [Kotlin](https://kotlinlang.org) library introducing support
for [UUID](https://en.wikipedia.org/wiki/Universally_unique_identifier).

The main class `UUID` is serializable out of the box, so the library depends
on [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization). If you don't need serialization, you don't
need to apply the plugin.

Supported platforms are:

- JVM (Java 8+, IR only)
- JavaScript (IR backend only)
- Native:
    - iOS

> To support the extensions on iOS, you need
> ```kotlin
> // build.gradle.kts
> ios {
>   binaries {
>     framework {
>        // Export transitively.
>        transitiveExport = true
>     }
>   }  
> }
> ```

## Including

This package is uploaded to `mavenCentral`.

````kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("app.softwork:kotlinx-uuid-core:LATEST")
}
````

## Usage

#### Creating from a UUID string

```kotlin
val uuid = UUID("1b3e4567-e99b-13d3-a476-446657420000")
val guid = UUID("{1b3e4567-e99b-13d3-a476-446657420000}")
```

#### Generating UUID4 using random

```kotlin
// using a default SecureRandom implementation
val uuid = UUID()

// use custom Kotlin Random instance
val uuid = UUID.generateUUID(yourRandom)
```

#### Generating UUID5 using hash

kotlin-uud provides the ability to generate uuids by hashing names (Only SHA-1 is supported at the moment).

```kotlin
val appNamespace = UUID("my-app-uuid")
val agentId = UUID.generateUUID(appNamespace, "agentId")
```

The other alternative is to generate UUID by hashing bytes (similar to `java.util.UUID.nameUUIDFromBytes`).

```kotlin
val uuid = UUID.generateUUID(bytes)
```

> Note that unlike `java.util.UUID`, kotlinx's generateUUID
> doesn't support MD5, so the blind migration
> from Java to kotlin-uud may lead to changing UUIDs.

#### Serializing (kotlinx.serialization)

There are two serializers for `UUID`: the default one and the binary.

The default serializer does always serialize UUIDs as string primitives.

```kotlin
Json.encodeToString(uuid) == "\"1b3e4567-e99b-13d3-a476-446657420000\""
```

The additional serializer is useful for binary formats. Because they are not human-readable, and it's possible to reduce
size.

```kotlin
val bytes = Protobuf.encodeToByteArray(BinarySerializer, uuid)
```

## Using with Exposed

[Exposed](https://github.com/JetBrains/Exposed) is an ORM framework for Kotlin. It has support for `java.util.UUID`, but
to get kotlin-uuid supported you need to include the corresponding dependency and use DSL functions:

```kotlin
dependencies {
    implementation("app.softwork:kotlinx-uuid-exposed:0.0.1")
}
```

When declaring a table having UUID as Primary Key:

```kotlin
// SQL DSL
object MyTable : KotlinxUUIDTable() {
    // there is "id" property with the kotlin-uud type
}

// DAO API
class MyTableEntity(id: EntityID<UUID>) : KotlinxUUIDEntity(id) {
    companion object : KotlinxUUIDEntityClass<MyTableEntity>(MyTable)

} 
```

To declare a regular column, use `kotlinxUUID` function:

```kotlin
object MyTable : Table() {
    val something = kotlinxUUID("SOME_COLUMN")
}
```

Unfortunately, there is a function called `uuid` in the base class, inside Exposed, this is why we can't
overwrite/override it, so it may lead to confusion. The function `uuid` only works with `java.util.UUID`:

```kotlin
object MyTable : Table() {
    val column1 = kotlinxUUID("C1") // kotlinx.uuid.UUID
    val column2 = uuid("C2") // java.util.UUID
}
```

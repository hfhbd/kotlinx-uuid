# Module kotlinx-uuid-core

This core module contains the serializable UUID class.

## Creating from a UUID string

```kotlin
val uuid = UUID("1b3e4567-e99b-13d3-a476-446657420000")
val guid = UUID("{1b3e4567-e99b-13d3-a476-446657420000}")
```

## Generating UUID4 using random

```kotlin
// using a default SecureRandom implementation
val uuid = UUID()

// use custom Kotlin Random instance
val uuid = UUID.generateUUID(yourRandom)
```

## Generating UUID5 using hash

`kotlinx-uuid` provides the ability to generate uuids by hashing names (Only SHA-1 is supported at the moment).

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

## UUID7 Draft

This library contains experimental support for UUIDv7 according to
this [draft IETF](https://datatracker.ietf.org/doc/html/draft-ietf-uuidrev-rfc4122bis).

```kotlin
val unixTimestamp = 42 // 48 bit
val uuid = UUIDv7(unixTimestamp)
uuid.unixTimeStamp // 42
```

## Serializing (kotlinx.serialization)

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

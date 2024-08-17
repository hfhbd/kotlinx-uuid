# Module kotlinx-uuid-core

This core module contains several helper methods for the [Uuid](kotlin.uuid.Uuid) class, like `timestamp`, `variant`,
`version`.

## Generating UUID5 using hash

`kotlinx-uuid` provides the ability to generate uuids by hashing names (Only SHA-1 is supported at the moment).

```kotlin
val appNamespace = Uuid.parse("my-app-uuid")
val agentId = Uuid.generateUuid(appNamespace, "agentId")
```

The other alternative is to generate Uuid by hashing bytes (similar to `java.util.UUID.nameUUIDFromBytes`).

```kotlin
val uuid = Uuid.generateUUID(bytes)
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

There is also a binary serializers for `Uuid`.
This additional serializer is useful for binary formats. Because they are not human-readable, and it's possible to
reduce
size.

```kotlin
val bytes = Protobuf.encodeToByteArray(BinarySerializer, uuid)
```

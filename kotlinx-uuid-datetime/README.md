# Module kotlinx-uuid-datetime

Provides support for UUIDv7 using kotlinx-datetime.

```kotlin
dependencies {
    implementation("app.softwork:kotlinx-uuid-datetime:LATEST")
}
```

UUIDv7 with the current timestamp using `Clock.System` can be created using:

```kotlin
val uuid = UUIDv7(random = Random)
```

When processing existing UUIDv7s, the timestamp bits can be interpreted as an Instant with millisecond precision using:

```kotlin
UUIDv7().instant
```

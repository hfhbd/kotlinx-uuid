# Module kotlinx-uuid-sqldelight

SQLDelight uses column adapters for custom types, like this UUID.
`kotlinx-uuid-sqldelight` provides two adapters, a `UUIDStringAdapter` for a `String` and a `ByteArrayAdapter` for
a `ByteArray` representation respectively.

```kotlin
dependencies {
    implementation("app.softwork:kotlinx-uuid-sqldelight:LATEST")
}
```

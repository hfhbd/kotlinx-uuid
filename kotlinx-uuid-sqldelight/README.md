# Module kotlinx-uuid-sqldelight

SQLDelight uses column adapters for custom types, like [Uuid](kotlin.uuid.Uuid).
`kotlinx-uuid-sqldelight` provides two adapters, a [UuidStringAdapter](app.softwork.uuid.sqldelight.UuidStringAdapter) for a [String] and a [UuidByteArrayAdapter](app.softwork.uuid.sqldelight.UuidByteArrayAdapter)` for
a [ByteArray] representation respectively.

```kotlin
dependencies {
    implementation("app.softwork:kotlinx-uuid-sqldelight:LATEST")
}
```

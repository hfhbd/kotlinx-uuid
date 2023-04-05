# Module kotlinx-uuid-exposed

[Exposed](https://github.com/JetBrains/Exposed) is an ORM framework for Kotlin. It has support for `java.util.UUID`, but
to get kotlin-uuid supported you need to include the corresponding dependency and use DSL functions:

```kotlin
dependencies {
    implementation("app.softwork:kotlinx-uuid-exposed:LATEST")
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

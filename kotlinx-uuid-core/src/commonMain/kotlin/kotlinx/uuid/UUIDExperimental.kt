/*
 * Copyright 2020-2020 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.uuid

/**
 * The API marked with this annotation is experimental. Its stability is not guaranteed, and you shouldn't rely on it.
 *
 * @property plannedVersion in which this API is planned to be stabilized
 */
@RequiresOptIn(
    "This UUID API is experimental and could be changed in future releases.",
    level = RequiresOptIn.Level.WARNING
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
public annotation class UUIDExperimentalAPI(val plannedVersion: String = "")

/**
 * The API marked with this annotation is internal and should be never used
 * outside this library. It's stability, behaviour and compatibility is not guaranteed
 * and could be changed in any release without notice.
 */
@RequiresOptIn(
    "This API is internal",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
public annotation class InternalAPI

package kotlinx.uuid.internal

import kotlinx.uuid.*

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public expect abstract class Parcelable(
    timeStampAndVersionRaw: Long,
    clockSequenceVariantAndNodeRaw: Long
)

public expect interface ParcelableCreator<T : Any>

internal expect val creator: ParcelableCreator<UUID>

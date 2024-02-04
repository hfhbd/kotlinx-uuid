package kotlinx.uuid.internal

import kotlinx.uuid.*

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual abstract class Parcelable actual constructor(
    timeStampAndVersionRaw: Long,
    clockSequenceVariantAndNodeRaw: Long
)

public actual interface ParcelableCreator<T: Any>

internal actual val creator: ParcelableCreator<UUID> = object: ParcelableCreator<UUID> {}

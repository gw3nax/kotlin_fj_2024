package newsAPI.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

object LocalDateAsLongSerializer : KSerializer<LocalDate> {
    override fun serialize(encoder: Encoder, value: LocalDate) {
        val epochSeconds = value.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
        encoder.encodeLong(epochSeconds)
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): LocalDate {
        val epochSeconds = decoder.decodeLong()
        return Instant.ofEpochSecond(epochSeconds).atZone(ZoneOffset.UTC).toLocalDate()
    }
}
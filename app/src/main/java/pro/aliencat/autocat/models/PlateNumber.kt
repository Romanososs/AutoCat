package pro.aliencat.autocat.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = PlateNumberSerializer::class)
data class PlateNumber(
    val number: String = ""
) {
    val isValid = number.length in 8.. 9
    val mainPart =  if (number.length >= 6) number.substring(0, 6) else number.substring(0, number.length)
    val region = if (number.length >= 7) number.substring(6, number.length) else ""

    override fun toString(): String {
        return number
    }
}


object PlateNumberSerializer : KSerializer<PlateNumber> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "pro.aliencat.autocat.models.PlateNumberSerializer",
        PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: PlateNumber) {
        encoder.encodeString(value.number)
    }

    override fun deserialize(decoder: Decoder): PlateNumber {
        return PlateNumber(decoder.decodeString())
    }
}
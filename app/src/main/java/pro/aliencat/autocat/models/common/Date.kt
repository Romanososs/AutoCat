package pro.aliencat.autocat.models.common

import android.text.format.DateUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Serializable(with = Date.DateSerializer::class)
data class Date(
    private val time: Long
) {
    companion object{
        val Empty = Date(0)
    }

    private val defaultDateFormat = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault())
    private val dateTime = Calendar.getInstance().also { it.timeInMillis = time }

    fun isToday() = DateUtils.isToday(time)

    fun isYesterday(): Boolean {
        val now: Calendar = Calendar.getInstance().also { it.add(Calendar.DATE, -1) }
        return now.get(Calendar.YEAR) == dateTime.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == dateTime.get(Calendar.MONTH) &&
                now.get(Calendar.DATE) == dateTime.get(Calendar.DATE)
    }

    fun isCurrentWeek():Boolean{
        val now: Calendar = Calendar.getInstance()
        return now.get(Calendar.YEAR) == dateTime.get(Calendar.YEAR) &&
                now.get(Calendar.WEEK_OF_MONTH) == dateTime.get(Calendar.WEEK_OF_MONTH)
        //Have edge case when 31 Dec and 1 Jan in the same week
    }

    fun isCurrentMonth():Boolean{
        val now: Calendar = Calendar.getInstance()
        return now.get(Calendar.YEAR) == dateTime.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == dateTime.get(Calendar.MONTH)
    }

    fun toHeaderString(): String {
        return if (isToday()){
            "Today"//TODO to resources
        } else if(isYesterday()){
            "Yesterday"
        } else if(isCurrentWeek()){
            SimpleDateFormat("EEEE", Locale.getDefault()).format(dateTime.time)
        } else if (isCurrentMonth()) {
            SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(dateTime.time)
        } else {
            SimpleDateFormat("LLLL yyyy", Locale.getDefault()).format(dateTime.time)
        }
    }

    override fun toString(): String {
        return defaultDateFormat.format(dateTime.time)
    }

    fun millisec(): Long = time

    operator fun minus(other: Date): Date {
        return Date(this.time - other.time)
    }

    object DateSerializer : KSerializer<Date> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
            "pro.aliencat.autocat.models.common.DateSerializer",
            PrimitiveKind.DOUBLE
        )

        override fun serialize(encoder: Encoder, value: Date) {
            encoder.encodeDouble(value.time.toDouble())
        }

        override fun deserialize(decoder: Decoder): Date {
            return Date(decoder.decodeDouble().toLong())
        }
    }
}




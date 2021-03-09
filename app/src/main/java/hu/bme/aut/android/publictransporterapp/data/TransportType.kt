package hu.bme.aut.android.publictransporterapp.data

import androidx.room.TypeConverter

enum class TransportType {
    BUS, TRAM, TROLLEY, NIGHTBUS, M1, M2, M3, M4, RAIL;

    companion object {
        @JvmStatic
        @TypeConverter
        fun getByOrdinal(ordinal: Int): TransportType? {
            var ret: TransportType? = null
            for (cat in values()) {
                if (cat.ordinal == ordinal) {
                    ret = cat
                    break
                }
            }
            return ret
        }

        @JvmStatic
        @TypeConverter
        fun toInt(hoa: TransportType): Int {
            return hoa.ordinal
        }
    }
}
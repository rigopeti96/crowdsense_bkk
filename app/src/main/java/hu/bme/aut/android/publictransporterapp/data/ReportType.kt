package hu.bme.aut.android.publictransporterapp.data

import androidx.room.TypeConverter

enum class ReportType {
    TRAFFIC, CONDUCTOR;

    companion object {
        @JvmStatic
        @TypeConverter
        fun getByOrdinal(ordinal: Int): ReportType? {
            var ret: ReportType? = null
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
        fun toInt(hoa: ReportType): Int {
            return hoa.ordinal
        }
    }
}
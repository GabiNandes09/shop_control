package com.rogue.shopcontrol.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rogue.shopcontrol.domain.model.DateRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

private val Context.homeDateRangeDataStore by preferencesDataStore(name = "shop_control_prefs")

private val KEY_START = stringPreferencesKey("home_date_range_start")
private val KEY_END = stringPreferencesKey("home_date_range_end")

class HomeDateRangeRepository(
    private val context: Context
) {

    fun observe(): Flow<DateRange?> =
        context.homeDateRangeDataStore.data.map { prefs ->

            val start = prefs[KEY_START]?.let { LocalDate.parse(it) }
            val end = prefs[KEY_END]?.let { LocalDate.parse(it) }

            if (start != null && end != null) {
                DateRange(start, end)
            } else {
                null
            }

        }


    suspend fun save(range: DateRange) {

        context.homeDateRangeDataStore.edit { prefs ->
            prefs[KEY_START] = range.start.toString()
            prefs[KEY_END] = range.end.toString()
        }

    }

}

package com.atech.core.pref

import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.atech.core.model.TagModel
import com.atech.core.utils.TAGS
import com.atech.core.utils.fromJsonList
import com.atech.core.utils.toJSON
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Keep
data class StudentPref(
    val filter: List<TagModel>,
)

@Singleton
class StudentPrefManager @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "student_pref")

    internal val studentPref = context.dataStore.data.catch { exception ->
        when (exception) {
            is IOException -> {
                emit(emptyPreferences())
                Log.e(TAGS.ERROR.name, "$exception")
            }

            else -> throw exception
        }
    }.map { preferences ->
        val filter = fromJsonList<TagModel>(preferences[PreferenceKey.DEF_FILTER] ?: "")
        StudentPref(filter)
    }

    internal suspend fun updateFilter(filter: List<TagModel>) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKey.DEF_FILTER] = toJSON(filter)
        }
    }

    private object PreferenceKey {
        val DEF_FILTER = stringPreferencesKey("def_filter")
    }
}


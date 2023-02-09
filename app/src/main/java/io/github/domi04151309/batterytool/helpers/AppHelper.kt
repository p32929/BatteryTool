package io.github.domi04151309.batterytool.helpers

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import io.github.domi04151309.batterytool.R
import org.json.JSONArray

object AppHelper {

    internal fun generatePreference(
        c: Context, packageName: String, forced: ForcedSet
    ): Preference {
        return Preference(c).let {
            it.icon = c.packageManager.getApplicationIcon(packageName)
            it.title = c.packageManager.getApplicationLabel(
                c.packageManager.getApplicationInfo(
                    packageName, PackageManager.GET_META_DATA
                )
            )
            it.summary = packageName
            if (forced.contains(packageName)) it.title =
                it.title as String + " " + c.resources.getString(R.string.main_forced)
            it
        }
    }

    internal fun generatePreference(
        c: Context, applicationInfo: ApplicationInfo
    ): Preference {
        return Preference(c).let {
            it.icon = applicationInfo.loadIcon(c.packageManager)
            it.title = applicationInfo.loadLabel(c.packageManager)
            it.summary = applicationInfo.packageName
            it
        }
    }

    internal fun hibernateFromBackground(c: Context) {
        val appArray = JSONArray(
            PreferenceManager.getDefaultSharedPreferences(c).getString(
                P.PREF_APP_LIST, P.PREF_APP_LIST_DEFAULT
            )
        )
        val commandArray: ArrayList<String> = ArrayList()

        for (i in 0 until appArray.length()) {
            try {
                val packageName = appArray.getString(i)
                commandArray.add("am force-stop $packageName")
            } catch (e: Exception) {
                continue
            }
        }

        if (commandArray.isNotEmpty()) Root.shell(commandArray.toTypedArray())
    }

    internal fun hibernateFromUi(c: Context) {
        val appArray = JSONArray(
            PreferenceManager.getDefaultSharedPreferences(c).getString(
                P.PREF_APP_LIST, P.PREF_APP_LIST_DEFAULT
            )
        )

        for (i in 0 until appArray.length()) {
            try {
                val packageName = appArray.getString(i)
                Root.shell("am force-stop $packageName")
            } catch (e: Exception) {
                continue
            }
        }
    }
}
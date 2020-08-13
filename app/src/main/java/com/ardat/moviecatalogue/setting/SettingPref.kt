package com.ardat.moviecatalogue.setting

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.provider.Settings
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ardat.moviecatalogue.BuildConfig
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.model.ResultMovieModel
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

class SettingPref : AppCompatPref() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, MainPreferenceFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class MainPreferenceFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

        private var mRequestQueue: RequestQueue? = null
        internal var mNotifList: List<ResultMovieModel>? = null
        internal var mMovieDailyReceiver = MovieDailyReceiver()
        internal var mMovieUpcomingReceiver = MovieUpcomingReceiver()
        internal var mSwitchReminder: SwitchPreference? = null
        internal var mSwitchToday: SwitchPreference? = null

        inner class GetMovieTask : AsyncTask<String, Void, Void>() {

            override fun doInBackground(vararg strings: String): Void? {
                getData(strings[0])
                return null
            }
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
            val key = preference.key
            val value = newValue as Boolean
            if (key == getString(R.string.key_today_reminder)) {
                if (value) {
                    mMovieDailyReceiver.setAlarm(activity)
                } else {
                    mMovieDailyReceiver.cancelAlarm(activity)
                }
            } else {
                if (value) {
                    mMovieUpcomingReceiver.setAlarm(activity)
                } else {
                    mMovieUpcomingReceiver.cancelAlarm(activity)
                }
            }
            return true
        }

        private fun setReleaseAlarm() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = Date()
            val today = dateFormat.format(date)
            val getDataAsync = GetMovieTask()
            getDataAsync.execute(BuildConfig.BASE_URL+BuildConfig.API_MOVIE  + BuildConfig.API_KEY+"&primary_release_date.gte=$today&primary_release_date.lte=$today")
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_main)
            mNotifList = ArrayList()
            mRequestQueue = Volley.newRequestQueue(activity)
            mSwitchReminder =
                findPreference(getString(R.string.key_today_reminder)) as SwitchPreference
            mSwitchReminder?.onPreferenceChangeListener = this
            mSwitchToday =
                findPreference(getString(R.string.key_release_reminder)) as SwitchPreference
            mSwitchToday?.onPreferenceChangeListener = this
            val myPref = findPreference(getString(R.string.key_lang))
            myPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
        }

        fun getData(url: String) {
            val request =
                JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                    try {
                        val jsonArray = response.getJSONArray("results")

                        for (i in 0 until jsonArray.length()) {
                            val data = jsonArray.getJSONObject(i)
                            mNotifList = listOf(
                                ResultMovieModel(
                                    data.getDouble("vote_average"),
                                    data.getString("poster_path"),
                                    data.getInt("id"),
                                    data.getString("title"),
                                    data.getString("overview"),
                                    data.getString("release_date"))
                            )
                        }
//                        mMovieUpcomingReceiver.setAlarm(activity)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error -> error.printStackTrace() })
            mRequestQueue!!.add(request)
        }
    }
}

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
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.model.ResultMovieModel

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale


class SettingPref : AppCompatPref() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, MainPreferenceFragment())
            .commit()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    class MainPreferenceFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

        private var mRequestQueue: RequestQueue? = null
        internal var mNotifList: MutableList<ResultMovieModel>? = null
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
                    setReleaseAlarm()
                } else {
                    mMovieUpcomingReceiver.cancelAlarm(activity)
                }
            }
            return true
        }

        private fun setReleaseAlarm() {
            val getDataAsync = MainPreferenceFragment.GetMovieTask()
            getDataAsync.execute("https://api.themoviedb.org/3/movie/upcoming?api_key=f04bce2a28b277c0c4ee02124610fef5&language=en-US")
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_main)
            mNotifList = ArrayList<ResultMovieModel>()
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
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = Date()
            val today = dateFormat.format(date)
            val request =
                JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                    try {
                        val jsonArray = response.getJSONArray("results")

                        for (i in 0 until jsonArray.length()) {
                            val data = jsonArray.getJSONObject(i)
                            val movieItem = ResultsItem()
                            movieItem.setTitle(data.getString("title"))
                            movieItem.setReleaseDate(data.getString("release_date"))
                            movieItem.setTitle(data.getString("title"))
                            movieItem.setOverview(data.getString("overview"))
                            movieItem.setPosterPath(data.getString("poster_path"))
                            if (data.getString("release_date") == today) {
                                mNotifList?.add(movieItem)
                            }
                        }
                        mMovieUpcomingReceiver.setAlarm(activity, mNotifList)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error -> error.printStackTrace() })
            mRequestQueue!!.add(request)
        }
    }
}

package com.ardat.moviecatalogue.setting

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.activity.MovieDetailActivity
import com.ardat.moviecatalogue.baserespon.DataMovieBaseRespon
import com.ardat.moviecatalogue.model.ResultMovieModel
import com.ardat.moviecatalogue.object_interface.DataInterface
import com.ardat.moviecatalogue.object_interface.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MovieUpcomingReceiver : BroadcastReceiver() {

    private var dataInterface : DataInterface? = null

    private fun sendNotification(
        context: Context,
        title: String,
        mDesc: String,
        id: Int,
        mMovieResult: ResultMovieModel
    ) {
        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        val mIntent = Intent(context, MovieDetailActivity::class.java)
        mIntent.putExtra("data", mMovieResult)

        val mPendingIntent = PendingIntent.getActivity(
            context, id, mIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(mDesc)
            .setContentIntent(mPendingIntent)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(uriTone)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "11011",
                "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.YELLOW
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            builder.setChannelId("11011")
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(id, builder.build())
    }

    fun setAlarm(context: Context) {
        var delay = 0

            cancelAlarm(context)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, MovieUpcomingReceiver::class.java)
            intent.putExtra("id", mNotifId)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                100, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis + delay,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis + delay, pendingIntent
                )
            }
            mNotifId += 1
            delay += 3000
        Toast.makeText(context, "Upcoming Notif ON", Toast.LENGTH_SHORT).show()
    }


    override fun onReceive(context: Context, intent: Intent) {
        dataInterface = RetrofitBuilder.builder(context).create(DataInterface::class.java)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val today = dateFormat.format(date)
        callDataApi(today, context)

    }

    fun callDataApi(today:String, context: Context){
        val callData = dataInterface!!.updateNotif(today, today)
        callData.enqueue(object: Callback<DataMovieBaseRespon>{
            override fun onFailure(call: Call<DataMovieBaseRespon>, t: Throwable) { }
            override fun onResponse(call: Call<DataMovieBaseRespon>, response: Response<DataMovieBaseRespon>) {
                if (response.isSuccessful){
                    val respon = response.body() as DataMovieBaseRespon
                    val dataMovie = respon.results!!
                    for (i in 0 until dataMovie.size){
                        val mMovieTitle = dataMovie.get(i).title
                        val mMovieResult = ResultMovieModel(dataMovie.get(i).vote_average, dataMovie.get(i).poster_path,dataMovie.get(i).id,dataMovie.get(i).title,dataMovie.get(i).overview,dataMovie.get(i).release_date)
                        val mDesc = context.getString(R.string.released_today) + " " + mMovieTitle
                        sendNotification(context, context.getString(R.string.app_name) + " Update", mDesc, dataMovie.get(i).id!!, mMovieResult)
                    }
                }
            }
        })

    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getPendingIntent(context))
        Toast.makeText(context, "Upcoming Notif OFF", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private var mNotifId = 2000

        private fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, MovieUpcomingReceiver::class.java)
            return PendingIntent.getBroadcast(
                context,
                1011,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
    }

}

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
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ardat.moviecatalogue.R
import com.ardat.moviecatalogue.model.ResultMovieModel

import java.util.Calendar

class MovieUpcomingReceiver : BroadcastReceiver() {

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

        val uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(mDesc)
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

    fun setAlarm(context: Context, mMovieResults: List<ResultMovieModel>) {
        var delay = 0

        for (movie in mMovieResults) {
            cancelAlarm(context)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, MovieUpcomingReceiver::class.java)
            intent.putExtra("data",movie)
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
        }
        Toast.makeText(context, "Upcoming Notif ON", Toast.LENGTH_SHORT).show()
    }


    override fun onReceive(context: Context, intent: Intent) {
        val dataMovie = intent.getParcelableArrayExtra("data") as ResultMovieModel
        val mMovieTitle = intent.getStringExtra("movietitle")
        val mMovieResult = ResultMovieModel(dataMovie.vote_average, dataMovie.poster_path,dataMovie.id,dataMovie.title,dataMovie.overview,dataMovie.release_date)
        val mDesc = context.getString(R.string.released_today) + " " + mMovieTitle
        sendNotification(context, context.getString(R.string.app_name), mDesc, dataMovie.id!!, mMovieResult)

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

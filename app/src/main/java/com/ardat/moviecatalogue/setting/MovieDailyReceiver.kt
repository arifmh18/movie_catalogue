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
import com.ardat.moviecatalogue.activity.MainActivity
import java.util.*

class MovieDailyReceiver : BroadcastReceiver() {

    private fun sendNotification(context: Context, title: String, desc: String, id: Int) {
        val mNotificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        val mIntent = Intent(context, MainActivity::class.java)

        val mPendingIntent = PendingIntent.getActivity(
            context, id, mIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val uriTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(desc)
            .setContentIntent(mPendingIntent)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(uriTone)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationChannel = NotificationChannel(
                "11001", "NOTIFICATION_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationChannel.enableLights(true)
            mNotificationChannel.lightColor = Color.YELLOW
            mNotificationChannel.enableVibration(true)
            mNotificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

            builder.setChannelId("11001")
            mNotificationManager.createNotificationChannel(mNotificationChannel)
        }
        mNotificationManager.notify(id, builder.build())

    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val mIntent = Intent(context, MovieDailyReceiver::class.java)
        return PendingIntent.getBroadcast(context, 1001, mIntent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    fun setAlarm(context: Context) {
        cancelAlarm(context)
        val mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val mCalendar = Calendar.getInstance()
        mCalendar.set(Calendar.HOUR_OF_DAY, 7)
        mCalendar.set(Calendar.MINUTE, 0)
        mCalendar.set(Calendar.SECOND, 0)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mAlarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                mCalendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                getPendingIntent(context)
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAlarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                mCalendar.timeInMillis,
                getPendingIntent(context)
            )
        }

        Toast.makeText(context, "Daily Notif ON", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarmManager.cancel(getPendingIntent(context))

        Toast.makeText(context, "Daily Notif OFF", Toast.LENGTH_SHORT).show()
    }

    override fun onReceive(context: Context, mIntent: Intent) {
        sendNotification(
            context, context.getString(R.string.released_daily),
            context.getString(R.string.released_dailydesk), 1001
        )
    }

}

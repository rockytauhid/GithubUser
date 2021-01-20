package com.dicoding.githubuser.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.githubuser.R
import com.dicoding.githubuser.activity.FavoriteActivity
import com.dicoding.githubuser.activity.MainActivity
import com.dicoding.githubuser.helper.Companion
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    private val idRepeating = 101

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(Companion.ALARM_EXTRA_MESSAGE)
        val title = context.getString(R.string.favorite)
        showAlarmNotification(context, title, message.toString())
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val title = context.getString(R.string.daily_reminder_title)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            getPendingIntent(context, context.getString(R.string.alarm_message))
        )
        showToast(context, title, context.getString(R.string.alarm_setup))
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val title = context.getString(R.string.daily_reminder_title)
        val message = context.getString(R.string.alarm_cancelled)
        val pendingIntent = getPendingIntent(context, message)
        pendingIntent?.cancel()
        alarmManager.cancel(pendingIntent)
        showToast(context, title, message)
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String
    ) {
        val channelId = "com.dicoding.githubuser.channel_1"
        val channelName = "com.dicoding.githubuser.alarm_channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(context, FavoriteActivity::class.java)
        intent.putExtra(Companion.ALARM_EXTRA_MESSAGE, message)
        val pendingIntent = TaskStackBuilder.create(context)
            .addParentStack(MainActivity::class.java)
            .addNextIntent(intent)
            .getPendingIntent(idRepeating, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_access_time_black)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(idRepeating, notification)
    }

    private fun getPendingIntent(context: Context, message: String): PendingIntent? {
        val intent =  Intent(context, AlarmReceiver::class.java)
        intent.putExtra(Companion.ALARM_EXTRA_MESSAGE, message)
        return PendingIntent.getBroadcast(
            context,
            idRepeating,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()
    }
}
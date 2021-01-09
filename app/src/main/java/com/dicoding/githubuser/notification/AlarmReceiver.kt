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
import com.dicoding.githubuser.helper.Companion
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    private val ID_REPEATING = 101

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(Companion.ALARM_EXTRA_MESSAGE)
        val title = context.getString(R.string.favorite)
        val notifId = ID_REPEATING
        showAlarmNotification(context, title, message.toString(), notifId)
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val title = context.getString(R.string.daily_reminder_title)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 5)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            getPendingIntent(context, title, context.getString(R.string.alarm_message))
        )
        showToast(context, title, context.getString(R.string.alarm_setup))
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val title = context.getString(R.string.daily_reminder_title)
        val message = context.getString(R.string.alarm_cancelled)
        val pendingIntent = getPendingIntent(context, title, message)
        pendingIntent?.cancel()
        alarmManager.cancel(pendingIntent)
        showToast(context, title, message)
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(context, FavoriteActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(intent)
            .getPendingIntent(ID_REPEATING, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_access_time_black)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
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
        notificationManagerCompat.notify(notifId, notification)
    }

    private fun getPendingIntent(context: Context, type: String, message: String): PendingIntent? {
        val intent =  Intent(context, AlarmReceiver::class.java)
        intent.putExtra(Companion.ALARM_EXTRA_MESSAGE, message)
        return PendingIntent.getBroadcast(
            context,
            ID_REPEATING,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title : $message", Toast.LENGTH_LONG).show()
    }
}
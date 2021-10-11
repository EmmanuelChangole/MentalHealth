package com.example.mentalhealthapp.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.mentalhealthapp.ItemFragment
import com.example.mentalhealthapp.MainActivity
import com.example.mentalhealthapp.R
import com.example.mentalhealthapp.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context)
{
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    contentIntent.putExtra(Constants.MOOD,true)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val iconImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.mipmap.ic_launcher
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(iconImage)
        .bigLargeIcon(null)
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS)
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    ).setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .setLargeIcon(iconImage)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        notify(NOTIFICATION_ID, builder.build())
}
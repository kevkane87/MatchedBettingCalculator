/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.kevkane87.matchedbettingcalculator.reminders

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.example.android.kevkane87.matchedbettingcalculator.Constants
import com.example.android.kevkane87.matchedbettingcalculator.MainActivity
import com.example.android.kevkane87.matchedbettingcalculator.R

//import kotlinx.coroutines.channels.consumesAll

// Notification ID.
private val NOTIFICATION_ID = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(applicationContext: Context, message: String) {
    // Create the content intent for the notification, which launches
    // this activity. Attach bet item in bundle
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val bundle = Bundle()
    bundle.putSerializable(Constants.REMINDER_ID, message)
    contentIntent.putExtra(Constants.REMINDER_ID, bundle)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.bet_reminder_channel_id)
    )
        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
        .setContentTitle(applicationContext.getString(R.string.bet_reminder))
        .setContentText(message)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}

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
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.android.kevkane87.matchedbettingcalculator.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(applicationContext: Context, message: String) {

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.bet_reminder_channel_id)
    )
        .setSmallIcon(R.drawable.icon_heading)
        .setContentTitle(applicationContext.getString(R.string.bet_reminder))
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(createID(), builder.build())
}

fun createID(): Int {
    val now = Date()
    return SimpleDateFormat("ddHHmmss", Locale.UK).format(now).toInt()
}

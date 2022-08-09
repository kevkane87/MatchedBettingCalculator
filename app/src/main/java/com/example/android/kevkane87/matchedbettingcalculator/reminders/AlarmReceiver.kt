package com.example.android.kevkane87.matchedbettingcalculator.reminders

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.android.kevkane87.matchedbettingcalculator.Constants

//broadcast receiver for receiving notification
class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(Constants.REMINDER_ID)
        val reminderDataItem = bundle?.getSerializable(Constants.REMINDER_ID) as String

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(context, reminderDataItem)
    }
}

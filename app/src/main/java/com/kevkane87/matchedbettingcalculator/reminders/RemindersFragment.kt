package com.kevkane87.matchedbettingcalculator.reminders

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kevkane87.matchedbettingcalculator.Constants
import com.kevkane87.matchedbettingcalculator.R
import com.kevkane87.matchedbettingcalculator.databinding.FragmentRemindersBinding
import java.text.SimpleDateFormat
import java.util.*

class RemindersFragment : Fragment() {

    private var _binding: FragmentRemindersBinding? = null
    private val binding get() = _binding!!

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private var dateTimeReminder = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRemindersBinding.inflate(inflater, container, false)

        val toolbarTitle = activity?.findViewById(R.id.toolbar_title) as TextView
        toolbarTitle.text = getString(R.string.bet_reminder)

        createChannel(
            getString((R.string.bet_reminder_channel_id)),
            getString(R.string.bet_reminder_channel_name)
        )

        _binding!!.reminderSetButton.setOnClickListener {

            setDateDialog(_binding!!.reminderText.text.toString())
        }

        return binding.root
    }



    private fun setAlarm(message: String) {

        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //create intent and attach bet details for reminder
        val intent = Intent(context, AlarmReceiver::class.java)
        val bundle = Bundle()
        bundle.putSerializable(Constants.REMINDER_ID, message)
        intent.putExtra(Constants.REMINDER_ID, bundle)

        alarmIntent = intent.let { intent ->
            PendingIntent.getBroadcast(context, createID(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        alarmMgr?.set(
            AlarmManager.RTC_WAKEUP,
            dateTimeReminder.timeInMillis,
            alarmIntent
        )
        Toast.makeText(context, getString(R.string.reminder_is_set), Toast.LENGTH_SHORT).show()

    }

    private fun setDateDialog(message: String) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireContext(),
            R.style.DialogTheme,
            { view, year, monthOfYear, dayOfMonth ->

                dateTimeReminder.set(Calendar.YEAR, year)
                dateTimeReminder.set(Calendar.MONTH, monthOfYear)
                dateTimeReminder.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                setTimeDialog(message)

            },
            year,
            month,
            day
        )
        dpd.show()

    }

    //get reminder time from user using dialog
    private fun setTimeDialog(message: String) {

        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            dateTimeReminder.set(Calendar.HOUR_OF_DAY, hour)
            dateTimeReminder.set(Calendar.MINUTE, minute)
            setAlarm(message)
        }
        val tpd = TimePickerDialog(
            requireContext(), R.style.DialogTheme, timeSetListener, cal.get(
                Calendar.HOUR_OF_DAY
            ), cal.get(Calendar.MINUTE), true
        )
        tpd.show()

    }



    //function to create notification channel using NotificationManager
    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.bet_reminder_channel_name)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.UK).format(now).toInt()
    }

}
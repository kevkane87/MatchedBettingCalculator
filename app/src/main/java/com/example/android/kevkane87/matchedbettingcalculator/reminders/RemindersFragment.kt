package com.example.android.kevkane87.matchedbettingcalculator.reminders

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.kevkane87.matchedbettingcalculator.Constants
import com.example.android.kevkane87.matchedbettingcalculator.R
import com.example.android.kevkane87.matchedbettingcalculator.databinding.FragmentRemindersBinding
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


        createChannel(
            getString((R.string.bet_reminder_channel_id)),
            getString(R.string.bet_reminder_channel_name)
        )

        _binding!!.reminderSetButton.setOnClickListener {

            setDateDialog(_binding!!.reminderText.toString())
        }


        return binding.root

    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarm(message: String) {

        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //create intent and attach bet details for reminder
        val intent = Intent(context, AlarmReceiver::class.java)
        val bundle = Bundle()
        bundle.putSerializable(Constants.REMINDER_ID, message)
        intent.putExtra(Constants.REMINDER_ID, bundle)
        alarmIntent = intent.let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        alarmMgr?.set(
            AlarmManager.RTC_WAKEUP,
            dateTimeReminder.timeInMillis,
            alarmIntent
        )
        Toast.makeText(context, "Reminder is set", Toast.LENGTH_SHORT).show()
    }

    //get reminder date from user using dialog
    @SuppressLint("ResourceAsColor")
    private fun setDateDialog(message: String) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireContext(),
            R.style.TimePickerTheme,
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
        /*dpd.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green_primary))
        dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green_primary))*/

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
            requireContext(), R.style.TimePickerTheme, timeSetListener, cal.get(
                Calendar.HOUR_OF_DAY
            ), cal.get(Calendar.MINUTE), true
        )
        tpd.show()
        /*tpd.getButton(TimePickerDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green_primary))
        tpd.getButton(TimePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green_primary))*/
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



}
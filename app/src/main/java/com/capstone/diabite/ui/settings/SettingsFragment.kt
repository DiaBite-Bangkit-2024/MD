package com.capstone.diabite.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentSettingsBinding
import com.capstone.diabite.db.local.HistoryViewModel
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.ui.settings.profile.ProfileActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val loginVM by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(
            requireContext()
        )
    }
    private val historyVM: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.logoutLayout.setOnClickListener {
            loginVM.logout()
            historyVM.deleteAllHistory()
        }

        binding.personalLayout.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }
        val pref = SettingsPreferences.getInstance(requireContext().dataStore)
        val settingViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref))[SettingsViewModel::class.java]

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchDarkTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchDarkTheme.isChecked = false
            }
        }

        binding.switchDarkTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        settingViewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderEnabled: Boolean ->
            binding.switchReminder.isChecked = isReminderEnabled
        }

        binding.switchReminder.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                scheduleMonthlyReminder()
                settingViewModel.saveReminderSetting(true)

                NotificationUtil.showNotification(
                    context = requireContext(),
                    channelId = "MonthlyReminderChannel",
                    channelName = "Monthly Reminder",
                    title = "Reminder Enabled",
                    text = "You have enabled monthly reminders.",
                    smallIcon = R.drawable.diabite_iconfix,
                    pendingIntent = null,
                    notificationId = 2
                )
            } else {
                cancelMonthlyReminder()
                settingViewModel.saveReminderSetting(false)
            }
        }


        binding.logoutBtn.setOnClickListener {
            loginVM.logout()
            historyVM.deleteAllHistory()
        }

        return root
    }


    private fun scheduleMonthlyReminder() {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(30, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "MonthlyReminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    private fun cancelMonthlyReminder() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork("MonthlyReminder")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

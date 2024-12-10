package com.capstone.diabite.ui.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
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

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                scheduleMonthlyReminder()
            } else {
                showPermissionDeniedMessage()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val personalInfoTextView: ImageView = binding.root.findViewById(R.id.iv_personal)
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
                checkNotificationPermissionAndScheduleReminder()
                settingViewModel.saveReminderSetting(true)
            } else {
                cancelMonthlyReminder()
                settingViewModel.saveReminderSetting(false)
            }
        }

        binding.logoutBtn.setOnClickListener {
            loginVM.logout()
            historyVM.deleteAllHistory()
        }

        binding.logoutLayout.setOnClickListener {
            loginVM.logout()
            historyVM.deleteAllHistory()
        }

        return root
    }

    private fun checkNotificationPermissionAndScheduleReminder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                scheduleMonthlyReminder()
            } else {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            scheduleMonthlyReminder()
        }
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

    private fun showPermissionDeniedMessage() {
        Toast.makeText(context,"Notification permission is required to set reminders.",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

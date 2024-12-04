package com.capstone.diabite.ui.dashboard

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.diabite.R
import com.capstone.diabite.databinding.FragmentDashboardBinding
import com.capstone.diabite.db.ApiClient
import com.capstone.diabite.db.DataResult
import com.capstone.diabite.db.local.HistoryDatabase
import com.capstone.diabite.ui.articles.ArticlesAdapter
import com.capstone.diabite.ui.articles.ArticlesRepo
import com.capstone.diabite.ui.articles.ArticlesVMFactory
import com.capstone.diabite.ui.articles.ArticlesViewModel
import com.capstone.diabite.ui.login.LoginViewModel
import com.capstone.diabite.view.AnalyzeActivity
import com.capstone.diabite.view.HistoryActivity
import com.capstone.diabite.view.food.RecomActivity
import com.capstone.diabite.view.auth.AuthViewModelFactory
import com.capstone.diabite.view.chatbot.ChatbotActivity
import com.capstone.diabite.view.DiabiteAppWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    private val binding get() = _binding!!
    private val loginVM by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(
            requireContext()
        )
    }
    private lateinit var viewModel: ArticlesViewModel
    private val profileVM: DashboardViewModel by viewModels()
    private val articlesAdapter = ArticlesAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val repository = ArticlesRepo(ApiClient.getApiService())
        viewModel =
            ViewModelProvider(this, ArticlesVMFactory(repository))[ArticlesViewModel::class.java]

        setupRecyclerView()
        setupUserProf()
        observeNewsData()
        fetchNews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginVM.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                profileVM.fetchUserProfile(user.token)
                refreshWidget()
            }
        }

        binding.apply {
            logoutBtn.setOnClickListener {
                loginVM.logout()
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = HistoryDatabase.getDatabase(requireContext()).historyDao()
                    dao.deleteAllHistory()

                    withContext(Dispatchers.Main) {
                        refreshWidget()
                    }
                }
            }

            val greetingMessage = getGreetingMessage()

            dbGreeting.text = greetingMessage

            val progress = 20
            circularProgressView.setProgress(progress)
            progressText.text = "$progress%"

            btnAnalyze.setOnClickListener {
                val intent = Intent(context, AnalyzeActivity::class.java)
                startActivity(intent)
            }
            btnRec.setOnClickListener {
                val intent = Intent(context, RecomActivity::class.java)
                startActivity(intent)
            }
            btnHistory.setOnClickListener {
                val intent = Intent(context, HistoryActivity::class.java)
                startActivity(intent)
            }

            gemini.setOnClickListener {
                val intent = Intent(context, ChatbotActivity::class.java)
                startActivity(intent)
            }

//            val sharedViewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
//            sharedViewModel.name.observe(viewLifecycleOwner) { name ->
//                dbName.text = getString(R.string.db_name, name)
//            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvDashboard.apply {
            adapter = articlesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeNewsData() {
        viewModel.newsData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is DataResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    articlesAdapter.submitList(result.data.take(10))
                }

                is DataResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchNews() {
        val query = "diabetes"
        viewModel.fetchNews(query)
    }


    private fun setupUserProf() {
        profileVM.userProfile.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataResult.Loading -> {}

                is DataResult.Success -> {
                    val data = result.data.profile
                    binding.apply {
                        dbName.text = data.name
                        vAge.text = getString(R.string.v_age, data.age.toString())
                        vHeight.text = getString(R.string.v_height, data.height.toString())
                        vWeight.text = getString(R.string.v_weight, data.weight.toString())
                        vBP.text = "${data.systolic}/${data.diastolic} mmhg"
                    }
                }

                is DataResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch profile: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun getGreetingMessage(): String {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..11 -> getString(R.string.good_morning)
            in 12..17 -> getString(R.string.good_afternoon)
            in 18..22 -> getString(R.string.good_evening)
            else -> getString(R.string.good_night)
        }
    }

    private fun refreshWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(requireContext())
        val componentName = ComponentName(requireContext(), DiabiteAppWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.layout.diabite_app_widget)

        val intent = Intent(requireContext(), DiabiteAppWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }
        requireContext().sendBroadcast(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
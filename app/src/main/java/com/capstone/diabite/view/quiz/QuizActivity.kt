package com.capstone.diabite.view.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.diabite.databinding.ActivityQuizBinding
import com.capstone.diabite.db.prediction.PredictionViewModel
import com.capstone.diabite.db.responses.TriviaItem
import com.capstone.diabite.view.MainActivity

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()
    private val predictionVM: PredictionViewModel by viewModels()
    private lateinit var triviaList: List<TriviaItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        predictionVM.quizData.observe(this) {
            binding.apply {
                backButton.setOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }

                triviaList = it.trivia.map { TriviaItem(it.answer, it.question) }

                var currentIndex = 0

                fun updateQuestion() {
                    tvQuiz.text = triviaList[currentIndex].question

                    if (currentIndex == triviaList.size - 1) {
                        btnNext.visibility = View.VISIBLE
                    } else {
                        btnNext.visibility = View.GONE
                    }
                }
                updateQuestion()

                btnTrue.setOnClickListener {
                    val isCorrect = triviaList[currentIndex].answer
                    if (isCorrect) {
                        Toast.makeText(this@QuizActivity, "Correct!", Toast.LENGTH_SHORT).show()
                        if (currentIndex < triviaList.size - 1) currentIndex++; updateQuestion()
                    } else {
                        Toast.makeText(this@QuizActivity, "Wrong Answer!", Toast.LENGTH_SHORT).show()
                    }
                }

                btnFalse.setOnClickListener {
                    val isCorrect = triviaList[currentIndex].answer
                    if (!isCorrect) {
                        Toast.makeText(this@QuizActivity, "Correct!", Toast.LENGTH_SHORT).show()
                        if (currentIndex < triviaList.size - 1) currentIndex++; updateQuestion()
                    } else {
                        Toast.makeText(this@QuizActivity, "Wrong Answer!", Toast.LENGTH_SHORT).show()
                    }
                }

                btnNext.setOnClickListener {
                    viewModel.incrementStreak()
                    val intent = Intent(this@QuizActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }

        predictionVM.errorMessage.observe(this@QuizActivity) { error ->
            Toast.makeText(this@QuizActivity, error, Toast.LENGTH_SHORT).show()
        }

        predictionVM.isLoading.observe(this@QuizActivity) {
            showLoading(it)
        }

        predictionVM.fetchTrivia()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            tvQuiz.visibility = if (isLoading) View.GONE else View.VISIBLE
            cardOtp.visibility = if (isLoading) View.GONE else View.VISIBLE
            btnNext.visibility = if (isLoading) View.GONE else View.VISIBLE
            quizTitle.visibility = if (isLoading) View.GONE else View.VISIBLE
            btnFalse.visibility = if (isLoading) View.GONE else View.VISIBLE
            btnTrue.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

    }
}
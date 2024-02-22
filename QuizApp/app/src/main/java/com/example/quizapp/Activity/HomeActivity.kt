package com.example.quizapp.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quizapp.Model.Question
import com.example.quizapp.R
import com.example.quizapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding
    private var questionList = mutableListOf<Question>()
    private var currentPosition: Int = 1
    private var selectOptionPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareQuestions()

        setQuestion()
        
    }

    private fun setQuestion() {

        var question = questionList!![currentPosition - 1]

        defaultOptionView()

        if (currentPosition == questionList!!.size) {
            binding.btnSubmit.text = "Finish"
        } else {
            binding.btnSubmit.text = "Submit"
        }

        binding.progressBar.progress = currentPosition
        binding.tvProgress.text = "$currentPosition" + "/" + binding.progressBar.max

        binding.tvQuestion.text = question.question
        binding.tvOptionOne.text = question.option1
        binding.tvOptionTwo.text = question.option2
        binding.tvOptionThree.text = question.option3
        binding.tvOptionFour.text = question.option4

    }

    private fun defaultOptionView() {
        var options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_option_one -> {
                selectOptionView(binding.tvOptionOne, 1)
            }

            R.id.tv_option_two -> {
                selectOptionView(binding.tvOptionTwo, 2)

            }

            R.id.tv_option_three -> {
                selectOptionView(binding.tvOptionThree, 3)
            }

            R.id.tv_option_four -> {
                selectOptionView(binding.tvOptionFour, 4)
            }

            R.id.btn_submit -> {
                if (selectOptionPosition == 0) {
                    currentPosition++

                    when {
                        currentPosition <= questionList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            Toast.makeText(
                                this,
                                "You have successfully completed Quiz.",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this, DetailActivity::class.java))
                        }
                    }
                } else {

                    var question = questionList?.get(currentPosition - 1)

                    if (question!!.correctAnswer != selectOptionPosition) {
                        answerView(selectOptionPosition, R.drawable.wrong_option_border_bg)
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    if (currentPosition == questionList!!.size) {
                        binding.btnSubmit.text = "Finish"
                    } else {
                        binding.btnSubmit.text = "Go to next question"
                    }
                    selectOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(correctAnswer: Int, drawable: Int) {
        when (correctAnswer) {
            1 -> {
                binding.tvOptionOne.background = ContextCompat.getDrawable(
                    this, drawable
                )
            }

            2 -> {
                binding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this, drawable
                )
            }

            3 -> {
                binding.tvOptionThree.background = ContextCompat.getDrawable(
                    this, drawable
                )
            }

            4 -> {
                binding.tvOptionFour.background = ContextCompat.getDrawable(
                    this, drawable
                )
            }
        }
    }

    private fun selectOptionView(tv: TextView, selectOptionNum: Int) {
        defaultOptionView()
        selectOptionPosition = selectOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    private fun prepareQuestions() {
        questionList.add(
            Question(
                1,
                "1) Android is -",
                "A. an operating system",
                "B. a web browser",
                "C. a web server",
                "D. None of the above ",
                1
            )
        )
        questionList.add(
            Question(
                2,
                "2) Under which of the following Android is licensed?",
                "A. OSS",
                "B. Sourceforge",
                "C. Apache/MIT",
                "D. None of the above",
                3
            )
        )
        questionList.add(
            Question(
                3,
                "3) For which of the following Android is mainly developed ?",
                "A. Servers",
                "B. Desktops",
                "C. Laptops",
                "D. Mobile devices ",
                4
            )
        )
        questionList.add(
            Question(
                4,
                "4) Which of the following is the first mobile phone released that ran the Android OS?",
                "A. HTC Hero",
                "B. Google gPhone",
                "C. T - Mobile G1",
                "D. None of the above ",
                3
            )
        )
        questionList.add(
            Question(
                5,
                "5) Which of the following virtual machine is used by the Android operating system?",
                "A. JVM",
                "B. Dalvik Virtual Machine",
                "C. Simple Virtual Machine",
                "D. None of the above ",
                2
            )
        )
        questionList.add(
            Question(
                6,
                "6) Android is based on which of the following language?",
                "A. JAVA / KOTLIN",
                "B. C++",
                "C. C",
                "D. None of the above ",
                1
            )
        )
        questionList.add(
            Question(
                7,
                "7) APK stands for -",
                "A. Android Phone Kit",
                "B. Android Page Kit",
                "C. Android Package Kit",
                "D. None of the above ",
                3
            )
        )
        questionList.add(
            Question(
                8,
                "8) What does API stand for?",
                "A. Application Programming Interface",
                "B. Android Programming Interface",
                "C. Android Page Interface",
                "D. Application Page Interface",
                1
            )
        )
        questionList.add(
            Question(
                9,
                "9) Which of the following converts Java byte code into Dalvik byte code?",
                "A. Dalvik converter",
                "B. Dex compiler",
                "C. Mobile interpretive compiler (MIC)",
                "D. None of the above ",
                2
            )
        )
        questionList.add(
            Question(
                10,
                "10) How can we stop the services in android?",
                "A. By using the stopSelf() and stopService() method",
                "B. By using the finish() method",
                "C. By using system.exit() method",
                "D. None of the above ",
                2
            )
        )
    }


}
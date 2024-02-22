package com.example.mycalculator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.mycalculator.databinding.ActivityMainBinding
import org.mariuszgromada.math.mxparser.Expression
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClear.setOnClickListener {
            binding.input.text = " "
            binding.output.text = " "
        }

        binding.btnBracketLeft.setOnClickListener {
            binding.input.text = addToInputText("(")
        }

        binding.btnBracketRight.setOnClickListener {
            binding.input.text = addToInputText(")")
        }

        binding.btnAc.setOnClickListener {
            var removeLast = binding.input.text.toString().dropLast(1)
            binding.input.text = removeLast
        }

        binding.button0.setOnClickListener {
            binding.input.text = addToInputText("0")
        }

        binding.button1.setOnClickListener {
            binding.input.text = addToInputText("1")
        }

        binding.button2.setOnClickListener {
            binding.input.text = addToInputText("2")
        }

        binding.button3.setOnClickListener {
            binding.input.text = addToInputText("3")
        }

        binding.button4.setOnClickListener {
            binding.input.text = addToInputText("4")
        }

        binding.button5.setOnClickListener {
            binding.input.text = addToInputText("5")
        }

        binding.button6.setOnClickListener {
            binding.input.text = addToInputText("6")
        }

        binding.button7.setOnClickListener {
            binding.input.text = addToInputText("7")
        }

        binding.button8.setOnClickListener {
            binding.input.text = addToInputText("8")
        }

        binding.button9.setOnClickListener {
            binding.input.text = addToInputText("9")
        }

        binding.btnPlus.setOnClickListener {
            binding.input.text = addToInputText("+")
        }

        binding.btnMinus.setOnClickListener {
            binding.input.text = addToInputText("-")
        }

        binding.btnDiv.setOnClickListener {
            binding.input.text = addToInputText("÷")
        }

        binding.btnMulti.setOnClickListener {
            binding.input.text = addToInputText("×")
        }

        binding.btnDot.setOnClickListener {
            binding.input.text = addToInputText(".")
        }

        binding.btnEqual.setOnClickListener {
            showResult()
        }

    }

    private fun getInputExpression(): String {
        var expression = binding.input.text.replace(Regex("÷"), "/")
        expression = expression.replace(Regex("×"), "*")
        return expression
    }


    private fun showResult() {
        try {
            var expression = getInputExpression()
            var result = Expression(expression).calculate()
            if (result.isNaN()) {
                // Show Error Message
                binding.output.text = ""
                binding.output.setTextColor(ContextCompat.getColor(this, R.color.red))
            } else {
                // Show Result
                binding.output.text = DecimalFormat("0.######").format(result).toString()
                binding.output.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
        } catch (e: Exception) {
            binding.output.text = ""
            binding.output.setTextColor(ContextCompat.getColor(this, R.color.red))
        }
    }

    private fun addToInputText(buttonValue: String): String {
        return binding.input.text.toString() + "" + buttonValue
    }
}
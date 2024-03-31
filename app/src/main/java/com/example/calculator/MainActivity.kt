package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.util.Stack

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private lateinit var expression : Expression
    var  lastNumeric = false
    var  stateError  = false
    var lastDot = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun onDigitClick(view: View) {
        if(stateError){
            binding.tvData.text = (view as Button).text
            stateError = false
        }else{
            binding.tvData.append((view as Button).text)
        }
        lastNumeric = true
        equal()
    }

    fun onEqualClick(view: View) {
        equal()
        // Update tvData with the input expression
        binding.tvData.text = binding.tvResult.text.toString()
        // Update tvResult with the calculated result
        binding.tvResult.text = binding.tvResult.text.toString()
    }

    fun onOperatorClick(view: View) {
        if(!stateError && lastNumeric) {
            binding.tvData.append((view as Button).text)
            lastNumeric = false
            lastDot = false
            equal()
        }
    }
    fun onBackClick(view: View) {
        binding.tvData.text = binding.tvData.text.toString().dropLast(1)
        try {
            val lastChar = binding.tvData.text.toString().last()
            if(lastChar.isDigit()) {
                equal()
            }
        }catch (e : Exception){
            binding.tvResult.text = ""
            binding.tvResult.visibility = View.GONE
            Log.e("Last char error",e.toString())
        }

    }

    fun onAllClearClick(view: View) {
        binding.tvData.text = ""
        binding.tvResult.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.tvResult.visibility = View.GONE
    }
    fun equal (){
        if(lastNumeric && !stateError){
            val text = binding.tvData.text.toString()
            expression = ExpressionBuilder(text).build()
            try{
                val result = expression.evaluate()
                binding.tvResult.visibility = View.VISIBLE
                binding.tvResult.text = "=" + result.toString()
            }
            catch (ex : ArithmeticException){
                Log.e("ArithmeticException",ex.toString())
                binding.tvResult.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}
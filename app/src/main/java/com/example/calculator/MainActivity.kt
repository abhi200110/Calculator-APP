import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.calculator.R

class MainActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private lateinit var solutionTextView: TextView
    private var operand1 = ""
    private var operand2 = ""
    private var hasDecimal = false // Flag to track decimal usage
    private var operator = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        resultTextView = findViewById(R.id.result_tv)
        solutionTextView = findViewById(R.id.solution_tv)

        // Set click listeners for all buttons
        val buttonIds = arrayOf(
            R.id.button_0,
            R.id.button_1,
            R.id.button_2,
            R.id.button_3,
            R.id.button_4,
            R.id.button_5,
            R.id.button_6,
            R.id.button_7,
            R.id.button_8,
            R.id.button_9,
            R.id.button_plus,
            R.id.button_minus,
            R.id.button_multiply,
            R.id.button_divide,
            R.id.button_percentage,
            R.id.button_All_clear,
            R.id.button_equal,
            R.id.button_decimal,
            R.id.button,
            R.id.button_history
        )

        buttonIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                onButtonClick(it as Button)
            }
        }
    }

    private fun onButtonClick(button: Button) {
        when (button.id) {
            R.id.button -> clear()
            R.id.button_All_clear -> clearAll()
            R.id.button_equal -> calculate()
            R.id.button_plus, R.id.button_minus, R.id.button_multiply, R.id.button_divide, R.id.button_percentage -> {
                handleOperatorButtonClick(button.text.toString())
            }

            R.id.button_decimal -> handleDecimalButtonClick()
            // Additional functionalities can be added here
            else -> handleDigitButtonClick(button.text.toString())
        }
    }

    private fun handleOperatorButtonClick(operator: String) {
        if (operand1.isNotEmpty() && this.operator.isEmpty()) {
            this.operator = operator
            solutionTextView.text = "$operand1 $operator "
        } else {
            showToast("Enter first operand first")
        }
    }

    private fun handleDecimalButtonClick() {
        if (!hasDecimal && (operand1.isEmpty() || !operand1.contains("."))) {
            operand1 += "."
            solutionTextView.append(".")
            hasDecimal = true
        } else if (!hasDecimal && !operand2.isEmpty() && !operand2.contains(".")) {
            operand2 += "."
            solutionTextView.append(".")
            hasDecimal = true
        } else {
            showToast("Decimal already used")
        }
    }

    private fun handleDigitButtonClick(digit: String) {
        if (operator.isEmpty()) {
            operand1 += digit
            solutionTextView.append(digit)
        } else {
            operand2 += digit
            solutionTextView.append(digit)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clear() {
        if (operator.isEmpty()) {
            if (operand1.isNotEmpty()) {
                operand1 = operand1.substring(0, operand1.length - 1)
                solutionTextView.text = operand1
            }
        } else {
            if (operand2.isNotEmpty()) {
                operand2 = operand2.substring(0, operand2.length - 1)
                solutionTextView.text =
                    solutionTextView.text.substring(0, solutionTextView.text.length - 1)
            } else {
                operator = ""
                solutionTextView.text = operand1
            }
        }
    }

    private fun clearAll() {
        operand1 = ""
        operand2 = ""
        operator = ""
        resultTextView.text = ""
        solutionTextView.text = ""
        hasDecimal = false
    }

    private fun calculate() {
        if (operand1.isNotEmpty() && operand2.isNotEmpty()) { // Condition to check if operands are present
            val num1 = operand1.toDoubleOrNull() ?: 0.0  // Safe conversion to Double
            val num2 = operand2.toDoubleOrNull() ?: 0.0

            val result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                "%" -> num1 * (num2 / 100)
                else -> 0.0
            }

            // Display the result (assuming you have a TextView named resultTextView)
            resultTextView.text = String.format("%.2f", result)
        } else {
            // Handle the case where operands are missing (e.g., show an error message)
            showToast("Please enter both operands")
        }
    }
}
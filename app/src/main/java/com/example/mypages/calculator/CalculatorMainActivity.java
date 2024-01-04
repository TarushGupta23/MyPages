package com.example.mypages.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mypages.R;
import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class CalculatorMainActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialButton btnC, btnBracketOpen, btnBracketClose, btnDivide;
    MaterialButton btn7, btn8, btn9, btnMultiply;
    MaterialButton btn4, btn5, btn6, btnMinus;
    MaterialButton btn1, btn2, btn3,btnAdd;
    MaterialButton btnAC, btn0, btnDot, btnEqual;

    TextView result, solution;

    int functionalBracket = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_main);

        result = findViewById(R.id.calculator_result);
        solution = findViewById(R.id.calculator_solution);

        assignId(btnC, R.id.calculator_C);
        assignId(btnAC, R.id.calculator_AC);

        assignId(btnBracketClose, R.id.calculator_bracketClose);
        assignId(btnBracketOpen, R.id.calculator_bracketOpen);

        assignId(btn9, R.id.calculator_9);
        assignId(btn8, R.id.calculator_8);
        assignId(btn7, R.id.calculator_7);
        assignId(btn6, R.id.calculator_6);
        assignId(btn5, R.id.calculator_5);
        assignId(btn4, R.id.calculator_4);
        assignId(btn3, R.id.calculator_3);
        assignId(btn2, R.id.calculator_2);
        assignId(btn1, R.id.calculator_1);
        assignId(btn0, R.id.calculator_0);

        assignId(btnDivide, R.id.calculator_divide);
        assignId(btnMultiply, R.id.calculator_multply);
        assignId(btnAdd, R.id.calculator_add);
        assignId(btnMinus, R.id.calculator_minus);
        assignId(btnEqual, R.id.calculator_equals);
        assignId(btnDot, R.id.calculator_dot);
    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        } catch (Exception e) {
            return "ERR";
        }
    }

    @Override
    public void onClick(View view) {
        MaterialButton btn = (MaterialButton) view;
        String btnTxt = btn.getText().toString();
        String dataToCalculate = solution.getText().toString();
        if (btnTxt.equals("AC")) {
            solution.setText("");
            result.setText("0");
            return;
        } else if (btnTxt.equals("=")) {
            solution.setText(result.getText().toString());
            return;
        } else if (btnTxt.equals("C")) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
        } else {
            dataToCalculate += btnTxt;
        }
        solution.setText(dataToCalculate);
        String finalResult = getResult(dataToCalculate);

        if (!finalResult.equals("ERR")) {
            result.setText(finalResult);
        }
    }
}
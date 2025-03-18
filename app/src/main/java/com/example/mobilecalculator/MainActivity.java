package com.example.mobilecalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public TextView formulaView;
    public TextView resultPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        formulaView = findViewById(R.id.formulaView);
        resultPreview = findViewById(R.id.resultPreview);
    }


    static public int getLastNumStartIndex(String formula) {
        for (int i = formula.length() - 1; i >= 0; --i) {
            if (formula.charAt(i) == '*' || formula.charAt(i) == '/' || formula.charAt(i) == '(' || formula.charAt(i) == ')' || formula.charAt(i) == '%') {
                return i + 1;
            }
            if (i == 0)
                return 0;
            if (formula.charAt(i) == '+' || formula.charAt(i) == '-' ) {
                return i;
            }
        }
        return -1;
    }
    static public String getLastNum(String formula) {
        int index = getLastNumStartIndex(formula);
        return index >= 0 ? formula.substring(index) : null;
    }

    public void onClick_NumInput(View v) {
        Button b = (Button)v;
        var num = b.getText().charAt(0);
        var formula = formulaView.getText().toString();

        if (formula.isEmpty()) {
            formula += num;
        }
        else if (formula.endsWith("%") || formula.endsWith(")")){
            formula += "*" + num;
        } else {
            String lastNum = getLastNum(formula);
            if (formula.endsWith("0") && Objects.equals(lastNum, "0")) {
                formula = formula.substring(0, formula.length() - 1) + num;
            }
            else {
                formula += num;
            }
        }

        formulaView.setText(formula);
    }

    public void onClick_PercentInput(View v) {
        var formula = formulaView.getText().toString();

        if (formula.isEmpty()) {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }
        else {
            char lastChar = formula.charAt(formula.length() - 1);
            if (isOperationSymbol(lastChar) || formula.endsWith("(") || formula.endsWith("%")) {
                Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
            }
            else {
                formula += '%';
            }
        }

        formulaView.setText(formula);
    }

    public boolean isOperationSymbol(char c) {
        return (c == '/' || c == '*' || c == '-' || c == '+');
    }

    public void onClick_MulDivInput(View v) {
        Button b = (Button)v;
        char sign = b.getText().charAt(0);
        var formula = formulaView.getText().toString();

        if (!formula.isEmpty()) {
            char lastChar = formula.charAt(formula.length() - 1);
            if (isOperationSymbol(lastChar)) {
                if (lastChar == '(' || lastChar == sign) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
                }
                else {
                    formula = formula.substring(0, formula.length() - 1) + sign;
                }
            }
            else {
                formula += sign;
            }
        } else {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }

        formulaView.setText(formula);
    }

    public void onClick_AddSubInput(View v) {
        Button b = (Button)v;

        char sign = b.getText().charAt(0);
        var formula = formulaView.getText().toString();

        if (!formula.isEmpty()) {
            char lastChar = formula.charAt(formula.length() - 1);
            if (isOperationSymbol(lastChar)) {
                if (lastChar == sign) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
                }
                else {
                    formula = formula.substring(0, formula.length() - 1) + sign;
                }
            }
            else {
                formula += sign;
            }
        } else {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }

        formulaView.setText(formula);
    }

    public boolean isNumber(char c) {
        return (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6'
                || c == '7' || c == '8' || c == '9');
    }

    public void onClick_decPointInput(View v) {
        var formula = formulaView.getText().toString();

        if (formula.isEmpty()) {
            formula = "0.";
        } else {
            char lastChar = formula.charAt(formula.length() - 1);
            if (lastChar == '.') {
                Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
            }
            else if (isNumber(lastChar)) {
                boolean onlyOneDot = true;
                for (int i = formula.length() - 1; i >= 0; --i) {
                    if (formula.charAt(i) == '.') {
                        onlyOneDot = false;
                        break;
                    }
                    else if (!isNumber(formula.charAt(i))) {
                        break;
                    }
                }
                if (onlyOneDot)
                    formula += ".";
            }
            else if (lastChar == '%' || lastChar == ')') {
                formula += "*0.";
            }
            else /* if (isOperationSymbol(lastChar)) */ {
                formula += "0.";
            }
        }

        formulaView.setText(formula);
    }

    public void onClick_inputZero(View v) {
        String formula = formulaView.getText().toString();

        if (formula.isEmpty()) {
            formulaView.setText("0");
            return;
        }

        char lastCharacter = formula.charAt(formula.length() - 1);
        if (lastCharacter == '%' || lastCharacter == ')'){
            formula += "*0";
        } else {
            boolean isFraction = false;
            boolean isSingleZero = false;
            if (lastCharacter == '0') {
                for (int i = formula.length() - 1; i >= 0; --i) {
                    if (formula.charAt(i) == '.') {
                        isFraction = true;
                        break;
                    }
                    else if (!isNumber(formula.charAt(i))) {
                        break;
                    }
                    else if (isNumber(formula.charAt(i)) && formula.charAt(i) != '0') {
                        isSingleZero = true;
                    }
                }
                if (isFraction || isSingleZero) {
                    formula += '0';
                }
                else {
                    Toast.makeText(this, "Not a fraction", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                formula += '0';
            }
        }

        formulaView.setText(formula);
    }

    public void onClick_SignFlip(View v) {
        var formula = formulaView.getText().toString();

        if (formula.isEmpty()) {
            formula = "-";
            formulaView.setText(formula);
            return;
        }

        char lastCharacter = formula.charAt(formula.length() - 1);

        if (lastCharacter == '/' || lastCharacter == '*' || lastCharacter == '+' || lastCharacter == '(') {
            formula += "-";
        }
        else if (lastCharacter == '-') {
            int penultimateIndex = formula.length() - 2;
            if (penultimateIndex >= 0) {
                char penultimateCharacter = formula.charAt(penultimateIndex);
                if (isOperationSymbol(penultimateCharacter))
                    formula = formula.substring(0, formula.length() - 1);
            }
            else
                formula = formula.substring(0, formula.length() - 1) + "+";
        }
        else if (lastCharacter == '%' || lastCharacter == ')') {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
        else {
            String lastNum = getLastNum(formula);
            if (lastNum == null) {
                Toast.makeText(this, "Unknown error SignFlip", Toast.LENGTH_SHORT).show();
                return;
            }
            int lastNumberStartIndex = getLastNumStartIndex(formula);
            if (lastNum.startsWith("+")) {
                formula = formula.substring(0, lastNumberStartIndex) + "-(-" + formula.substring(lastNumberStartIndex + 1);
            }
            else if (lastNum.startsWith("-")) {
                formula = formula.substring(0, lastNumberStartIndex) + formula.substring(lastNumberStartIndex + 1);
            }
            else {
                formula = formula.substring(0, lastNumberStartIndex) + "-" + formula.substring(lastNumberStartIndex);
            }
        }

        formulaView.setText(formula);
    }

    public boolean areParenthesisMatched(String formula) {
        int openingBracesCount = 0;
        int closingBracesCount = 0;

        char lastBrace = 0;
        for (int i = formula.length() - 1; i >= 0; --i) {
            if (lastBrace == '(' && formula.charAt(i) == ')') {
                // rightmost block of braces pairs is over
                break;
            }
            else if (formula.charAt(i) == '(') {
                openingBracesCount++;
                lastBrace = '(';
            }
            else if (formula.charAt(i) == ')') {
                closingBracesCount++;
                lastBrace = ')';
            }
        }

        return closingBracesCount == openingBracesCount;
    }

    public void onClick_ParenthesisInput(View v) {
        var formula = formulaView.getText().toString();

        if (formula.isEmpty()) {
            formula = "(";
            formulaView.setText(formula);
            return;
        }

        char lastCharacter = formula.charAt(formula.length() - 1);

        if (isOperationSymbol(lastCharacter) || lastCharacter == '(') {
            formula += "(";
        }
        else {
            if (areParenthesisMatched(formula)) {
                formula += "*(";
            }
            else {
                formula += ')';
            }
        }

        formulaView.setText(formula);
    }

    public void onClick_Clear(View v) {
        formulaView.setText("");
    }

    public void onClick_Backspace(View v) {
        var formula = formulaView.getText().toString();
        if (!formula.isEmpty()) {
            formula = formula.substring(0, formula.length() - 1);
            formulaView.setText(formula);
        }
    }
}
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

    public String getLastNum(String formula) {
        String result = null;
        for (int i = formula.length() - 1; i >= 0; --i) {
            if (formula.charAt(i) == '*' || formula.charAt(i) == '/' || formula.charAt(i) == '(' || formula.charAt(i) == ')' || formula.charAt(i) == '%') {
                result = formula.substring(i + 1);
                break;
            }
            if (i == 0)
                return formula;
            if (formula.charAt(i) == '+' || formula.charAt(i) == '-' ) {
                result = formula.substring(i);
                break;
            }
        }
        return result;
    }

    public void onClick_NumInput(View v) {
        Button b = (Button)v;
        CharSequence formula = formulaView.getText();
        if (formula.length() == 0) {
            formulaView.setText(formulaView.getText().toString() + b.getText());
        }
        else if ((formula.charAt(formula.length() - 1) == '%' || formula.charAt(formula.length() - 1) == ')')){
            formulaView.setText(formulaView.getText().toString() + "*" + b.getText());
        } else {
            String lastNum = getLastNum(formula.toString());
            Toast.makeText(this, lastNum, Toast.LENGTH_SHORT).show();
            if (formula.charAt(formula.length() - 1) == '0' && Objects.equals(lastNum, "0")) {
                formulaView.setText(formulaView.getText().toString().substring(0, formula.length() - 1) + b.getText());
            }
            else {
                formulaView.setText(formulaView.getText().toString() + b.getText());
            }
        }
    }

    public void onClick_PercentInput(View v) {
        Button b = (Button)v;
        CharSequence formula = formulaView.getText();
        if (formula.length() == 0) {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }
        else {
            char lastChar = formula.charAt(formula.length() - 1);
            if (isOperationSymbol(lastChar) || lastChar == '(' || lastChar == '%') {
                Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
            }
            else {
                formulaView.setText(formulaView.getText().toString() + b.getText());
            }
        }
    }

    public boolean isOperationSymbol(char c) {
        return (c == '/' || c == '*' || c == '-' || c == '+');
    }

    public void onClick_MulDivInput(View v) {
        Button b = (Button)v;
        CharSequence formula = formulaView.getText();
        if (formula.length() != 0) {
            char lastChar = formula.charAt(formula.length() - 1);
            if (isOperationSymbol(lastChar)) {
                if (lastChar == '(' || lastChar == b.getText().charAt(0)) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
                }
                else {
                    formulaView.setText(formulaView.getText().toString().substring(0, formula.length() - 1) + b.getText());
                }
            }
            else {
                formulaView.setText(formulaView.getText().toString() + b.getText());
            }
        } else {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_AddSubInput(View v) {
        Button b = (Button)v;
        CharSequence formula = formulaView.getText();
        if (formula.length() != 0) {
            char lastChar = formula.charAt(formula.length() - 1);
            if (isOperationSymbol(lastChar)) {
                if (lastChar == b.getText().charAt(0)) {
                    Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
                }
                else {
                    formulaView.setText(formulaView.getText().toString().substring(0, formula.length() - 1) + b.getText());
                }
            }
            else {
                formulaView.setText(formulaView.getText().toString() + b.getText());
            }
        } else {
            Toast.makeText(this, "Invalid expression", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNumber(char c) {
        return (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6'
                || c == '7' || c == '8' || c == '9');
    }

    public void onClick_decPointInput(View v) {
        Button b = (Button) v;
        CharSequence formula = formulaView.getText();
        if (formula.length() == 0) {
            formulaView.setText("0.");
        } else {
            char lastChar = formula.charAt(formula.length() - 1);
            if (lastChar == b.getText().charAt(0)) {
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
                    formulaView.setText(formulaView.getText().toString() + ".");
            }
            else if (lastChar == '%' || lastChar == ')') {
                formulaView.setText(formulaView.getText().toString() + "*0.");
            }
            else /* if (isOperationSymbol(lastChar)) */ {
                formulaView.setText(formulaView.getText().toString() + "0.");
            }
        }
    }

    public void onClick_inputZero(View v) {
        Button b = (Button) v;
        CharSequence formula = formulaView.getText();
        if (formula.length() == 0) {
            formulaView.setText(b.getText());
            return;
        }

        char lastCharacter = formula.charAt(formula.length() - 1);
        if (lastCharacter == '%' || lastCharacter == ')'){
            formulaView.setText(formulaView.getText().toString() + "*" + b.getText());
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
                    formulaView.setText(formulaView.getText().toString() + b.getText());
                }
                else {
                    Toast.makeText(this, "Not a fraction", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                formulaView.setText(formulaView.getText().toString() + b.getText());
            }
        }

    }

    public void onClick_SignFlip(View v) {
        Button b = (Button) v;
        CharSequence formula = formulaView.getText();
        if (formula.length() == 0) {
            formulaView.setText("(-");
            return;
        }

        char lastCharacter = formula.charAt(formula.length() - 1);

    }

    public void onClick_Clear(View v) {
        formulaView.setText("");
    }
}
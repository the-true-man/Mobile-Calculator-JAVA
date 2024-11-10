package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView history;
    private boolean isEqually = false;
    private boolean isAction = false;
    private EditText display;
    private double num1;
    private double num2;

    private char operator;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        history = findViewById(R.id.historyLabel);
    }
    public String zeroTrimming(double num){
        String s = String.valueOf(num);
        if (s.endsWith(".0")) {
            return s.substring(0, s.length() - 2); // Удаляем ".0"
        }
        return s;
    }
    public void addNumberAction(View view){
        if(display.getText().toString().equals("0")){
            display.setText("");
        }
        if(isAction){
            display.setText(((Button) view).getText());
        }
        else{
            display.append(((Button) view).getText());
        }
        setFontForContent();
        isEqually = false;
        isAction = false;
    }
    public void setFontForContent(){
        if(display.getText().toString().length()>=16){
            display.post(() -> {
                float textSize = display.getTextSize();
                while (display.getPaint().measureText(display.getText().toString()) > display.getWidth()) {
                    textSize--;
                    display.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
            });
        }
        else{
            display.setTextSize(45);
        }

    }
    public void plusAction(View view) {
        if (!Double.isNaN(operator) && !isAction) {
            equallyAction(view);
        }

        operator = ((Button) view).getText().charAt(0);
        isAction = true;
        String h = zeroTrimming(num1)+" " + operator+" ";
        history.setText(h);
    }
    public void clearClick(View view) {
        display.setText("0");
        num1 = 0;
        num2 = Double.NaN;
        operator = ' ';
        history.setText("");
        display.setTextSize(45);
    }
    public void equallyAction(View view) {
        if(Double.isNaN(num1)){
            return;
        }
        if(!isEqually){
            num2 = Double.parseDouble(display.getText().toString());
        }
        isEqually = true;
        if(operator == '+'){
            display.setText(zeroTrimming(num1+num2));
        }
        else if(operator == '-'){
            display.setText(zeroTrimming(num1-num2));
        }
        else if(operator == '*'){
            display.setText(zeroTrimming(num1*num2));
        }
        else if(operator == '/'){
            if(num2 == 0){
                clearClick(view);
                return;
            }
            display.setText(zeroTrimming(num1/num2));
        }
        String h = "";
        if(operator != ' '){
            h = zeroTrimming(num1) +" " +operator +" "+ zeroTrimming(num2)+" = ";
        }
        else{
             h = zeroTrimming(num1) +" = ";
        }
        history.setText(h);
        num1 = Double.parseDouble(display.getText().toString());
        isAction = true;
        if(display.getText().toString().toLowerCase().contains("infinity")){
            clearClick(display);
        }
        setFontForContent();


    }
    @SuppressLint("SetTextI18n")
    public void addDot(View view){
        if(display.getText().toString().contains(".")){
            return;
        }
        isAction = false;
        display.setText(display.getText().toString()+".");
    }
}
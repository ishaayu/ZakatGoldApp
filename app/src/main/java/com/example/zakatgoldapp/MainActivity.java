package com.example.zakatgoldapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // UI Components
    TextInputEditText etWeight, etGoldValue;
    AutoCompleteTextView spinnerType;
    Button btnCalculate, btnClear;
    TextView tvTotalValue, tvZakatPayable, tvTotalZakat;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etWeight = findViewById(R.id.etWeight);
        etGoldValue = findViewById(R.id.etGoldValue);
        spinnerType = findViewById(R.id.spinnerType);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvZakatPayable = findViewById(R.id.tvZakatPayable);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);


        String[] items = {"Keep", "Wear"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerType.setAdapter(adapter);
        spinnerType.setKeyListener(null);


        btnCalculate.setOnClickListener(v -> calculateZakat());
        btnClear.setOnClickListener(v -> clearInputs());
    }

    private void calculateZakat() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String weightStr = etWeight.getText().toString();
        String valueStr = etGoldValue.getText().toString(); // Get Gold Value
        String type = spinnerType.getText().toString();


        if (weightStr.isEmpty() || valueStr.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double currentGoldValue = Double.parseDouble(valueStr);
            double X;


            if (type.equalsIgnoreCase("Keep")) {
                X = 85.0;
            } else {
                X = 200.0;
            }


            double totalGoldValue = weight * currentGoldValue;


            double weightMinusX = weight - X;


            double zakatPayableValue;
            if (weightMinusX <= 0) {
                zakatPayableValue = 0;
            } else {
                zakatPayableValue = weightMinusX * currentGoldValue;
            }


            double totalZakat = zakatPayableValue * 0.025;


            DecimalFormat currency = new DecimalFormat("RM #,##0.00");

            tvTotalValue.setText("Total Gold Value: " + currency.format(totalGoldValue));
            tvZakatPayable.setText("Zakat Payable Value: " + currency.format(zakatPayableValue));
            tvTotalZakat.setText(currency.format(totalZakat));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        etWeight.setText("");
        etGoldValue.setText("");
        spinnerType.setText("");
        tvTotalValue.setText("Total Gold Value: RM 0.00");
        tvZakatPayable.setText("Zakat Payable Value: RM 0.00");
        tvTotalZakat.setText("RM 0.00");
        etWeight.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
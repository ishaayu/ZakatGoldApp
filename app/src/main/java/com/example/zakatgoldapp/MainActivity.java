package com.example.zakatgoldapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // 1. UI Components
    Toolbar toolbar;
    TextInputEditText etWeight;
    AutoCompleteTextView spinnerType;
    Button btnCalculate, btnClear;
    TextView tvTotalZakat, tvTotalValue, tvZakatPayable;


    private static final double GOLD_PRICE_PER_GRAM = 310.50;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. Initialize Views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etWeight = findViewById(R.id.etWeight);
        spinnerType = findViewById(R.id.spinnerType);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        tvTotalZakat = findViewById(R.id.tvTotalZakat);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvZakatPayable = findViewById(R.id.tvZakatPayable);


        String[] goldTypes = new String[]{"Simpanan (Keep)", "Perhiasan (Wear)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                goldTypes
        );
        spinnerType.setAdapter(adapter);


        spinnerType.setKeyListener(null);


        btnCalculate.setOnClickListener(v -> calculateZakat());
        btnClear.setOnClickListener(v -> clearInputs());


        clearInputs();
    }

    private void calculateZakat() {
        String weightStr = etWeight.getText().toString();
        String type = spinnerType.getText().toString();


        if (weightStr.isEmpty()) {
            etWeight.setError("Sila masukkan berat emas (Please enter weight)");
            etWeight.requestFocus();
            return;
        }
        if (type.isEmpty()) {
            Toast.makeText(this, "Sila pilih jenis emas (Please select type)", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double uruf;
            double zakatPayableWeight;
            double zakatAmount;


            if (type.equals("Simpanan (Keep)")) {
                uruf = 85.0;

                zakatPayableWeight = (weight >= uruf) ? weight : 0;
            } else {
                uruf = 200.0;

                zakatPayableWeight = (weight > uruf) ? (weight - uruf) : 0;
            }

            double totalValue = weight * GOLD_PRICE_PER_GRAM;
            zakatAmount = zakatPayableWeight * GOLD_PRICE_PER_GRAM * 0.025;


            DecimalFormat currencyFormat = new DecimalFormat("RM #,##0.00");
            DecimalFormat weightFormat = new DecimalFormat("#.## g");


            tvTotalValue.setText("Nilai Emas: " + currencyFormat.format(totalValue));
            tvZakatPayable.setText("Berat Wajib Zakat: " + weightFormat.format(zakatPayableWeight));
            tvTotalZakat.setText(currencyFormat.format(zakatAmount));

        } catch (NumberFormatException e) {
            etWeight.setError("Input tidak sah");
        }
    }

    private void clearInputs() {
        etWeight.setText("");
        spinnerType.setText("", false); // Clear selection
        spinnerType.clearFocus();

        tvTotalValue.setText("Nilai Emas: RM 0.00");
        tvZakatPayable.setText("Berat Wajib Zakat: 0g");
        tvTotalZakat.setText("RM 0.00");

        etWeight.setError(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_about) {

            try {
                startActivity(new Intent(this, AboutActivity.class));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Halaman 'About' belum dibuat.", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (itemId == R.id.menu_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Aplikasi Zakat Emas");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Kira zakat emas anda dengan mudah! Nilai semasa: RM " + GOLD_PRICE_PER_GRAM + "/g");
            startActivity(Intent.createChooser(shareIntent, "Kongsi melalui"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
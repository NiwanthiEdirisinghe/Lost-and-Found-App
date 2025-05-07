package com.example.lostfoundapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {
    private RadioGroup rgPostType;
    private RadioButton rbLost, rbFound;
    private EditText etName, etPhone, etDescription, etDate, etLocation;
    private Button btnSave;

    private ImageButton btnBack;
    private TextView tvTitle;
    private Calendar calendar;

    private DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        dbHelper = new DBHelper(this);

        calendar = Calendar.getInstance();

        tvTitle = findViewById(R.id.tvTitle);
        rgPostType = findViewById(R.id.rgPostType);
        rbLost = findViewById(R.id.rbLost);
        rbFound = findViewById(R.id.rbFound);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        rbLost.setChecked(true);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateField();
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateAdvertActivity.this,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etDate.setFocusable(false);
        etDate.setClickable(true);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateDateField() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        etDate.setText(sdf.format(calendar.getTime()));
    }

    private void saveItem() {
        if (validateInputs()) {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String location = etLocation.getText().toString().trim();

            String type = rbLost.isChecked() ? "Lost" : "Found";

            Item item = new Item(name, phone, description, date, location, type);

            long id = dbHelper.insertItem(item);

            if (id > 0) {
                Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show();
                clearInputs();
                finish(); // Go back to previous activity
            } else {
                Toast.makeText(this, "Failed to save item!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Name is required");
            isValid = false;
        }

        if (etPhone.getText().toString().trim().isEmpty()) {
            etPhone.setError("Phone number is required");
            isValid = false;
        }

        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Description is required");
            isValid = false;
        }

        if (etDate.getText().toString().trim().isEmpty()) {
            etDate.setError("Date is required");
            isValid = false;
        }

        if (etLocation.getText().toString().trim().isEmpty()) {
            etLocation.setError("Location is required");
            isValid = false;
        }

        return isValid;
    }

    private void clearInputs() {
        etName.setText("");
        etPhone.setText("");
        etDescription.setText("");
        etDate.setText("");
        etLocation.setText("");
        rbLost.setChecked(true);
    }
}
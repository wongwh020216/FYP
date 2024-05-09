package com.example.residentapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class VehicleBookingActivity extends AppCompatActivity {

    private ImageView img;
    private TextView dModel,dPlateNumber,dRate,dDate,dTime,dPrice;

    private Button bDate,bTime,bConfirm;
    Spinner spinner;
    String [] hours = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_booking);

        mDatabase = FirebaseDatabase.getInstance().getReference("VehicleBookings");

        img = findViewById(R.id.dImageVehicle);
        dModel = findViewById(R.id.dModel);
        dPlateNumber = findViewById(R.id.dPlateNumber);
        dRate = findViewById(R.id.dRate);
        spinner = findViewById(R.id.spinner_hours);
        dDate = findViewById(R.id.vehicle_renting_date);
        dTime = findViewById(R.id.vehicle_renting_time);
        bDate = findViewById(R.id.vehicle_renting_date_button);
        bTime = findViewById(R.id.vehicle_renting_time_button);
        dPrice = findViewById(R.id.dPrice);
        bConfirm = findViewById(R.id.confirm_booking_button);

        String Model = getIntent().getStringExtra("dModel");
        String PlateNumber = getIntent().getStringExtra("dPlateNumber");
        String Rate = getIntent().getStringExtra("dRate");
        String ImageVehicle = getIntent().getStringExtra("dImageVehicle");

        dModel.setText(Model);
        dPlateNumber.setText(PlateNumber);
        dRate.setText(Rate);
        Glide.with(this)
                .load(ImageVehicle)
                .into(img);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VehicleBookingActivity.this, R.layout.spinner_item,hours);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(dDate);
            }
        });

        bTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialog(dTime);
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndConfirmBooking();
            }
        });

    }

    private void openDialog(final TextView targetTextView) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, dayOfMonth, month, year) -> {
                    // Set the selected date to visitDateText
                    String formattedDate = formatDate(dayOfMonth, month, year);
                    targetTextView.setText(formattedDate);
                },
                currentYear, currentMonth, currentDayOfMonth);

        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        // Format the date as desired (e.g., yyyy/MM/dd)
        return String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
    }

    private void openTimePickerDialog(final TextView targetTextView) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    // Set the selected time to the targetTextView
                    String formattedTime = formatTime(hourOfDay, minute);
                    targetTextView.setText(formattedTime);
                },
                currentHour, currentMinute, false); // false for 12-hour format, true for 24-hour format

        timePickerDialog.show();
    }

    private String formatTime(int hourOfDay, int minute) {
        // Format the time as desired (e.g., hh:mm AM/PM)
        String amPm = hourOfDay < 12 ? "AM" : "PM";
        int hour = hourOfDay % 12 == 0 ? 12 : hourOfDay % 12;
        return String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm);
    }

    private void calculatePrice() {
        // Get the selected hour from the spinner
        String selectedHour = spinner.getSelectedItem().toString();
        // Parse the rate as a double
        double rate = Double.parseDouble(dRate.getText().toString());
        // Parse the selected hour as an integer
        int hour = Integer.parseInt(selectedHour);
        // Calculate the price
        double price = rate * hour;
        // Update the price TextView
        dPrice.setText(String.valueOf(price));
    }


    private void validateAndConfirmBooking() {
        // Get values to save
        String date = dDate.getText().toString();
        String time = dTime.getText().toString();
        String id = mDatabase.push().getKey();

        // Check if any field is empty
        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(VehicleBookingActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to confirm the booking?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // If user confirms, save booking to Firebase
                        saveBookingToFirebase();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // If user cancels, dismiss dialog
                        dialog.dismiss();
                    })
                    .show();
        }



    }

    private void saveBookingToFirebase() {
        String model = dModel.getText().toString();
        String plateNumber = dPlateNumber.getText().toString();
        String date = dDate.getText().toString();
        String time = dTime.getText().toString();
        String price = dPrice.getText().toString();
        String selectedHour = spinner.getSelectedItem().toString();

        String id = mDatabase.push().getKey();

        // Create a Booking object
        VehicleBooking booking = new VehicleBooking(id,model, plateNumber ,date, time, price, selectedHour);



        // Save the booking to Firebase Realtime Database
        mDatabase.child(id).setValue(booking);

        // Show success message to the user
        Toast.makeText(VehicleBookingActivity.this, "Booking saved successfully!", Toast.LENGTH_SHORT).show();
    }
}
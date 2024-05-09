package com.example.residentapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
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

public class FacilityBookingActivity extends AppCompatActivity {

    private ImageView img;
    private TextView dFacilityName,dDate,dTime,duration,status;
    private Spinner spinner;
    private String [] hours = {"1","2","3"};
    private Button bDate,bTime,bConfirm;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_booking);

        mDatabase = FirebaseDatabase.getInstance().getReference("FacilityBooking");

        img = findViewById(R.id.dFacilityImg);
        dFacilityName = findViewById(R.id.dFacilityName);
        duration = findViewById(R.id.duration);
        spinner = findViewById(R.id.spinner_hours);
        dDate = findViewById(R.id.facility_renting_date);
        dTime = findViewById(R.id.facility_renting_time);
        bDate = findViewById(R.id.facility_renting_date_button);
        bTime = findViewById(R.id.facility_renting_time_button);
        bConfirm = findViewById(R.id.confirm_facility_button);

        String FName = getIntent().getStringExtra("dFacilityName");
        String FImg = getIntent().getStringExtra("dFacilityImg");

        dFacilityName.setText(FName);
        Glide.with(this)
                .load(FImg)
                .into(img);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FacilityBookingActivity.this, R.layout.spinner_item,hours);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);



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

    private void validateAndConfirmBooking() {
        // Get values to save
        String date = dDate.getText().toString();
        String time = dTime.getText().toString();
        String id = mDatabase.push().getKey();

        // Check if any field is empty
        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(FacilityBookingActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
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

        String facilityName = dFacilityName.getText().toString();
        String date = dDate.getText().toString();
        String time = dTime.getText().toString();
        String selectedDuration = spinner.getSelectedItem().toString();
        String status;
        String id = mDatabase.push().getKey();

        status = "Pending";

        // Create a Booking object
        FacilityBooking booking = new FacilityBooking(id,facilityName,date,time,selectedDuration,status);


        // Save the booking to Firebase Realtime Database
        mDatabase.child(id).setValue(booking);

        // Show success message to the user
        Toast.makeText(FacilityBookingActivity.this, "Booking saved successfully!", Toast.LENGTH_SHORT).show();
    }

}
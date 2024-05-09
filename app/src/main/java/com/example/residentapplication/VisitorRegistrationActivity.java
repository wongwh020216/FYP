package com.example.residentapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.residentapplication.databinding.ActivityVisitorRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class VisitorRegistrationActivity extends AppCompatActivity {

    ActivityVisitorRegistrationBinding binding;
    String visitorName, visitorPhoneNUmber, purposeOfVisit, vehicleNumber, visitDate, endDate,overnightTxt,status;
    FirebaseDatabase db;
    DatabaseReference ref;


    private TextView visitDateText;
    private TextView endDateText;
    private Button visitDateButton;
    private Button endDateButton;
    private Button walkInVehicleButton,overNightButton;
    private EditText visitorVehicleNumber;
    private Button confirmButton;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_registration);

        binding = ActivityVisitorRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        visitDateButton = findViewById(R.id.visit_date_button);
        endDateButton = findViewById(R.id.end_date_button);
        visitDateText = findViewById(R.id.visit_date_txt);
        endDateText = findViewById(R.id.end_date_txt);
        walkInVehicleButton = findViewById(R.id.walk_in_vehicle_button);
        visitorVehicleNumber = findViewById(R.id.visitor_vehicle_number);
        confirmButton = findViewById(R.id.confirm_button);
        overNightButton = findViewById(R.id.overnight_button);


        ref = FirebaseDatabase.getInstance().getReference().child("Visitors");


        visitDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(visitDateText);
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(endDateText);
            }
        });

        walkInVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If EditText is currently visible, make it invisible; otherwise, make it visible
                if (visitorVehicleNumber.getVisibility() == View.VISIBLE) {
                    visitorVehicleNumber.setVisibility(View.INVISIBLE);
                    walkInVehicleButton.setText("Walk-In");
                } else {
                    visitorVehicleNumber.setVisibility(View.VISIBLE);
                    walkInVehicleButton.setText("Vehicle");

                }
            }
        });

        overNightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the current text of the button
                String buttonText = overNightButton.getText().toString();

                // Toggle the text based on the current text
                if (buttonText.equals("Overnight")) {
                    overNightButton.setText("Not overnight");
                    status = "Approved";

                    // Make end date button and text visible
                    endDateButton.setVisibility(View.INVISIBLE);
                    endDateText.setVisibility(View.INVISIBLE);

                    // Make visit date button and text invisible
                    visitDateButton.setVisibility(View.VISIBLE);
                    visitDateText.setVisibility(View.VISIBLE);

                } else {
                    overNightButton.setText("Overnight");
                    status = "Pending";

                    // Make end date button and text invisible
                    endDateButton.setVisibility(View.VISIBLE);
                    endDateText.setVisibility(View.VISIBLE);

                    // Make visit date button and text visible
                    visitDateButton.setVisibility(View.VISIBLE);
                    visitDateText.setVisibility(View.VISIBLE);
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.navigation_home) {
                            startActivity(new Intent(VisitorRegistrationActivity.this, HomeScreenActivity.class));
                            return true;
                        } else if (id == R.id.navigation_history) {
                            startActivity(new Intent(VisitorRegistrationActivity.this, HistoryActivity.class));
                            return true;
                        } else if (id == R.id.navigation_profile) {
                            startActivity(new Intent(VisitorRegistrationActivity.this, UserProfileActivity.class));
                            return true;
                        }
                        return false;
                    }
                });



        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ref.push().getKey();

                visitorName = binding.visitorName.getText().toString();
                visitorPhoneNUmber = binding.visitorPhoneNumber.getText().toString();
                purposeOfVisit = binding.purposeVisit.getText().toString();
                vehicleNumber = binding.visitorVehicleNumber.getText().toString();
                visitDate = binding.visitDateTxt.getText().toString();
                endDate = binding.endDateTxt.getText().toString();
                String type = binding.overnightButton.getText().toString();


                String status;
                if (type.equals("Overnight")) {
                    status = "Pending";
                } else {
                    status = "Approved";
                }

                String phonePattern = "\\d{10,11}";

                if (!visitorName.isEmpty() && !visitorPhoneNUmber.isEmpty() && !purposeOfVisit.isEmpty() ) {

                    if (!visitorPhoneNUmber.matches(phonePattern)) {
                        binding.visitorPhoneNumber.setError("Invalid phone number");
                        return;
                    }



                    Visitor visitor = new Visitor(id,visitorName, visitorPhoneNUmber, purposeOfVisit, vehicleNumber, visitDate, endDate, type, status);
                    db = FirebaseDatabase.getInstance();
                    ref = db.getReference("Visitor");


                    // Set visitor data with the generated ID
                    ref.child(id).setValue(visitor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Clear input fields after successful registration
                                binding.visitorName.setText("");
                                binding.visitorPhoneNumber.setText("");
                                binding.purposeVisit.setText("");
                                binding.visitorVehicleNumber.setText("");
                                binding.visitDateTxt.setText("");
                                binding.endDateTxt.setText("");

                                Toast.makeText(VisitorRegistrationActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(VisitorRegistrationActivity.this, DisplayVisitorDetailsActivity.class);
                                intent.putExtra("visitorId", id); // Pass visitor ID to the next activity
                                intent.putExtra("keyName",visitorName);
                                intent.putExtra("keyPhoneNumber",visitorPhoneNUmber);
                                intent.putExtra("keyPurpose",purposeOfVisit);
                                intent.putExtra("keyVehicle",vehicleNumber);
                                intent.putExtra("keyVisitDate",visitDate);
                                intent.putExtra("keyType", type); // Changed keyName to keyType
                                intent.putExtra("keyEndDate",endDate);
                                startActivity(intent);
                            } else {
                                Toast.makeText(VisitorRegistrationActivity.this, "Failed to register: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(VisitorRegistrationActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                }
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




}
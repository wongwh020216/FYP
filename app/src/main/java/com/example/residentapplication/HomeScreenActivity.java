package com.example.residentapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeScreenActivity extends AppCompatActivity {

    private Button EhailingButton;
    private Button VehicleRentingButton;
    private Button VisitorRegistrationButton;
    private Button EmergencyServiceButton,FacilitiesButton;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private Button LogoutButton;

    private Button TestingButton;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //EhailingButton = findViewById(R.id.e_hailing_btn);
        VehicleRentingButton = findViewById(R.id.vehicle_renting_button);
        VisitorRegistrationButton = findViewById(R.id.visitor_registration_button);
        EmergencyServiceButton = findViewById(R.id.emergency_services_button);
        LogoutButton = (Button) findViewById(R.id.logout_btn);
        FacilitiesButton = findViewById(R.id.facilities_button);





        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

//        EhailingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ehailingIntent = new Intent(HomeScreenActivity.this, E_hailingActivity.class );
//                startActivity(ehailingIntent);
//            }
//        });

        VehicleRentingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vehicleRentingIntent = new Intent(HomeScreenActivity.this, VehicleRentingActivity.class );
                startActivity(vehicleRentingIntent);
            }
        });

        VisitorRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visitorRegistrationIntent = new Intent(HomeScreenActivity.this, VisitorRegistrationActivity.class );
                startActivity(visitorRegistrationIntent);
            }
        });

        EmergencyServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emergencyServiceIntent = new Intent(HomeScreenActivity.this, EmergencyServicesActivity.class );
                startActivity(emergencyServiceIntent);
            }
        });

        FacilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facilitiesIntent = new Intent(HomeScreenActivity.this, DisplayFacilitiesActivity.class );
                startActivity(facilitiesIntent);
            }
        });

//        TestingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent vehicleBookingIntent = new Intent(HomeScreenActivity.this,VehicleBookingActivity.class);
//                startActivity(vehicleBookingIntent);
//            }
//        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation item selection here
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    return true;
                } else if (itemId == R.id.navigation_history) {
                    startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


    }




    private void Logout() {
        Intent loginIntent = new Intent(HomeScreenActivity.this, UserLoginActivity.class);
        loginIntent.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        startActivity(loginIntent);
        finish();
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Perform logout
                        mAuth.signOut();
                        Logout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




}
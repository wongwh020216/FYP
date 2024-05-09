package com.example.residentapplication;

import static android.content.ContentValues.TAG;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmergencyServicesActivity extends AppCompatActivity {

    private TextView policeNumber,ambulanceNumber,fireNumber;
    private DatabaseReference ref;
    private Button policeButton,ambulanceButton,fireButton,moreButton;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_services);

        policeNumber = findViewById(R.id.police_number);
        ambulanceNumber = findViewById(R.id.ambulance_number);
        fireNumber = findViewById(R.id.fire_number);
        policeButton = findViewById(R.id.police_button);
        ambulanceButton = findViewById(R.id.ambulance_button);
        fireButton = findViewById(R.id.fire_button);
        moreButton = findViewById(R.id.more_button);

        ref = FirebaseDatabase.getInstance().getReference("EmergencyContacts");

        ambulanceButton.setBackgroundResource(R.drawable.ambulance);
        fireButton.setBackgroundResource(R.drawable.fire);
        policeButton.setBackgroundResource(R.drawable.police);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value1 = snapshot.child("Police").getValue(String.class);
                String value2 = snapshot.child("Ambulance").getValue(String.class);
                String value3 = snapshot.child("Fire").getValue(String.class);

                Log.d(TAG, "Value 1 is: " + value1);
                Log.d(TAG, "Value 2 is: " + value2);
                Log.d(TAG, "Value 3 is: " + value3);

                // Set the text of the TextViews with the retrieved values
                policeNumber.setText(value1);
                ambulanceNumber.setText(value2);
                fireNumber.setText(value3);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        policeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallPolice();
            }
        });

        ambulanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallAmbulance();
            }
        });

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallFire();
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moreContactsIntent = new Intent(EmergencyServicesActivity.this, MoreContactsActivity.class );
                startActivity(moreContactsIntent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.navigation_home) {
                            startActivity(new Intent(EmergencyServicesActivity.this, HomeScreenActivity.class));
                            return true;
                        } else if (id == R.id.navigation_history) {
                            startActivity(new Intent(EmergencyServicesActivity.this, HistoryActivity.class));
                            return true;
                        } else if (id == R.id.navigation_profile) {
                            startActivity(new Intent(EmergencyServicesActivity.this, UserProfileActivity.class));
                            return true;
                        }
                        return false;
                    }
                });

    }

    private void CallPolice(){
        String number = policeNumber.getText().toString();
        if(number.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(EmergencyServicesActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EmergencyServicesActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else{
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    private void CallAmbulance(){
        String number = ambulanceNumber.getText().toString();
        if(number.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(EmergencyServicesActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EmergencyServicesActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else{
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    private void CallFire(){
        String number = fireNumber.getText().toString();
        if(number.trim().length() > 0){
            if (ContextCompat.checkSelfPermission(EmergencyServicesActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EmergencyServicesActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else{
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CallPolice();
                CallAmbulance();
                CallFire();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
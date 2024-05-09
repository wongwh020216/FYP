package com.example.residentapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class VehicleRentingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    VehicleAdapter vehicleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_renting);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<VehicleModel> options =
                new FirebaseRecyclerOptions.Builder<VehicleModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("RentVehicle"), VehicleModel.class)
                        .build();


        vehicleAdapter = new VehicleAdapter(options);
        recyclerView.setAdapter(vehicleAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(
                new BottomNavigationView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.navigation_home) {
                            startActivity(new Intent(VehicleRentingActivity.this, HomeScreenActivity.class));
                            return true;
                        } else if (id == R.id.navigation_history) {
                            startActivity(new Intent(VehicleRentingActivity.this, HistoryActivity.class));
                            return true;
                        } else if (id == R.id.navigation_profile) {
                            startActivity(new Intent(VehicleRentingActivity.this, UserProfileActivity.class));
                            return true;
                        }
                        return false;
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
        vehicleAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vehicleAdapter.startListening();
    }


}
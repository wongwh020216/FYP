package com.example.residentapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<Object> itemList = new ArrayList<>();
    private DatabaseReference vehicleRef;
    private DatabaseReference facilityRef;
    private ProgressBar progressBar;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.history_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar=findViewById(R.id.loading_indicator1);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_history);

        Button toggleListButton = findViewById(R.id.toggle_filter_button); // Assuming you have a button with this ID in your layout


        toggleListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = toggleListButton.getText().toString(); // Convert to uppercase for case-insensitive comparison
                // Clear the current list
                itemList.clear();
                if (buttonText.equals("Vehicles")) {
                    toggleListButton.setText("Facilities");
                    // Load vehicle data
                    loadFacilityData();
                    // Change button text to Facilities
                } else {
                    toggleListButton.setText("Vehicles");
                    // Load facility data
                    loadVehicleData();
                    // Change button text to Vehicles
                }
            }
        });



        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (itemId == R.id.navigation_history) {
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        // Firebase references
        vehicleRef = FirebaseDatabase.getInstance().getReference().child("VehicleBookings");
        facilityRef = FirebaseDatabase.getInstance().getReference().child("FacilityBooking");

        loadVehicleData();


    }

    private void loadVehicleData() {
        vehicleRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<VehicleBooking> vehicleList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    VehicleBooking vehicle = dataSnapshot.getValue(VehicleBooking.class);
                    vehicleList.add(vehicle);
                }
                Collections.reverse(vehicleList);
                itemList.addAll(vehicleList);
                updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void loadFacilityData() {
        facilityRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FacilityBooking> facilityList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FacilityBooking facility = dataSnapshot.getValue(FacilityBooking.class);
                    Log.e(TAG, facility.getFacilityName());
                    facilityList.add(facility);
                }
                Collections.reverse(facilityList);
                itemList.addAll(facilityList);
                updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void updateAdapter() {
        if (historyAdapter == null) {
            historyAdapter = new HistoryAdapter(HistoryActivity.this, itemList);
            recyclerView.setAdapter(historyAdapter);
        } else {
            historyAdapter.notifyDataSetChanged();
        }
        // Sort itemList based on a common attribute like timestamp
        Collections.sort(itemList, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                long timestamp1 = getTimestamp(o1);
                long timestamp2 = getTimestamp(o2);
                // Compare timestamps in reverse order to display newest items first
                return Long.compare(timestamp2, timestamp1);
            }

            private long getTimestamp(Object object) {
                if (object instanceof VehicleBooking) {
                    return ((VehicleBooking) object).getTimestamp();
                } else if (object instanceof FacilityBooking) {
                    return ((FacilityBooking) object).getTimestamp();
                }
                return 0;
            }
        });

        isLoadingVehicle(false);
    }

    void isLoadingVehicle(boolean loading){
        if (loading){
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}


package com.example.residentapplication;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

//import com.example.residentialapplication.R;
//import com.example.residentialapplication.adapter.FacilityAdapter;
//
//import com.example.residentialapplication.adapter.HomeAdapter;
//import com.example.residentialapplication.listerner.FacilitiesListerner;
//import com.example.residentialapplication.listerner.ItemListerner;
//import com.example.residentiapplication.model.Facilities;
//import com.example.residentialapplication.model.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DisplayFacilitiesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FacilityAdapter facilityAdapter;

    private LinkedHashSet<Facilities> facilitiesLhs= new LinkedHashSet<>();
    private DatabaseReference ref;
    private String id;
    private Intent intent,test;
    private ProgressBar progressBar;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_facilities);

        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.facility_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(facilityAdapter);
        ref = FirebaseDatabase.getInstance().getReference();

        progressBar=findViewById(R.id.loading_indicator1);



        isLoadingVehicle(true);



        FirebaseDatabase.getInstance().getReference().child("Facility")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            facilitiesLhs.add(new Facilities(
                                    Objects.requireNonNull(dataSnapshot.child("year").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("id").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("Purpose").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("Model").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("FacilityName").getValue()).toString()
                            ));
                            facilityAdapter=new FacilityAdapter(DisplayFacilitiesActivity.this, facilitiesLhs, new FacilitiesListerner() {
                                @Override
                                public void onFacilitiesPosition(Facilities facilities) {
                                    //intent=(new Intent(FacilityActivity.this,FacilityDetails.class));

                                    intent.putExtra("id",facilities.getId());
                                    intent.putExtra("Name",facilities.getName());
                                    intent.putExtra("purpose",facilities.getPurpose());
                                    intent.putExtra("model",facilities.getModel());
                                    intent.putExtra("year",facilities.getYear());
                                    intent.putExtra("image",facilities.getImage());
                                    startActivity(intent);

                                }

                            });
                            isLoadingVehicle(false);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DisplayFacilitiesActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(facilityAdapter);
                        }
                        //adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }



                });
    }
    void isLoadingVehicle(boolean loading){



        if (loading){
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }else {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }


}
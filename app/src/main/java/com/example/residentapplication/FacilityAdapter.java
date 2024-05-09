package com.example.residentapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.residentapplication.R;
import com.example.residentapplication.FacilitiesListerner;
import com.example.residentapplication.Facilities;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;

import java.util.LinkedHashSet;
import java.util.List;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.ViewHolder> {
    private Context context;
    private LinkedHashSet<Facilities> facilitiesList;
    private FacilitiesListerner facilityListener; // Corrected interface name

    // Constructor to initialize adapter with required data
    public FacilityAdapter(Context context, LinkedHashSet<Facilities> facilitiesList, FacilitiesListerner facilityListener) {
        this.context = context;
        this.facilitiesList = facilitiesList;
        this.facilityListener = facilityListener;
    }

    // onCreateViewHolder() method is called by RecyclerView to create a new ViewHolder instance
    @NonNull
    @Override
    public FacilityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.facility, parent, false);
        return new ViewHolder(view);
    }

    // onBindViewHolder() method is called by RecyclerView to bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull FacilityAdapter.ViewHolder holder, int position) {
        Facilities facilities =  (Facilities) facilitiesList.toArray()[position];
        String dFacilityName = facilities.getName();
        String dFacilityImg = facilities.getImage();


        // Set data to views in ViewHolder
        holder.name.setText(facilities.getName());


        Glide.with(holder.img.getContext())
                .load(facilities.getImage())
                .placeholder(com.firebase.geofire.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);








        // Set onClickListener for item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call onFacilitiesPosition method of FacilitiesListener interface with clicked item
                Intent intent = new Intent(holder.itemView.getContext(),FacilityBookingActivity.class);
                intent.putExtra("dFacilityName", dFacilityName);
                intent.putExtra("dFacilityImg",dFacilityImg);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    // getItemCount() method returns the total number of items in the dataset
    @Override
    public int getItemCount() {
        return facilitiesList.size();
    }

    // ViewHolder class holds references to views in each list item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        private TextView name;

        View view;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View FacilityView) {
            super(FacilityView);
            name = FacilityView.findViewById(R.id.name);
            img = FacilityView.findViewById(R.id.facility_img);
            view = FacilityView;
            relativeLayout = FacilityView.findViewById(R.id.relative_layout);
        }
    }
    public void setData(LinkedHashSet<Facilities> facilitiesList) {
        this.facilitiesList = facilitiesList;
        notifyDataSetChanged();
    }
}

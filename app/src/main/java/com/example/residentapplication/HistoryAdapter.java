package com.example.residentapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_VEHICLE = 1;
    private static final int TYPE_FACILITY = 2;

    private boolean showVehicleOnly = true;
    private List<Object> originalItemList;

    private Context context;
    private List<Object> itemList;

    // Constructor to initialize adapter with required data
    public HistoryAdapter(Context context, List<Object> itemList) {
        this.context = context;
        this.originalItemList = new ArrayList<>(itemList);
        this.itemList = itemList;

    }

    private boolean showVehicleList = true; // Flag to track whether to show vehicle list or facility list

    public void setShowVehicleList(boolean showVehicleList) {
        this.showVehicleList = showVehicleList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = itemList.get(position);
        if (item instanceof VehicleBooking) {
            return TYPE_VEHICLE;
        } else {
            return TYPE_FACILITY;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_VEHICLE) {
            View vehicleView = inflater.inflate(R.layout.history, parent, false);
            return new VehicleViewHolder(vehicleView);
        } else {
            View facilityView = inflater.inflate(R.layout.historyfacility, parent, false);
            return new FacilityViewHolder(facilityView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = itemList.get(position);

        if (item instanceof VehicleBooking) {
            ((VehicleViewHolder) holder).bind((VehicleBooking) item);
        } else {
            ((FacilityViewHolder) holder).bind((FacilityBooking) item);
        }
    }

    // getItemCount() method returns the total number of items in the dataset
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // ViewHolder class holds references to views in each list item
    private class VehicleViewHolder extends RecyclerView.ViewHolder {

        private TextView vehicleModel;
        private TextView plateNumber;
        private TextView vehiclePrice;
        private TextView bookingHours;
        private TextView bookingDate;
        private RelativeLayout relativeLayout;

        public VehicleViewHolder(@NonNull View vehicleView) {
            super(vehicleView);
            vehicleModel = vehicleView.findViewById(R.id.model);
            plateNumber = vehicleView.findViewById(R.id.plateNumber);
            bookingHours = vehicleView.findViewById(R.id.time);
            bookingDate = vehicleView.findViewById(R.id.date);
            vehiclePrice = vehicleView.findViewById(R.id.price);
            relativeLayout = vehicleView.findViewById(R.id.relative_layout);
        }

        public void bind(VehicleBooking vehicle) {
            vehicleModel.setText("Vehicle Name: " + vehicle.getModel());
            plateNumber.setText("Plate Number: " + vehicle.getPlateNumber());
            bookingDate.setText("Booking Date: " + vehicle.getDate());
            bookingHours.setText("Booking Hours: " + vehicle.getTime());
            vehiclePrice.setText("Price: " + vehicle.getPrice());
        }
    }

    private class FacilityViewHolder extends RecyclerView.ViewHolder {

        private TextView facilityName;
        private TextView facilityBookingTime;
        private TextView facilityBookingDate;
        private TextView facilityBookingDuration;
        private RelativeLayout relativeLayout;

        public FacilityViewHolder(@NonNull View facilityView) {
            super(facilityView);

            facilityName = facilityView.findViewById(R.id.facilityName);
            facilityBookingTime = facilityView.findViewById(R.id.facilityBookingTime);
            facilityBookingDate = facilityView.findViewById(R.id.facilityBookingDate);
            facilityBookingDuration = facilityView.findViewById(R.id.facilityBookingDuration);
            relativeLayout = facilityView.findViewById(R.id.relative_layout);
        }

        public void bind(FacilityBooking facility) {
            Log.e("bind: ", facility.getFacilityName());
            facilityName.setText("Facility Name: " + facility.getFacilityName());
            facilityBookingTime.setText("Booking Time: " + facility.getTime());
            facilityBookingDate.setText("Booking Date: " + facility.getDate());
            facilityBookingDuration.setText("Booking Duration: " + facility.getSelectedDuration());
        }
    }
}

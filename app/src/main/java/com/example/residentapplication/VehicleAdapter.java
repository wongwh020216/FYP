package com.example.residentapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class VehicleAdapter extends FirebaseRecyclerAdapter<VehicleModel,VehicleAdapter.myViewHolder>{



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public VehicleAdapter(@NonNull FirebaseRecyclerOptions<VehicleModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull VehicleModel model) {
        final String dModel = model.getVehicleModel();
        final String dPlateNumber = model.getPlateNumber();
        final String dRate = model.getRate();
        final String dImageVehicle = model.getImage();

        holder.vehicleModel.setText(model.getVehicleModel());
        holder.vehicleColor.setText(model.getVehicleColor());
        holder.vehicleRate.setText(model.getRate());
        holder.vehiclePlateNumber.setText(model.getPlateNumber());

        Glide.with(holder.img.getContext())
                .load(model.getImage())
                .placeholder(com.firebase.geofire.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),VehicleBookingActivity.class);
                intent.putExtra("dModel",dModel);
                intent.putExtra("dPlateNumber",dPlateNumber);
                intent.putExtra("dRate",dRate);
                intent.putExtra("dImageVehicle",dImageVehicle);
                holder.itemView.getContext().startActivity(intent);
            }

        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView vehicleModel,vehicleColor,vehiclePlateNumber,vehicleRate;
        View view;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img1);
            vehicleModel = (TextView) itemView.findViewById(R.id.vehicle_model);
            vehicleColor = (TextView) itemView.findViewById(R.id.vehicle_color);
            vehiclePlateNumber = (TextView) itemView.findViewById(R.id.vehicle_plate_number);
            vehicleRate = (TextView) itemView.findViewById(R.id.vehicle_rate);
            view=itemView;

        }

    }

}
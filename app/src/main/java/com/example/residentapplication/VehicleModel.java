package com.example.residentapplication;

public class VehicleModel {
    String PlateNumber, Rate, VehicleColor, VehicleModel, image;

    VehicleModel(){

    }

    public VehicleModel(String plateNumber, String rate, String vehicleColor, String vehicleModel, String image) {
        PlateNumber = plateNumber;
        Rate = rate;
        VehicleColor = vehicleColor;
        VehicleModel = vehicleModel;
        this.image = image;
    }



    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getVehicleColor() {
        return VehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        VehicleColor = vehicleColor;
    }

    public String getVehicleModel() {
        return VehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        VehicleModel = vehicleModel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

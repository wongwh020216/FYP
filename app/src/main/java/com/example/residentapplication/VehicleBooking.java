package com.example.residentapplication;

public class VehicleBooking {

    String id,model, plateNumber, date, time, price, selectedHour;

    public VehicleBooking() {
    }

    public VehicleBooking(String id,String model, String plateNumber, String date, String time, String price, String selectedHour) {
        this.id = id;
        this.model = model;
        this.plateNumber = plateNumber;
        this.date = date;
        this.time = time;
        this.price = price;
        this.selectedHour = selectedHour;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSelectedHour() {
        return selectedHour;
    }

    public void setSelectedHour(String selectedHour) {
        this.selectedHour = selectedHour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

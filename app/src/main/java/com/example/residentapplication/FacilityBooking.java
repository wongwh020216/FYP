package com.example.residentapplication;

public class FacilityBooking {
    String id,facilityName,date,time,selectedDuration,status;

    public FacilityBooking() {
    }

    public FacilityBooking(String id, String facilityName, String date, String time, String selectedDuration,String status) {
        this.id = id;
        this.facilityName = facilityName;
        this.date = date;
        this.time = time;
        this.selectedDuration = selectedDuration;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
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

    public String getSelectedDuration() {
        return selectedDuration;
    }

    public void setSelectedDuration(String selectedDuration) {
        this.selectedDuration = selectedDuration;
    }
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

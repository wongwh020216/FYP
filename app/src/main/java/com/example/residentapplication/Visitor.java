package com.example.residentapplication;

public class Visitor {
    String id, visitorName, visitorPhoneNUmber, purposeOfVisit, vehicleNumber, visitDate, endDate, type,status;

    public Visitor() {
    }

    public Visitor(String id,String visitorName, String visitorPhoneNUmber, String purposeOfVisit, String vehicleNumber, String visitDate, String endDate,String overnight,String status) {
        this.id = id;
        this.visitorName = visitorName;
        this.visitorPhoneNUmber = visitorPhoneNUmber;
        this.purposeOfVisit = purposeOfVisit;
        this.vehicleNumber = vehicleNumber;
        this.visitDate = visitDate;
        this.endDate = endDate;
        this.type = overnight;
        this.status = status;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorPhoneNUmber() {
        return visitorPhoneNUmber;
    }

    public void setVisitorPhoneNUmber(String visitorPhoneNUmber) {
        this.visitorPhoneNUmber = visitorPhoneNUmber;
    }

    public String getPurposeOfVisit() {
        return purposeOfVisit;
    }

    public void setPurposeOfVisit(String purposeOfVisit) {
        this.purposeOfVisit = purposeOfVisit;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

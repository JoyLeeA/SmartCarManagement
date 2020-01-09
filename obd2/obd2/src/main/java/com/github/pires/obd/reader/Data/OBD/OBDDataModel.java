package com.github.pires.obd.reader.Data.OBD;

public class OBDDataModel {

    private String rpm,voltage,distance,cool,date;
    private String speed,engineLoad,fuelLeft;

    public String getEngineLoad() {
        return engineLoad;
    }

    public void setEngineLoad(String engineLoad) {
        this.engineLoad = engineLoad;
    }

    public String getFuelLeft() {
        return fuelLeft;
    }

    public void setFuelLeft(String fuelLeft) {
        this.fuelLeft = fuelLeft;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }


    public String getRpm() {
        return rpm;
    }

    public void setRpm(String rpm) {
        this.rpm = rpm;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCoolantTemperature() {
        return cool;
    }

    public void setCoolantTemperature(String coolantTemperature) {
        this.cool = coolantTemperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

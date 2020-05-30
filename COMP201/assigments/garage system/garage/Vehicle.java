/*
 * COMP201 Assignment 2
 * vehicle.java
 * NAME:Pengcheng Jin
 * STUDENT ID:201447767
 * COMPUTER USERNAME:sgpjin4
 * 
 */
public class Vehicle {

    private String vehicleID;
    private String type;

    public Vehicle() {// constructor
    }

    public Vehicle(String vehicleID) {// constructor
    }

    public Vehicle(String vehicleID, String type) {// constructor
    }

    public String get_type() {
        return type;
    }

    public void set_type(String theType) {
        type = theType;
    }

    public String get_vehicleID() {
        return vehicleID;
    }

    public void set_vehicleID(String theVehicleID) {
        vehicleID = theVehicleID;
    }
}

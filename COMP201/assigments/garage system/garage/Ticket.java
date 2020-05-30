
/*
 * COMP201 Assignment 2
 * ticket.java
 * NAME:Pengcheng Jin
 * STUDENT ID:201447767
 * COMPUTER USERNAME:sgpjin4
 * 
 */
import java.util.Date;

public class Ticket {

    private Customer holder; // Customer Holder
    private String status;
    private Vehicle vehicleID;
    private String test;
    private Date deadline;
    private double price;
    private String notes;

    public Ticket() { // constructor

    }
    public Ticket(Customer theCustomer, String status, Vehicle vehicleID, String test, Date deadline, double price,
            String notes) { // constructor

    }

    public void setHolder(Customer theCustomer) {
        holder = theCustomer;
    }

    public Customer getHolder() {
        return holder;
    }

    public void setStatus(String theStatus) {
        status = theStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setVehicleID(Vehicle theVehicleID) {
        vehicleID = theVehicleID;
    }

    public Vehicle getVehicleID() {
        return vehicleID;
    }

    public void setTest(String theTest) {
        test = theTest;
    }

    public String getTest() {
        return test;
    }

    public void setDeadline(Date theDeadline) {
        deadline = theDeadline;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setPrice(double thePrice) {
        price = thePrice;
    }

    public double getPrice() {
        return price;
    }

    public void setNotes(String theNotes) {
        notes = theNotes;
    }

    public String getNotes() {
        return notes;
    }

}

/*
 * COMP201 Assignment 2
 * Manager.java
 * NAME:Pengcheng Jin
 * STUDENT ID:201447767
 * COMPUTER USERNAME:sgpjin4
 * 
 */
public class Manager extends Mechanic {

    public boolean checkStatus(Ticket theTicket) {//check status=check ticket
        return true;
    }

    public boolean checkQuality(Ticket theTicket) {
        return true;
    }

    public boolean updatePrice(Ticket theTicket) {
        return true;
    }

    public boolean updateStatus(Ticket theTicket) {//update status from check to signed off
        return true;
    }

}

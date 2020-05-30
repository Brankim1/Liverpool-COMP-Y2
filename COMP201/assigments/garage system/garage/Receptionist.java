/*
 * COMP201 Assignment 2
 * receptionist.java
 * NAME:Pengcheng Jin
 * STUDENT ID:201447767
 * COMPUTER USERNAME:sgpjin4
 * 
 */
import java.util.Date;
public class Receptionist extends Staff {

	public void create_Ticket(Customer theCustomer, String status, Vehicle vehicleID, String test, Date deadline,
			double price, String notes) {  //add data for each ticket

			//addTicket()	
	}

	public void addTicket(Ticket theTicket) {

		garge.tickets.add(theTicket);//add ticket to garage

	}
	public void addVehicle(String vehicleID, String type){//add vehicle for customer

	}
	public boolean checkStatus(Ticket theTicket) {//check signed off ticket
		return true;
	}

	public boolean updateStatus(Ticket theTicket) {//update ticket status from signed off to complete
		return true;
	}

	public boolean informCustomer(Ticket theTicket) {// call customer
		return true;
	}
}

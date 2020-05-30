/*
 * COMP201 Assignment 2
 * mechanic.java
 * NAME:Pengcheng Jin
 * STUDENT ID:201447767
 * COMPUTER USERNAME:sgpjin4
 * 
 */
public class Mechanic extends Staff {
    public boolean viewTicket(Ticket theTicket) {//find waiting ticket
        return true;
    }

    public boolean updateStatus(Ticket theTicket) {//update ticket status from waiting to progress
        return true;
    }

    public void inspectionTasks(Ticket theTicket) {//execute inspection Task
    String a =theTicket.getTest();
	if(a.equals("the MOT test")){}
	else if(a.equals("the general diagonstic test")){}

	}

    public void repairTasks(Ticket theTicket) {//execute repair Task
    
	String a =theTicket.getTest();
	if (a.equals("body repair")){}
	else if (a.equals("engine repair")){}
	else if (a.equals("window replacement")){}
	else if (a.equals("insurance mandated repair")){}}

    public void maintenanceTasks(Ticket theTicket) {//execute maintenance Task
    
	String a =theTicket.getTest();
	if (a.equals("air conditioning top-up")){}
	else if (a.equals("body respray")){}
	else if (a.equals("tyre change")){}}

    public boolean changeNotes(Ticket theTicket) {//change unexpected cost notes, notes in tickets
        return true;
    }

}

/*
 * COMP201 Assignment 2
 * customer.java
 * NAME:Pengcheng Jin
 * STUDENT ID:201447767
 * COMPUTER USERNAME:sgpjin4
 * 
 */
import java.util.Vector;
public class Customer {

    private String name;
    private String need;
    private String telephone;
    public Vector<Vehicle> vehicles;//vehicle vector, because one customer may have many vehicles

    public String getName() {
        return name;

    }

    public void setName(String theName) {
        name = theName;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String theNeed) {
        need = theNeed;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String theTelephone) {
        telephone = theTelephone;
    }

}
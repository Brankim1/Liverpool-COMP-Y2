/*
 * COMP201 Assignment 1
 * Person.java
 * NAME:
 * STUDENT ID:
 * COMPUTER USERNAME:
 * 
 */
 
 public class Person{
	 public String name;

     public Person()
     {
         name = new String("UNKNOWN");
     }

     public Person(String theName)
     {
         name = new String(theName);
         System.out.println("A person named " + name + " has been created.");
     }
};

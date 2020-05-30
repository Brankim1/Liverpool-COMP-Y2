/*
 * COMP201 2019-20
 * Assignment 1
 * CreditUnionUser.java
 * NAME:
 * STUDENT ID:
 * COMPUTER USERNAME:
 */
 
 
 //TODO: You must go through ALL CODE to ensure it meets requirements!!!
 
 public class CreditUnionUser{

	 public static void main(String[] args)
	 {
			//Create new Clerk
			Clerk clerk = new Clerk("Donald");
			
			//Create some new customers
			Customer customer1 = new Customer("Thomas");
			Customer customer2 = new Customer("Richard");
			Customer customer3 = new Customer("Harry");
			Customer customer4 = new Customer("Sally");
			
			//Create the credit union with opening capital
			CreditUnion cu = new CreditUnion(10000.00, clerk);
			System.out.println("Credit Union Capital is: " + cu.getTotalCapital(clerk));

			//TODO: Write code that tests the program. Use the code below to prompt your testing
			
			//Create some saving accounts (The Clerk must do this on behalf of the customer)

			clerk.openSavings(customer1, 300.0);
			System.out.println("Credit Union Capital is: " + cu.getTotalCapital(clerk));
			
			customer1.makeWithdrawal(10.0);
			System.out.println("Credit Union Capital is: " + cu.getTotalCapital(clerk));
			
			//Add balances to saving accounts (The customer does this)
			
			//Make some withdrawals from saving accounts (The customer does this)
			
			//Process a loan application (The clerk does this on behalf of the customer)
			
			//Make a payment to loan account (The customer does this)
			
			//Close loan account (The clerk does this)
			
			//Close saving accounts (The clerk does this)
			
			
	 } 
	 
};

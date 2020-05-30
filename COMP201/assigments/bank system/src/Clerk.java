/*
 * COMP201 2019-20
 * Assignment 1
 * Clerk.java *V2*
 * NAME:
 * STUDENT ID:
 * COMPUTER USERNAME:
 */
 
 public class Clerk extends Person{
	private CreditUnion union;
	 
	 public Clerk()
	 {
		 super(); 
	  }
	 
	 public Clerk(CreditUnion theUnion)
	 {
		 super();
		 union = theUnion;
	 }
	 
	 public Clerk(String theName)
	 {
			super(theName);
	 }
	 
	 public void setUnion(CreditUnion theUnion)
	 {
			union = theUnion;
	 }
	 
	 
	 public void addAccount(Account theAccount)
	{

		union.accounts.add(theAccount);
		
	}
	
	public void rmAccount(Account theAccount)
	{
			//TODO: Complete this method as opposite of addAccount(Account, Person)
	}
	
	public void addCustomer(Customer c)
	{
		union.people.add(c);
		System.out.println("Customer " + c.name + " is registered with the Credit Union");
	}
	
	public void rmCustomer(Customer c)
	{
		
	}
	 
	 public boolean openSavings(Customer theCustomer, double openAmount)
	 {
		 //Check the Customer is registered with the union
		 if(!(union.people.contains(theCustomer)))
		 {
			 addCustomer(theCustomer);
		 }
		 
			 //Check we are opening the account with a valid amount
			 if(openAmount >=0.0)
			 {
				 try
				 {
					//Create a Saving account for the customer with the openAmount
					Account theAccount = new Savings(openAmount, theCustomer); 
					addAccount(theAccount);
					return true;
				 }catch(AccountException e)
				 {
						System.out.println("Account Exception caught whilst opening a savings account");
				 }
				
			 }
		 
		 return false;
	 }
	 
	 public boolean openLoan(Customer theCustomer, double theAmount)
	 {
		 //TODO: Complete this method - use the Process Loan method to help
		 return true;
	 }
	 
	 public boolean closeAccount()
	 {
		 //TODO: Complete this method
		 return true;
	 }
	 
	 public boolean applyInterest()
	{	//TODO: Complete this method
		return true;
	}
};

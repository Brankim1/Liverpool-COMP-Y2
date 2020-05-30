/*
 * COMP201 Assignment 1
 * Loan.java
 * NAME:
 * STUDENT ID:
 * COMPUTER USERNAME:
 * NAME:
 * STUDENT ID:
 * COMPUTER USERNAME: 
 */
 
 public class Loan extends Account{
	 //Default constructor for Loan account - sets initial balance to 0
	public Loan()
	{
		super();
		setBalance(0.0);
	}
	
	public Loan(double amount) throws AccountException
	{
		if(amount > 0.0)
		{
			throw new AccountException("Cannot open a Loan account with positive balance: " + amount);
		}
		//TODO: Complete the opening of this Loan account
	}
	
	public void close() throws AccountException
	{
		if(getBalance() < 0.0)
		{
			throw new AccountException("Cannot close a Savings account with negative balance: " + getBalance());
		} else if (getBalance() > 0.0)
        {
            throw new AccountException("Cannot close a loan account with positive balance, this should not have happened, so an error has occured! Balance is: £" + getBalance());
        } else
		{
			System.out.println("Closing the account...");
			System.out.println("Please return £" + getBalance() + " to the customer.");
			System.out.println("Balance at account closure is now: £" + getBalance());
		}
	}
	
	/**
	 * Makes Payment into the loan account
	 * @return The new balance
	 * @param amount the amount to make payment of
	 */ 
	public double payment(double amount)
	{
		//TODO: Complete this method inline with reqs
		return getBalance();
	}
	
	public void withdraw(double amount) throws AccountException
	{
			throw new AccountException("...");
	}
};

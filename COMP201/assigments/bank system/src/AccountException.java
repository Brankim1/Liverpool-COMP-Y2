/*
* AccountException.java
 * NAME:
 * STUDENT ID:
 * COMPUTER USERNAME:
 * /
 
/** 
* A custom Exception type that is used for signalling an error
* relating to operations upon an account.
* This can be any operation that creates a disallowed state, eg:
* 	- Invalid Opening Balance
* 	- Customer attempting an operation that only a Clerk can do
* 	- Balance on savings account becoming negative
* 
*/
class AccountException extends Exception
{
	AccountException(String s)
	{
		super(s);
	}
}

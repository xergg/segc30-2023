package lib;

public class Account {

	private final String clientID;
	private double balance = 200;

	public Account( String clientID ) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance( double balance, boolean add ) {
		this.balance = add ? this.balance + balance : this.balance - balance;
	}

}

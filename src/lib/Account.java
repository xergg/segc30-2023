package lib;

import java.util.HashMap;
import java.util.Map;

public class Account {

	private final String clientID;
	private double balance = 200;
	Map <String, String> messages = new HashMap<String, String>();

	public Account( String clientID ) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	public double getBalance() {
		return balance;
	}

	public void getMessages(){
		//TODO
	}

	public void setBalance( double balance, boolean add ) {
		this.balance = add ? this.balance + balance : this.balance - balance;
	}

	public void addMessage (String username , String message){
		messages.put(username, message);
	}

}

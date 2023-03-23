package lib;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class Account that implements a Serializable. Has various methods that defines an Account such as clientID, it's balance, and many other
 * given account functionalities.
 */
public class Account implements Serializable{

	private final String clientID;
	private double balance = 200;
	private ArrayList<String> messageList = new ArrayList<String>();


	/**
	 * Basic constructor for an account, with a clientID.
	 * @param clientID given clientID
	 */
	public Account( String clientID ) {
		this.clientID = clientID;
	}

	/**
	 * Gets the clientID
	 * @return account clientID
	 */
	public String getClientID() {
		return clientID;
	}

	/**
	 * Gets the account balance
	 * @return account balance
	 */
	public double getBalance() {
		return balance;
	}


	/**
	 * Sets the account balance
	 * @param balance account balance
	 * @param add flag whether or not it's to add or subtract from the balance
	 */
	public void setBalance( double balance, boolean add ) {
		this.balance = add ? this.balance + balance : this.balance - balance;
	}


	/**
	 * Gets the message list 
	 * @return message list from the account
	 */
	public ArrayList<String> getMessageList(){
		return messageList;
	}

	/**
	 * Reads unread messages from the account and then deletes them once they've been read
	 * @return unread messages
	 */
	public String readMessages(){
		StringBuilder str = new StringBuilder();
		if(messageList.isEmpty())
			return "";
			
		for(String message : messageList){
			str.append(message + "\n");
		}
		messageList.clear();

		return str.toString();
	}


	/**
	 * adds message to messagelist
	 * @param message message to be added
	 */
	public void receiveMessage(String message) {
		this.messageList.add(message);
	}
}

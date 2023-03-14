package lib;
import java.util.ArrayList;

public class Account {

	private final String clientID;
	private double balance = 200;
	private ArrayList<String> messageList = new ArrayList<String>();

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

	public ArrayList<String> getMessageList(){
		return messageList;
	}

	public String readMessages(){
		StringBuilder str = new StringBuilder();
		for(String message : messageList){
			str.append(message + "\n");
			messageList.remove(message);
		}

		return str.toString();
	}

	public void receiveMessage(String message) {
		this.messageList.add(message);
	}
}

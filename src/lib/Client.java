package lib;

import java.util.ArrayList;

public class Client {
	
	private String username;
	private int balance;
	private ArrayList<Wine> wineList;
	
	public Client(String username) {
		this.username = username;
		this.balance = 200;
		this.wineList = new ArrayList<>();
	}	
	
	
}

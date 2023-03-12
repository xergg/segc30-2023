package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import lib.enums.Commands;

public class Tintolmarketskel {

	private String username;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private Socket socket;


	public Tintolmarketskel() {}

	public void connect(String username, String host, int tcpPort) throws UnknownHostException, IOException {
		this.socket = new Socket(host, tcpPort);
		inStream = new ObjectInputStream(this.socket.getInputStream() );
		outStream = new ObjectOutputStream(this.socket.getOutputStream() );
		this.username = username;
	}	

	public void close() throws IOException {
		outStream.close();
		inStream.close();
		socket.close();
	}

	public boolean login(String username, String password) throws IOException, ClassNotFoundException {
		//ClientID
		outStream.writeObject(username);

		//Password		
		outStream.writeObject(password);

		Commands auth = (Commands) inStream.readObject();
		
		return auth.equals(Commands.VALID_LOGIN);
	}

	public void add (String wineID, String filename) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.ADD);
		outStream.writeObject(username);
		outStream.writeObject(wineID);
		outStream.writeObject(filename);

		Commands message = (Commands) inStream.readObject();

		if ( message.equals( Commands.SUCCESS))
			System.out.println( "Wine sucessfully added" );
		else
			System.out.println( "Wine already exists!" );
	}

	public void sell (String wineID, int value, int quantity) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.SELL);

		outStream.writeObject(username);
		outStream.writeObject(wineID);
		outStream.writeObject(value);
		outStream.writeObject(quantity);

		Commands message = (Commands) inStream.readObject();

		if ( message.equals( Commands.SUCCESS))
			System.out.println( "It is on sale!" );
		else
			System.out.println( "The wine does not exist!" );
	}

	public void view (String wineID) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.VIEW);
		outStream.writeObject(wineID);

	}

	public void buy (String wineID, String seller, int quantity) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.BUY);
		outStream.writeObject(username);
		outStream.writeObject(wineID);
		outStream.writeObject(seller);
		outStream.writeObject(quantity);

		Commands message = (Commands) inStream.readObject();
		if ( message.equals( Commands.SUCCESS))
			System.out.println( "The wine was sucessfully bought!" );
		else if (message.equals( Commands.BALANCE_NOT_ENOUGH))
			System.out.println( "You don't have enough money to buy it!" );
		else if (message.equals(Commands.QUANTITY_NOT_ENOUGH))
			System.out.println( "That quantity is not available!" );
		else 
			System.out.println( "The wine does not exist!" );

	}
	

	public void wallet () throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.WALLET);
		outStream.writeObject(username);
		Commands message = (Commands) inStream.readObject();
		
	}
	


	public void classify (String wineID, int stars) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.WALLET);
		outStream.writeObject(wineID);
		outStream.writeObject(stars);
		Commands message = (Commands) inStream.readObject();
		
	}
	
	public void talk (String user, String msg) throws IOException, ClassNotFoundException {

		//Dunno ainda
		
	}
}

package client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.OptionalDouble;

import lib.AccountHandler;
import lib.Wine;
import lib.enums.Commands;
import lib.enums.Paths;
import lib.utils.Utils;

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

	public Commands login(String username, String password) throws IOException, ClassNotFoundException {
		//ClientID
		outStream.writeObject(username);

		//Password		
		outStream.writeObject(password);

		Commands auth = (Commands) inStream.readObject();
		return auth;
	}

	public void add (String wineID, String filename) throws IOException, ClassNotFoundException {

		File image = new File(filename);

		if(image.exists()){


			outStream.writeObject(Commands.ADD);
			outStream.writeObject(username);
			outStream.writeObject(wineID);
			
			
			Utils.sendFile(outStream, image);
	
			Commands message = (Commands) inStream.readObject();
			
			if ( message.equals( Commands.ERROR)){
				String error = (String) inStream.readObject();
				System.out.println( error );
			}
			else{
				System.out.println( "Wine sucessfully added");
			}
		} else {
			System.out.println("File image does not exist");
		}			
	}

	public void sell (String wineID, String valueS, String quantityS) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.SELL);
		outStream.writeObject(username);
		outStream.writeObject(wineID);
		Double value;
		int quantity;
		try {
			value = Double.parseDouble(valueS);
			quantity = Integer.parseInt(quantityS);
		} catch (NumberFormatException nfe) {
			value = -1.00;
			quantity = -1;
		}
		outStream.writeObject(value);
		outStream.writeObject(quantity);
		
		
		Commands message = (Commands) inStream.readObject();

		if ( message.equals( Commands.SUCCESS)){
			System.out.println( "It is on sale!" );
		}
		else{
			String error = (String) inStream.readObject();
			System.out.println(error);
		}
	}

	public void view (String wineID) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.VIEW);//1
		//2
		outStream.writeObject(wineID); 

		//3
		Commands message = (Commands) inStream.readObject();

		if (message.equals(Commands.ERROR)){
			String error = (String) inStream.readObject();
			System.out.println(error);
		}
		 
		else {

			//4
			byte[] buffer = Utils.receiveFile(inStream);

			//5
			String wineName = (String) inStream.readObject();
			Double wineRating = (Double) inStream.readObject();
			String sales = (String) inStream.readObject();

			String ImagePath = Paths.CLIENT_DIRECTORY.getPath() + '/' + username + '/' + wineName + ".png";
			Utils.createDirectories(Paths.CLIENT_DIRECTORY.getPath() + '/' + username );
			Utils.createFile(buffer, ImagePath);
				
			System.out.println("Wine:" + wineName);
			System.out.println("Image:" + ImagePath);
			if(wineRating == -1){
				System.out.println( "Classification: N/A");
			} else {
				System.out.println("Classification: " + wineRating);
			}
			System.out.println("Sales: \n" + sales);
		}
	}

	public void buy (String wineID, String seller, String quantityS) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.BUY);
		outStream.writeObject(username);
		outStream.writeObject(wineID);
		outStream.writeObject(seller);
		
		int quantity;
		
		try {
			quantity = Integer.parseInt(quantityS);
		} catch (NumberFormatException nfe) {
			quantity = -1;
		}

		outStream.writeObject(quantity);

		Commands message = (Commands) inStream.readObject();
		if ( message.equals( Commands.SUCCESS))
			System.out.println( "The wine was sucessfully bought!" );
		else if (message.equals( Commands.BALANCE_NOT_ENOUGH))
			System.out.println( "You don't have enough money to buy it!" );
		else if (message.equals(Commands.QUANTITY_NOT_ENOUGH))
			System.out.println( "That quantity is not available!" );
		else {
			String error = (String) inStream.readObject();
			System.out.println( error );
		}
			

	}
	

	public void wallet() throws IOException, ClassNotFoundException {
		outStream.writeObject(Commands.WALLET);
		outStream.writeObject(username);
		
		Commands message = (Commands) inStream.readObject();

		if(message.equals(Commands.ERROR)){
			
			String error = (String) inStream.readObject();
			System.out.println( error );

		} else {

			double balance = (double) inStream.readObject();
			System.out.println("The balance in your wallet is: " + balance);

		}
		
	}
	


	public void classify (String wineID, String starsS) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.CLASSIFY);
		outStream.writeObject(wineID);
		int stars;
		try {
			stars = Integer.parseInt(starsS);
	    } catch (NumberFormatException nfe) {
			stars = -1;
		}
		outStream.writeObject(stars);

		Commands message = (Commands) inStream.readObject();

		if(message.equals(Commands.ERROR)){
			
			String error = (String) inStream.readObject();
			System.out.println( error );

		} else {

			System.out.println("The wine " + wineID + " has been successfully classified with " + stars + " stars!");

		}

		
	}
	public void talk (String user, String msg) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.TALK);
		outStream.writeObject(username);
		outStream.writeObject(user);
		outStream.writeObject(msg);
		

		Commands message = (Commands) inStream.readObject();

		if(message.equals(Commands.ERROR)){
			
			String error = (String) inStream.readObject();
			System.out.println( error );
		
		}

		else {
			System.out.println("Message sent");

		}
	}

	public void read () throws IOException, ClassNotFoundException{
	
		outStream.writeObject(Commands.READ);
		outStream.writeObject(username);

		Commands message = (Commands) inStream.readObject();

		if(message.equals(Commands.ERROR)){

			String error = (String) inStream.readObject();
			System.out.println( error );

		}

		else {
			
			String messagesToRead = (String) inStream.readObject();
			System.out.println("messages:" + messagesToRead);
		}
	
	}


}
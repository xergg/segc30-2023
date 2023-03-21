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

		outStream.writeObject(Commands.ADD);
		outStream.writeObject(username);
		outStream.writeObject(wineID);
		
		File image = new File(filename);
		Utils.sendFile(outStream, image);

		Commands message = (Commands) inStream.readObject();
		
		if ( message.equals( Commands.ERROR))
			System.out.println( "Wine already exists!" );
		else
			System.out.println( "Wine sucessfully added");
	}

	public void sell (String wineID, String value, String quantity) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.SELL);
		outStream.writeObject(username);
		outStream.writeObject(wineID);
		outStream.writeObject(Double.parseDouble(value));
		outStream.writeObject(Integer.parseInt(quantity));
		
		Commands message = (Commands) inStream.readObject();

		if ( message.equals( Commands.SUCCESS))
			System.out.println( "It is on sale!" );
		else
			System.out.println( "The wine does not exist!" );
	}

	public void view (String wineID) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.VIEW);

		outStream.writeObject(wineID); 

		byte[] buffer = Utils.receiveFile(inStream);
		
		Commands message = (Commands) inStream.readObject();

		if (message.equals(Commands.ERROR)){
			//falta receber mensagem de erro
			System.out.println("Wine not Found");
		}
		 
		else {
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

	public void buy (String wineID, String seller, String quantity) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.BUY);
		outStream.writeObject(username);
		outStream.writeObject(wineID);
		outStream.writeObject(seller);
		outStream.writeObject(Integer.parseInt(quantity));

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
	

	public void wallet() throws IOException, ClassNotFoundException {
		outStream.writeObject(Commands.WALLET);
		outStream.writeObject(username);
		
		Commands message = (Commands) inStream.readObject();

		if(message.equals(Commands.ERROR)){
			//falta receber mensagem de erro
			System.out.println("There was an error checking your wallet.");
		
		} else {
			double balance = (double) inStream.readObject();
			System.out.println("The balance in your wallet is: " + balance);

		}
		
	}
	


	public void classify (String wineID, String starsS) throws IOException, ClassNotFoundException {

		outStream.writeObject(Commands.CLASSIFY);
		outStream.writeObject(wineID);
		int stars = Integer.parseInt(starsS);
		outStream.writeObject(stars);

		Commands message = (Commands) inStream.readObject();

		if(message.equals(Commands.ERROR)){
			//falta receber mensagem de erro
			System.out.println("There was an error classifying the wine.");


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
			System.out.println("There was an issue starting a conversation with the user.");
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
			System.out.println("There was an error retrieving your messages.");
		}

		else {
			
			String messagesToRead = (String) inStream.readObject();
			System.out.println("messages:" + messagesToRead);
		}
	
	}


}
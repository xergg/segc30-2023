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

/**
 * Client side of Tintolmarket, with most of the fucntion for it, hence the skel name.
 */
public class Tintolmarketskel {

	private String username;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private Socket socket;

	/**
	 * constructor
	 */
	public Tintolmarketskel() {}


	/**
	 * Connects the user to the server
	 * @param username given user id
	 * @param host given host
	 * @param tcpPort given tcp port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connect(String username, String host, int tcpPort) throws UnknownHostException, IOException {
		this.socket = new Socket(host, tcpPort);
		inStream = new ObjectInputStream(this.socket.getInputStream() );
		outStream = new ObjectOutputStream(this.socket.getOutputStream() );
		this.username = username;
	}	

	/**
	 * Closes connection to the server
	 * @throws IOException
	 */
	public void close() throws IOException {
		outStream.close();
		inStream.close();
		socket.close();
	}

	/**
	 * Logs in the user given an username and a password
	 * @param username given username
	 * @param password given password
	 * @return true if authenticated, false otherwise
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Commands login(String username, String password) throws IOException, ClassNotFoundException {
		//ClientID
		outStream.writeObject(username);

		//Password		
		outStream.writeObject(password);

		Commands auth = (Commands) inStream.readObject();
		return auth;
	}


	/**
	 * Adds a wine given a wine id and an image
	 * @param wineID given wine id
	 * @param filename given image
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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


	/**
	 * Makes a listing for a sale given a wine id, a value for the wine and the stock for it
	 * @param wineID given wine id
	 * @param valueS given sale wine value
	 * @param quantityS given quantity wine value
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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

	/**
	 * View all sales, classification given a wine id
	 * @param wineID given wine id
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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


	/**
	 * Given a wine id, and a seller, buy that wine from the seller
	 * @param wineID given id
	 * @param seller given seller
	 * @param quantityS given quantity to buy
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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
	
	/**
	 * Checks the user's balance
	 */
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
	

	/**
	 * Classifies a given wine with the stars to be given
	 * @param wineID given wine id
	 * @param starsS given stars to classify the wine
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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

	/**
	 * Send a message to another user
	 * @param user given user id to send the message to
	 * @param msg given message to send
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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


	/**
	 * Read all unread messages
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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
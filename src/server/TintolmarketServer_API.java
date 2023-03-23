package server;

import static lib.utils.Utils.invokeMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.login.AccountNotFoundException;

import exceptions.*;
import lib.AccountHandler;
import lib.SaleHandler;
import lib.Wine;
import lib.WineHandler;
import lib.enums.Commands;
import lib.enums.Paths;
import lib.utils.Utils;

/**
 * Server's API. Most methods for our server are here.
 */
public class TintolmarketServer_API {

	private static final File uFile = new File("users.txt");

	private final ObjectInput inStream;
	private final ObjectOutput outStream;

	/**
	 * Basic constructor with a socket so input and output stream can be used
	 * @param socket given socket
	 * @throws IOException
	 */
	public TintolmarketServer_API(Socket socket) throws IOException {
		outStream = new ObjectOutputStream(socket.getOutputStream());
		inStream = new ObjectInputStream(socket.getInputStream());
	}

	/**
	 * Authentication method to login our client.
	 * @return client id if authenticated, "" otherwise
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public String authentication() throws IOException, ClassNotFoundException{

		uFile.createNewFile();

		String clientID = inStream.readObject().toString();
		String password = inStream.readObject().toString();

		boolean clientAccess = login(clientID, password);
		Commands ct = clientAccess? Commands.VALID_LOGIN : Commands.INVALID_LOGIN;
		outStream.writeObject(ct);

		return clientAccess ? clientID : "";
	}

	/**
	 * Saves a client and its password to a file
	 * @param currentClient current client id
	 * @param password current client password
	 */
	public static void saveToFile(String currentClient, String password) {
		String saveU = currentClient + "/" + password + "\n";

		try {

			FileOutputStream file = new FileOutputStream(uFile , true);
			// get the content in bytes
			byte[] contentInBytes = saveU.getBytes();
			file.write(contentInBytes);
			file.write('\n');
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Functions so that the client can login
	 * @param clientID given client id
	 * @param password given client password
	 * @return true if the client is registered and the password is correct
	 * @throws IOException
	 */
	private boolean login(String clientID, String password) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(uFile));
		boolean registered = false;
		boolean passCorrect = false;
		String line;

		while((line = br.readLine()) != null) {
			String[] id = line.split("/");
			if(id[0].equals(clientID)) {
				registered = true;
				passCorrect = id[1].equals(password);
			}
		}

		br.close();

		if(!registered) {
			saveToFile(clientID, password);
			AccountHandler.addAccount(clientID);
		}
		
		return registered ? passCorrect : true;
	}

	/**
	 * Invokes the methods from a map
	 * @param skelmethods map with the methods
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void invoke(Map<String, Method> skelmethods) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException {
		Commands command = (Commands) inStream.readObject();

		if(command.equals(Commands.QUIT)) {
			System.out.println("Client quitted!");
		}

		invokeMethod(this, skelmethods, command);
	}
	
	/**
	 * Adds wine to the server with an image, given a user id.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void add () throws ClassNotFoundException, IOException  {

		String error = "" ;
		String userID = (String) inStream.readObject();
		String wineID = (String) inStream.readObject();
		boolean operationSuccessful = false;

		try {
			//Falta verificar se ja existe o vinho antes de transferir a imagem
			AccountHandler.checkValid(userID);

			UUID imageId = UUID.randomUUID();
			
			String ImagePath = Paths.IMAGES.getPath() + "/" + imageId + ".png";

			byte[] buffer = Utils.receiveFile( inStream );
			
			Utils.createFile( buffer, ImagePath );

			WineHandler.create(userID, wineID, ImagePath);
			operationSuccessful = true;


		} catch (AccountNotFoundException e) {
		
			error = "User acount not Found ";
		
		} catch (NullArgumentException e){
		
			error = "Invalid argument";
		
		} catch (WineAlreadyExistsException e) {
		
			error = "This Wine is already in the system, therefore it cannot be added";
		
		} catch (IOException e) {
		
			error = "There has been an error creating the wine image file";
		} 
	
		if (!operationSuccessful) {
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);
		}
		else {
			outStream.writeObject(Commands.SUCCESS);
		}

	}
	
	/**
	 * Given a a seller, and a wine id, posts a listing for the selling of the wine.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void sell () throws IOException, ClassNotFoundException {

		String error = "";
		String userID = (String) inStream.readObject();
		String wineID = (String) inStream.readObject();
		double value = (double) inStream.readObject();
		int quantity = (int) inStream.readObject();

		try {
			AccountHandler.checkValid(userID);
			SaleHandler.addStock(wineID, value, userID, quantity);
			outStream.writeObject(Commands.SUCCESS);

		} catch (AccountNotFoundException e) {
		
			outStream.writeObject(Commands.ERROR);
			error = "User acount not Found ";
			outStream.writeObject(error);
		
		} catch (WineDoesNotExistException e) {
		
			outStream.writeObject(Commands.ERROR);
			error = "Selected wine does not exist";
			outStream.writeObject(error);
		
		} catch (WineNotFoundException e) {
			
			outStream.writeObject(Commands.ERROR);
			error = "Selected wine does not exist";
			outStream.writeObject(error);
		
		} catch (NullArgumentException e) {
			
			outStream.writeObject(Commands.ERROR);
			error = "Invalid/Null value inserted";
			outStream.writeObject(error);
		
		} catch (InvalidQuantityPriceException e) {
			
			outStream.writeObject(Commands.ERROR);
			error = "Price/Quantity cant be 0 , negative or a non digit";
			outStream.writeObject(error);
		} 
	}
	
	/**
	 * Checks the wine and all its characteristics such as name, average rating, sales.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void view () throws IOException, ClassNotFoundException {
		//1
		String error = "";
		boolean operationSuccessful = false;
		//2
		String wineID = (String) inStream.readObject();

		try {

			Wine wine = WineHandler.getWine(wineID);
			//3
			outStream.writeObject(Commands.SUCCESS);

			File image = new File(wine.getImage());
			
			//4
			Utils.sendFile(outStream, image);

			//5
			outStream.writeObject(wine.getName());
			outStream.writeObject(wine.getRating());
			outStream.writeObject(wine.getAllSales());

			operationSuccessful = true;

		} catch (WineNotFoundException e) {
		
			error = "Selected wine does not exist";
		
		} catch (NullArgumentException e) {
		
			error = "Invalid/Null value inserted";
		
		} catch (IOException e){

			error = "there was an error sending the image file";

		} 

		if(!operationSuccessful){
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);
		}

	}
	
	/**
	 * Given a buyer, a seller and a wine, the buyer makes a purchase from the seller for the specific wine.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void buy () throws IOException, ClassNotFoundException {
		String error = "";
		String userID = (String) inStream.readObject();
		String wineID = (String) inStream.readObject();
		String seller = (String) inStream.readObject();
		int quantity = (int) inStream.readObject();
		

		try {
			
			AccountHandler.checkValid(userID);
			AccountHandler.checkValid(seller);
			SaleHandler.buy(userID, seller, wineID, quantity);
			outStream.writeObject(Commands.SUCCESS);
			
		} catch (AccountNotFoundException e) {
			
			outStream.writeObject(Commands.ERROR);
			error = "User/Seller account not Found ";
			outStream.writeObject(error);

		} catch (WineNotFoundException e) {
			
			error = "Selected wine does not exist";
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);

		} catch (NullArgumentException e) {

			error = "Invalid/Null value inserted";
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);
			
	
		} catch (NotEnoughQuantitiesException e) {
		
			outStream.writeObject(Commands.QUANTITY_NOT_ENOUGH);
		
		} catch (NotEnoughMoneyException e) {
		
			outStream.writeObject(Commands.BALANCE_NOT_ENOUGH);
		
		} catch (SameUserException e) {
			
			error = "Seller and Buyer cant be the same person";
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);
		
		} catch (InvalidQuantityPriceException e) {

			outStream.writeObject(Commands.ERROR);
			error = "Quantity cant be 0 , negative or non digit";
			outStream.writeObject(error);

		}		
	}

	/**
	 * Checks the user's balance.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void wallet () throws IOException, ClassNotFoundException {
		String error = "";
		String userID = (String) inStream.readObject();
		boolean operationSuccessful = false;



		try {
			AccountHandler.checkValid(userID);
			operationSuccessful = true;

		} catch (AccountNotFoundException e) {
	
			error = "User account not Found ";

		}
		
		double balance = AccountHandler.getBalance(userID);

		if(!operationSuccessful){
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);
		} else {
			outStream.writeObject(Commands.SUCCESS);
			outStream.writeObject(balance);
		}
			
	}

	/**
	 * Classifies a given wine.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void classify () throws IOException, ClassNotFoundException {

		String error = "Wine can only be classified between 0 and 5 stars";
		String wineID = (String) inStream.readObject();
		int stars = (int) inStream.readObject();
		boolean operationSuccessful = false;

		try {
			if (stars<=5 && stars>=0){
				
				WineHandler.classify(wineID, stars);

				operationSuccessful = true;
			}

		} catch (WineNotFoundException e ){
			
			error = "Selected wine does not exist";

		} catch (NullArgumentException e1){
			
			error = "Invalid/Null value inserted";
		
		}

		if(!operationSuccessful){
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);
		} else {
			outStream.writeObject(Commands.SUCCESS);
		}


	}
	//precisa verificar
	/**
	 * Sends a message to another user
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void talk () throws IOException, ClassNotFoundException {

		String error = "";
		boolean operationSuccessful = false;
		String userID = (String) inStream.readObject();
		String user = (String) inStream.readObject();
		String msg = (String) inStream.readObject(); 

		try{
			AccountHandler.checkValid(userID);
			AccountHandler.checkValid(user);
			
			if (!userID.equals(user)){
				AccountHandler.talk(user, userID + ":" + msg);
				outStream.writeObject(Commands.MESSAGE_SENT);	
				operationSuccessful = true;
			} else {
				throw new SameUserException();
			}

		} catch (AccountNotFoundException e){
			
			error = "User account not Found ";

		} catch (SameUserException e) {
			
			error = "You cant send a message to yourself";
		}

		if(!operationSuccessful){
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);
		}

	}
	/**
	 * Reads all the unread messages that were sent to the user.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void read () throws IOException, ClassNotFoundException{
		
		String error = "";
		boolean operationSuccessful = false;
		//verificar primeiro se os users sao validos
		String userID = (String) inStream.readObject();

		try{
			AccountHandler.checkValid(userID);


			String messages = AccountHandler.read(userID);

			outStream.writeObject(Commands.SUCCESS);
			outStream.writeObject(messages);
			operationSuccessful = true;

		//Nunca deve acontecer
		} catch (AccountNotFoundException e){
			
			error = "User account not Found ";
			
		}

		if(!operationSuccessful){
			outStream.writeObject(Commands.ERROR);
			outStream.writeObject(error);
		}

	}
}

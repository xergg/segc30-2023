package server;

import static lib.utils.Utils.invokeMethod;

import java.io.BufferedReader;
import java.io.File;
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
import lib.utils.Utils;


public class TintolmarketServer_API {

	private static File uFile;

	private final ObjectInput inStream;
	private final ObjectOutput outStream;

	public TintolmarketServer_API(Socket socket) throws IOException {
		outStream = new ObjectOutputStream(socket.getOutputStream());
		inStream = new ObjectInputStream(socket.getInputStream());
	}

	public String authentication() throws IOException, ClassNotFoundException{

		uFile = new File ("users.txt");

		if(!uFile.exists())
			uFile.createNewFile();

		String clientID = inStream.readObject().toString();
		String password = inStream.readObject().toString();

		boolean clientAccess = login(clientID, password);
		Commands ct = clientAccess? Commands.VALID_LOGIN : Commands.INVALID_LOGIN;
		outStream.writeObject(ct);

		return clientAccess ? clientID : "";
	}

	public static void saveToFile(String currentClient, String password) {
		String saveU = currentClient + "/" + password + "\n";

		try {

			FileOutputStream file = new FileOutputStream(uFile);
			// get the content in bytes
			byte[] contentInBytes = saveU.getBytes();
			file.write(contentInBytes);
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
			outStream.writeObject(Commands.SUCCESS);
		} else if (!passCorrect)
			outStream.writeObject(Commands.INVALID_LOGIN);
		else
			outStream.writeObject(Commands.VALID_LOGIN);
		
		return registered ? passCorrect : true;
	}

	public void invoke(Map<String, Method> skelmethods) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException {
		Commands command = (Commands) inStream.readObject();

		if(command.equals(Commands.QUIT)) {
			System.out.println("Client quitted!");
		}

		invokeMethod(this, skelmethods, command);
	}

	public void add () throws ClassNotFoundException, IOException  {

		String userID = (String) inStream.readObject();
		String wineID = (String) inStream.readObject();
		boolean operationSuccessful = false;

		try {
			
			AccountHandler.checkValid(userID);

			UUID imageId = UUID.randomUUID();
			
			String ImagePath = "./data/serverImages/" + imageId + ".png";

			byte[] buffer = Utils.receiveFile( inStream );
			Utils.createFile( buffer, ImagePath );

			WineHandler.create(userID, wineID, ImagePath);
			operationSuccessful = true;


		} catch (AccountNotFoundException e) {
			e.printStackTrace();
		} catch (NullArgumentException e2){
			e2.printStackTrace();
		} catch (WineAlreadyExistsException e3) {
			e3.printStackTrace();
		} catch (IOException e4) {
			e4.printStackTrace();
		}
		
		if (operationSuccessful) {
			outStream.writeObject(Commands.SUCCESS);
		} 
		else {
			outStream.writeObject(Commands.ERROR);
		}

	}

	public void sell () throws IOException, ClassNotFoundException, NullArgumentException {

		String userID = (String) inStream.readObject();
		String wineID = (String) inStream.readObject();
		int value = (int) inStream.readObject();
		int quantity = (int) inStream.readObject();

		try {

			AccountHandler.checkValid(userID);
			SaleHandler.addStock(wineID, value, userID, quantity);

		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WineDoesNotExistException e) {
			e.printStackTrace();
			outStream.writeObject(Commands.ERROR);
		} catch (WineNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outStream.writeObject(Commands.ERROR);
		} 

		outStream.writeObject(Commands.SUCCESS);

	}

	public void view () throws IOException, ClassNotFoundException {

		boolean operationSuccessful = false;

		String wineID = (String) inStream.readObject();

		try {

			Wine wine = WineHandler.getWine(wineID);

			outStream.writeObject(Commands.SUCCESS);
			outStream.writeObject(wine);

			operationSuccessful = true;

		} catch (WineNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(!operationSuccessful)
			outStream.writeObject(Commands.ERROR);

	}

	public void buy () throws IOException, ClassNotFoundException {

		String userID = (String) inStream.readObject();
		String wineID = (String) inStream.readObject();
		String seller = (String) inStream.readObject();
		int quantity = (int) inStream.readObject();


		try {
			
			AccountHandler.checkValid(userID);
			AccountHandler.checkValid(seller);
			SaleHandler.buy(userID, seller, wineID, quantity);
			
		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WineNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outStream.writeObject(Commands.ERROR);
		} catch (NullArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughQuantitiesException e) {
			e.printStackTrace();
			outStream.writeObject(Commands.QUANTITY_NOT_ENOUGH);
		} catch (NotEnoughMoneyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			outStream.writeObject(Commands.BALANCE_NOT_ENOUGH);
		}		
	}


	public void wallet () throws IOException, ClassNotFoundException {

		String userID = (String) inStream.readObject();
		
		try {
			AccountHandler.checkValid(userID);
		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double balance = AccountHandler.getBalance(userID);
		System.out.print("API" + balance);
		outStream.writeObject(balance);
	}


	public void classify () throws IOException, ClassNotFoundException {

		String wineID = (String) inStream.readObject();
		int stars = (int) inStream.readObject();
		boolean operationSuccessful = false;

		try {

			WineHandler.classify(wineID, stars);

			operationSuccessful = true;

		} catch (WineNotFoundException e ){
			e.printStackTrace();
		} catch (NullArgumentException e1){
			e1.printStackTrace();
		}

		if(operationSuccessful){
			outStream.writeObject(Commands.SUCCESS);
		} else {
			outStream.writeObject(Commands.ERROR);
		}


	}

	public void talk () throws IOException, ClassNotFoundException {

		//verificar primeiro se os users sao validos
		String userID = (String) inStream.readObject();
		String user = (String) inStream.readObject(); //o user a que vai ser enviado

		try{
			AccountHandler.checkValid(userID);
			AccountHandler.checkValid(user);

			String msg = (String) inStream.readObject();
			AccountHandler.talk(user, userID + ":" + msg);
			outStream.writeObject(Commands.MESSAGE_SENT);	

		} catch (AccountNotFoundException e){
			e.printStackTrace();
		}

	}

	public void read () throws IOException, ClassNotFoundException{

		//verificar primeiro se os users sao validos
		String userID = (String) inStream.readObject();

		try{
			AccountHandler.checkValid(userID);

			String messages = AccountHandler.read(userID);

			outStream.writeObject(Commands.SUCCESS);
			outStream.writeObject(messages);
			
		} catch (AccountNotFoundException e){
			e.printStackTrace();
		}

	}
}

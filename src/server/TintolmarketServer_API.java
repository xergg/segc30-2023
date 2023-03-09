package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lib.Commands;
import lib.Message;

public class TintolmarketServer_API {

	private static File uFile;

	public static String authentication(ObjectOutputStream outStream, ObjectInputStream inStream)
			throws IOException, ClassNotFoundException{

		uFile = new File ("users.txt");

		if(!uFile.exists())
			uFile.createNewFile();

		String clientID = inStream.readObject().toString();
		String password = inStream.readObject().toString();

		boolean clientAccess = login(clientID, password);
		Message m = clientAccess? new Message(Commands.VALID_LOGIN, "Valid login") : 
			new Message(Commands.INVALID_LOGIN, "Invalid login");
		outStream.writeObject(m);

		return clientAccess ? clientID : "";
	}

	public static void saveToFile(String currentClient, String password) {
		String saveU = currentClient + "/" + password + "/n";

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

	private static boolean login(String clientID, String password) throws IOException {
		String saveU = clientID + "/" + password + "/n";
		BufferedReader br = new BufferedReader(new FileReader(uFile));
		boolean registered = false;
		boolean passCorrect = false;
		String line;

		while((line = br.readLine()) != null) {
			String[] id = line.split("/");
			if(id[0].equals(clientID)) {
				registered = true;
				passCorrect = id[1].equals(password)? true : false;
			}
		}

		if(!registered)
			saveToFile(clientID, password);

		return registered ? passCorrect : true;
	}

	public static Object add(String string, String string2) {
		return null;
	}

	public static Object sell(String string, double parseDouble, double parseDouble2) {
		return null;
	}

	public static Object view(String string) {
		return null;
	}

	public static Object buy(String string, double parseDouble, double parseDouble2) {
		return null;
	}

	public static Object wallet() {
		return null;
	}





}

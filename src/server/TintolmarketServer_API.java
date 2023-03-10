package server;

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
import java.util.Map;

import lib.enums.Commands;
import static lib.Utils.invokeMethod;


public class TintolmarketServer_API {

	private static File uFile;

	private final ObjectInput inStream;
	private final ObjectOutput outStream;

	public TintolmarketServer_API( ObjectInput inStream, ObjectOutput outStream )
	{
		this.inStream = inStream;
		this.outStream = outStream;
	}

	public String authentication(ObjectOutputStream outStream, ObjectInputStream inStream)
			throws IOException, ClassNotFoundException{

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
		
		br.close();
		
		if(!registered)
			saveToFile(clientID, password);

		return registered ? passCorrect : true;
	}

	public void invoke(ObjectInputStream inStream, ObjectOutputStream outStream, Map<String, Method> skelmethods) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, IOException {
		Commands command = (Commands) inStream.readObject();

		if(command.equals(Commands.QUIT)) {
			System.out.println("Client quitted!");
		}

		invokeMethod(this, skelmethods, command);
	}





}

package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lib.Commands;
import lib.Message;

public class TintolmarketServer_API {
	
	private static File uFile;

    public static void authentication(ObjectOutputStream outStream, ObjectInputStream inStream)
    throws IOException, ClassNotFoundException{

    	uFile = new File ("users.txt");
        
        if(!uFile.exists())
            uFile.createNewFile();

        String clientID = inStream.readObject().toString();
        String password = inStream.readObject().toString();
        Message m;
        m = login(clientID, password) ? new Message(Commands.VALID_LOGIN, "Valid login") : new Message(Commands.INVALID_LOGIN, "Invalid login");
        outStream.writeObject(m);
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

	private static boolean login(String clientID, String password) {
		StringBuilder sb = new StringBuilder();
		String saveU = clientID + "/" + password + "/n";
	    
		try {
			  //creating FileInputStream object.
			  FileInputStream file = new FileInputStream(uFile);
			  int i;
			  //read file.
			  while((i = file.read())!=-1)
				sb.append((char)i);
			  
		} catch(Exception e) {
			e.printStackTrace();
		}			
		
		return sb.toString().contains(saveU);
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

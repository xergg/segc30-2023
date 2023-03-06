package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TintolmarketServer_API {

    public static String authentication(ObjectOutputStream outStream, ObjectInputStream inStream)
    throws IOException, ClassNotFoundException{

        File uFile = new File ("users.txt");
        
        if(!uFile.exists()){
            uFile.createNewFile();
        }

        String clientID = inStream.readObject().toString();


        outStream.writeObject("Authenticated");
		return new String(clientID);
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

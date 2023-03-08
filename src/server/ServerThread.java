package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ServerThread extends Thread {

	private Socket socket = null;

	ServerThread(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run(){

		try{

			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			
			//AUTHENTICATION
	        if(!TintolmarketServer_API.authentication(outStream, inStream))
	        	return;
	        
			String[] commandArray = new String().split(" ");
			switch (commandArray[0]){

				case "a":
				case "add":
					outStream.writeObject(TintolmarketServer_API.add(commandArray[1],commandArray[2]));
				break;
			
				case "s":
				case "sell":
					outStream.writeObject(TintolmarketServer_API.sell(commandArray[1], Double.parseDouble(commandArray[2]), Double.parseDouble(commandArray[3]) ));
		
				case "v":
				case "view":
					outStream.writeObject(TintolmarketServer_API.view(commandArray[1]));
	
				case "b":
				case "buy":
					outStream.writeObject(TintolmarketServer_API.buy(commandArray[1], Double.parseDouble(commandArray[2]), Double.parseDouble(commandArray[3])));

				case "w":
				case "wallet":
					outStream.writeObject(TintolmarketServer_API.wallet());
			}
	} catch(Exception e) {
		e.getStackTrace();
	}

	}
}
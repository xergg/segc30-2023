package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import lib.Commands;
import lib.Message;

public class Tintolmarket {
	
	static ObjectOutputStream out;
	static ObjectInputStream in;
	static String username;
	
	public static void main(String[] args) {

		if(args.length < 4) {
			System.out.println( "Wrong number of arguments");
			System.out.println("Example: Tintolmarket <serverAddress> <userID> [password]\r\n");
			System.exit(-1);
		}
		
		Socket socket = null;

		//default
		int tcpPort = 12345;
		String[] hostPort = args[0].split(":");
		String host = hostPort[0];
		
		if(hostPort.length == 2)
			tcpPort = Integer.parseInt(hostPort[1]);
		
		try {
			socket =  new Socket(host, tcpPort); 
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			System.out.println("Connected to server");

			//ClientID
			username = args[2];
			Scanner sc = new Scanner(System.in);
			out.writeObject(username);
			Message auth = (Message) in.readObject();
			
			if (auth.getType() == Commands.VALID_LOGIN) {
				
			} else if(auth.getType() == Commands.INVALID_LOGIN) {
				
			}
			
		} catch() {
			
		}

	}

}

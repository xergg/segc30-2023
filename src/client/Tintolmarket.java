package client;

import java.io.IOException;
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
				System.out.println(auth.getMessage());
				System.out.println(menuToString());
			} else if(auth.getType() == Commands.INVALID_LOGIN) {
				System.err.println(auth.getMessage());
				System.exit(-1);
			}
			
			String command = null;

			while(!(command = sc.nextLine()).equals("quit")) {
				readCommand(command);
			}

			sc.close();
			
		} catch(IOException e) {
			System.err.println("Error communicating with server");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void readCommand(String command) {
		
		String[] commandArray = command.split(" ");
		
		try {

			switch (commandArray[0]) {

			case "add":
				break;
			case "sell":
				break;
			case "view":
				break;
			case "buy":
				break;
			case "wallet":
				break;
			case "classify":
				break;
			case "talk":
				break;
			case "read":
				break;
		
	}

	
	private static String menuToString() {
		return "Available operations: \n"+
				"- add <wine> <image> \n" + 
				"- sell <wine> <value> <quantity>\n" + 
				"- view <wine> \n" + 
				"- buy <wine> <seller> <quantity> \n" + 
				"- wallet \n" + 
				"- classify <wine> <stars>  \n" + 
				"- talk <user> <message> \n" + 
				"- read - \n" +
				"- quit\n";
	}
}

package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import lib.Commands;
import lib.Message;

public class Tintolmarket {
	
	private static final String WRONG_NUMBER_OF_ARGUMENTS = "Wrong number of arguments";
	static ObjectOutputStream out;
	static ObjectInputStream in;
	static String username;
	static String password;
	
	public static void main(String[] args) {

		if(args.length < 2) {
			System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
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
			out.writeObject(username);
			
			//Password
			Scanner sc = new Scanner(System.in);
			if(args.length < 4) {
				System.out.println("Please insert Password: ");
				password = sc.nextLine();
			} else 
				password = args[3];			
			out.writeObject(password);
	
			
			Message auth = (Message) in.readObject();
			
			if (auth.getType() == Commands.VALID_LOGIN) {
				System.out.println(auth.getMessage());
				System.out.println(menuToString());
			} else if(auth.getType() == Commands.INVALID_LOGIN) {
				System.err.println(auth.getMessage());
				System.exit(-1);
			}
			
			String command = null;

			while(!(command = sc.nextLine()).equals("quit")) 
				readCommand(command);
			
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
				if(commandArray.length != 3) {
					System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
					System.out.println("Example: add <wine> <image>");
					return;
				}
				break;

			case "sell":
				if(commandArray.length != 4){
					System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
					System.out.println("Example : sell <wine> <value> <quantity>");
				}
				break;

			case "view":
				if(commandArray.length != 2){
					System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
					System.out.println("Example: view <wine>");
				}
				break;

			case "buy":
			if(commandArray.length != 4){
				System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
				System.out.println("Example: buy <wine> <seller> <quantity>");
			}
				break;

			case "wallet":
			// no arguments other than wallet or w, therefore should redirect to menu
			if(commandArray.length != 1){
				System.out.println("Please type out a command.");
			}
				break;

			case "classify":
			if(commandArray.length != 3){
				System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
				System.out.println("Example: classify <wine> <stars>");
			}
				break;

			case "talk":
			if(commandArray.length != 3){
				System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
				System.out.println("Example: talk <user> <message>");
			}
				break;
				
			case "read":
			// no arguments other than read or r, therefore should redirect to menu
			if(commandArray.length != 1){
				System.out.println("Please type out a command.");
			}
				break;
			}
	} catch (Exception e){
		System.out.println(e.getMessage());
	}
	
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

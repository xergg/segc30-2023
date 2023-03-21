package client;

import static lib.utils.Utils.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import lib.enums.Commands;

public class Tintolmarket {

	private static final String WRONG_NUMBER_OF_ARGUMENTS = "Wrong number of arguments";
	private static final Tintolmarketskel client = new Tintolmarketskel();
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		String password = null ;
		String username = args[1] ;

		if(args.length == 2){
			System.out.println("Insert a password:");
			password = sc.nextLine();
		}
		else if(args.length != 3) {
			System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
			System.out.println("Example: Tintolmarket <serverAddress> <userID> [password]\r\n");
			System.exit(-1);
		} 
		else 
			password = args[2];
		

		if(username.matches(".*[\\\\/:*?\"<>|].*")){
			System.err.println("\nInvalid username. Characters \"\\ / : * ? \" < > | \" not allowed");
			System.exit(-1);
		}

		//default
		int tcpPort = 12345;
		String host = args[0];
		
		if ( args[0].contains( ":" ) ) {
            String[] hostPort = args[0].split( ":" );
            host = hostPort[0];
            
            tcpPort = Integer.parseInt( hostPort[1] );
            
            if ( tcpPort <= 0 ) {
                System.out.println("Port is not valid");
                System.exit( -1 );
            }
        }

		try {

			client.connect(username, host, tcpPort);
			System.out.println("Connected to server");
			Commands validLogin = client.login(username, password);
			
			if (validLogin.equals(Commands.VALID_LOGIN)) 
				System.out.println("Login sucessful!");
			else if (validLogin.equals(Commands.SUCCESS))
				System.out.println("You just entered the list!");
			 else {
				System.err.println("Login failed!");
				System.exit(-1);
			}
			
			String command = "";
			
			Method [] methods = Tintolmarketskel.class.getMethods();
			Map <String, Method> stubMethodsMap = new HashMap<>();
			Arrays.stream( methods ).forEach( m -> stubMethodsMap.put( m.getName(), m));

			while(!(command.equals("quit"))) {
				System.out.println(menuToString());
				System.out.printf( "\nInsert command: " );
				command = sc.nextLine();
				readCommand(command, stubMethodsMap);
			}
			System.out.print("Client ended connection");
			sc.close();
		} catch(IOException e) {
			System.err.println("Error communicating with server");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	private static void readCommand(String command, Map<String, Method> stubMethodsMap) {
		
		if(command.equals("quit"))
			return;
		String[] commandArray = command.split(" ", 2);
		Commands commandEnum = Commands.valueOfType(commandArray[0]);
		String[] commandArgs = new String[0];
		
		if(commandArray.length > 1)
			commandArgs = commandArray[1].split( "[\\s](?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)" );
		try {
			invokeMethod(client, stubMethodsMap, commandEnum, commandArgs, true);			
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
				"- read \n" +
				"- quit\n";
	}
}
		
package client;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import lib.enums.Commands;
import static lib.Utils.*;

public class Tintolmarket {

	private static final String WRONG_NUMBER_OF_ARGUMENTS = "Wrong number of arguments";
	private static final Tintolmarketskel client = new Tintolmarketskel();

	public static void main(String[] args) {

		if(args.length < 2) {
			System.out.println(WRONG_NUMBER_OF_ARGUMENTS);
			System.out.println("Example: Tintolmarket <serverAddress> <userID> [password]\r\n");
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

			client.connect(host, tcpPort);
			
			System.out.println("Connected to server");

			boolean validLogin = client.login(args);
			
			if (validLogin) {
				System.out.println("Login sucessful!");
				System.out.println(menuToString());
			} else {
				System.err.println("Login failed!");
				System.exit(-1);
			}
			
			String command = null;
			Scanner sc = new Scanner(System.in);
			
			Method [] methods = Tintolmarketskel.class.getMethods();
			Map <String, Method> stubMethodsMap = new HashMap<>();
			Arrays.stream( methods ).forEach( m -> stubMethodsMap.put( m.getName(), m));

			while(!(command = sc.nextLine()).equals("quit")) {
				System.out.println( "\nInsert command: " );
				readCommand(command, stubMethodsMap);
			}

			sc.close();

			
			
			System.out.println("Connected to server");

		} catch(IOException e) {
			System.err.println("Error communicating with server");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	private static void readCommand(String command, Map<String, Method> stubMethodsMap) {

		String[] commandArray = command.split(" ", 3);
		Commands commandEnum = Commands.valueOfType(commandArray[0]);

		try {
			invokeMethod(client, stubMethodsMap, commandEnum, commandArray, true );			
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
		
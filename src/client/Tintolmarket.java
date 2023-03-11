package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import lib.Client;
import lib.enums.Commands;
import static lib.Utils.*;

public class Tintolmarket {

	private static final String WRONG_NUMBER_OF_ARGUMENTS = "Wrong number of arguments";
	static ObjectOutputStream out;
	static ObjectInputStream in;
	static String username;

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
			username = args[1];	

			if(username.matches(".*[\\\\/:*?\"<>|].*")){
				System.err.println("\nInvalid username. Characters \"\\ / : * ? \" < > | \" not allowed");
				System.exit(-1);
			}

			out.writeObject(username);
			String password;

			//Password
			Scanner sc = new Scanner(System.in);
			if(args.length < 4) {
				System.out.println("Please insert Password: ");
				password = sc.nextLine();
			} else 
				password = args[3];			
			out.writeObject(password);

			Commands auth = (Commands) in.readObject();

			if (auth.equals(Commands.VALID_LOGIN)) {
				System.out.println(auth.getMethodName());
				System.out.println(menuToString());
			} else if(auth.equals(Commands.INVALID_LOGIN)) {
				System.err.println(auth.getMethodName());
				System.exit(-1);
			}

			String command = null;

			while(!(command = sc.nextLine()).equals("quit")) {
				System.out.println( "\nInsert command: " );
				readCommand(username, command);
			}

			sc.close();

		} catch(IOException e) {
			System.err.println("Error communicating with server");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void readCommand(String useranme, String command) {

		String[] commandArray = command.split(" ", 3);

		// Colocamos os metodos do client num mapa, em que cada key e' o nome do metodo em string.
		Method [] methods = Client.class.getMethods();
		Map <String, Method> stubMethodsMap = new HashMap<>();
		Arrays.stream( methods ).forEach( m -> stubMethodsMap.put( m.getName(), m));
        Commands commandEnum = Commands.valueOfType( commandArray[0] );

		try {
			invokeMethod( new Client(username), stubMethodsMap, commandEnum, commandArray, true );			
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

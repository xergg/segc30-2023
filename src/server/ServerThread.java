package server;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ServerThread extends Thread {

	private final Socket socket;
	private static final Map<String, Method> skelMethodsMap;

	static {
		// Colocamos os metodos do skel num mapa, em que cada key e' o nome do metodo em string.
		Method [] methods = TintolmarketServer_API.class.getMethods();
		skelMethodsMap = new HashMap<>();
		Arrays.stream( methods ).forEach( m -> skelMethodsMap.put( m.getName(), m ) );
	}

	ServerThread(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run(){

		try{
			// Criamos um skel do server para a thread com as streams definidas acima
			TintolmarketServer_API serverSkel = new TintolmarketServer_API(socket);

			//AUTHENTICATION
			String clientID = serverSkel.authentication();
			if(clientID.isEmpty())	
				System.out.println( "Client " + clientID + " login sucessful!" );

			while(true) 
				serverSkel.invoke(skelMethodsMap);

		} catch(Exception e) {
			e.getStackTrace();
		}

	}
}
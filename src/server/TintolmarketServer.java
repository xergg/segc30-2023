package server;

import java.io.IOException;
import java.net.ServerSocket;

public class TintolmarketServer {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Wrong number of arguments");
			System.out.println("Example: TintolmarketServer <port>");
			System.exit(-1);
		}

		System.out.println("Server iniciated");
		TintolmarketServer server = new TintolmarketServer();
		server.startServer(Integer.parseInt(args[0]));
	}

	private void startServer(int port) {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);	

		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		//listen to clients
		while(true) {
			try {
				new ServerThread(serverSocket.accept()).start();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

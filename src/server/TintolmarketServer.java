package server;

import java.io.IOException;
import java.net.ServerSocket;

import lib.enums.Paths;
import lib.utils.Utils;

/**
 * Class that iniciates the server for Tintolmarket.
 */
public class TintolmarketServer {

	static
    {
        Utils.createDirectories(Paths.IMAGES.getPath());
		Utils.createDirectories(Paths.CLIENT_DIRECTORY.getPath());
    }
	public static void main(String[] args) {
		
		int port = 12345 ;

		if (args.length == 1){
			port = Integer.parseInt(args[0]);
		} 
		else if (args.length == 0){
			port = 12345;
		}
		else {
			System.out.println("Wrong number of arguments");
			System.out.println("Example: TintolmarketServer <port>");
			System.exit(-1);
		}

		System.out.println("Server iniciated");
		TintolmarketServer server = new TintolmarketServer();
		server.startServer( port );
	}

	/**
	 * starts our server with the given port, usually 12345
	 * @param port
	 */
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

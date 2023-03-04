package server;

import java.net.Socket;


public class ServerThread extends Thread {

	private Socket socket = null;

	ServerThread(Socket clientSocket) {
		socket = clientSocket;
	}
}
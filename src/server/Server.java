package server;

import entity.Account;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dailiwen
 * @date 2018/06/17
 */
public class Server {
	public static Map<String, Account> accounts = new HashMap<>();
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8080);
		while (true) {
			Socket socket = serverSocket.accept();
			SocketHandler socketHandler = new SocketHandler(socket);
			Thread thread = new Thread(socketHandler);
			thread.start();
		}
	}
}

import java.io.*;
import java.net.*;

// TODO: Auto-generated Javadoc
/**
 * The Class WebServer.
 */
public class WebServer {

	/** The server socket. */
	public static ServerSocket serverSocket;

	/**
	 * Instantiates a new web server.
	 *
	 * @throws Exception the exception
	 */
	public WebServer() throws Exception {

		while (true) {
			Socket socket;

			try {
				socket = serverSocket.accept();
				HttpRequest request = new HttpRequest(socket);
				Thread thread = new Thread(request);
				thread.start();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Initialising Server...");
		serverSocket = new ServerSocket(6789);
		System.out.println("Server initialised on Port 6789");
		new WebServer();

	}
}
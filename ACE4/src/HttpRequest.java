import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

// TODO: Auto-generated Javadoc
/**
 * The Class HttpRequest.
 */
public class HttpRequest implements Runnable {

	/** The Constant CRLF. */
	final static String CRLF = "\r\n";
	
	/** The is. */
	public InputStream IS = null;
	
	/** The dos. */
	public DataOutputStream DOS = null;
	
	/** The socket. */
	Socket socket;

	/**
	 * Instantiates a new http request.
	 *
	 * @param socket the socket
	 * @throws Exception the exception
	 */
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Process request.
	 *
	 * @throws Exception the exception
	 */
	private void processRequest() throws Exception {

		IS = socket.getInputStream();
		DOS = new DataOutputStream(socket.getOutputStream());

		BufferedReader br = new BufferedReader(new InputStreamReader(IS));

		String requestLine = br.readLine();

		System.out.println(requestLine);

		String headerLine = null;
		while ((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		}
		
		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken();
		String fileName = tokens.nextToken();
		
		fileName = "." + fileName;
		
		FileInputStream FIS = null;
		boolean fileExists = true;
		try {
		 FIS = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
		 fileExists = false;
		}
		
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if (fileExists) {
		 statusLine = "200" + CRLF;
		 contentTypeLine = "Content-type: " +
		 contentType( fileName ) + CRLF;
		} else {
		 statusLine = "404 Not Found" + CRLF;
		 contentTypeLine = "text/html" + CRLF;
		 entityBody = "<HTML>" +
		 "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
		 "<BODY>Not Found</BODY></HTML>";
		}
		
		DOS.writeBytes(statusLine);
		
		DOS.writeBytes(contentTypeLine);
		
		DOS.writeBytes(CRLF);
		
		if (fileExists) {
			 sendBytes(FIS, DOS);
			 FIS.close();
			} else {
			 DOS.writeBytes(entityBody);
			}
		
		DOS.close();
		br.close();
		socket.close();
	}

	/**
	 * Send bytes.
	 *
	 * @param fis the fis
	 * @param os the os
	 * @throws Exception the exception
	 */
	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
		// Construct a 1K buffer to hold bytes on their way to the socket.
		byte[] buffer = new byte[1024];
		int bytes = 0;
		// Copy requested file into the socket's output stream.
		while ((bytes = fis.read(buffer)) != -1) {
			os.write(buffer, 0, bytes);
		}
	}

	/**
	 * Content type.
	 *
	 * @param fileName the file name
	 * @return the string
	 */
	private static String contentType(String fileName)
	{
	 if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
	 return "text/html";
	 }
	 if(fileName.endsWith(".gif")) {
	 return "image/gif";
	 }
	 if(fileName.endsWith(".jpeg")) {
	 return "image/jpeg.";
	 }
	 return "application/octet-stream";
	}

}

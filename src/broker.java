import java.net.*;
import java.util.*;

public class broker {
	public static List<MyFile> fileList = new ArrayList<MyFile>();
	
	public static void main(String[] args) throws Exception{
		ServerSocket serverSocket = new ServerSocket(1234);
		
		while(true) {
			Socket clientSocket = serverSocket.accept();
			clientMonitorThread cmt = new clientMonitorThread(clientSocket);
			Thread T = new Thread(cmt);
			T.start();
		}
	}
}

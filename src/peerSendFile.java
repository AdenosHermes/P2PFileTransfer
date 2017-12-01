import java.util.*;
import java.net.*;
import java.io.*;

public class peerSendFile implements Runnable {

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(5678);
			while(true) {
				Socket receiveFileSocket = serverSocket.accept();
				InputStream ris = receiveFileSocket.getInputStream();
				ObjectInputStream rois = new ObjectInputStream(ris);
				String fileName = (String) rois.readObject();
				File file = new File(fileName);
				
				OutputStream ros = receiveFileSocket.getOutputStream();
				ObjectOutputStream roos = new ObjectOutputStream(ros);
				roos.writeObject(file);
				roos.flush();
				
			}
		} catch (Exception E) {
			System.out.println(E);
		}

	}

}

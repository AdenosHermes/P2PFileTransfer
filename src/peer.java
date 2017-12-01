import java.util.*;
import java.net.*;
import java.io.*;

public class peer {
	public static void main(String[] args) {
		try {
			Thread T = new Thread(new peerSendFile());
			T.start();
			
			
			Socket clientSocket = new Socket("localhost", 1234);
			Scanner scan = new Scanner(System.in);
			System.out.println("Welcome to use P2PFileTransfer!");
			System.out.println("Commands: \n R: register a new file \n U: unregister a file \n S: search for and get a file \n E: exit");
			
			OutputStream os = clientSocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			InputStream is = clientSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			while (true) {
				System.out.println("Waiting for command...");
				String line = scan.nextLine().toUpperCase();
				if (line.equals("R")) {
					oos.writeObject("R");
					oos.flush();
					
					System.out.println("Enter the file name:");
					String fileName = scan.nextLine();
					while (!new File(fileName).exists()) {
						System.out.println("File does not exist in the current folder!");
						System.out.println("Please check and enter the file name:");
						fileName = scan.nextLine();
					}
					oos.writeObject(fileName);
					oos.flush();
					
					String reply = (String) ois.readObject();
					System.out.println(reply);
				} else if (line.equals("U")) {
					oos.writeObject("U");
					oos.flush();
					System.out.println("Enter the file name:");
					String fileName = scan.nextLine();
					oos.writeObject(fileName);
					oos.flush();
					
					String reply = (String) ois.readObject();
					System.out.println(reply);
				} else if (line.equals("S")) {
					oos.writeObject("S");
					oos.flush();
					System.out.println("Enter the file name:");
					String fileName = scan.nextLine();
					oos.writeObject(fileName);
					oos.flush();
					
					String reply = (String) ois.readObject();
					System.out.println(reply);
					if (reply.startsWith("E")) {
						InetAddress hostIP = (InetAddress) ois.readObject();
						int hostPort = (int) ois.readObject();
						
						Socket receiveFileSocket = new Socket(hostIP, 5678);
						OutputStream ros = receiveFileSocket.getOutputStream();
						ObjectOutputStream roos = new ObjectOutputStream(ros);
						roos.writeObject(fileName);
						roos.flush();
						InputStream ris = receiveFileSocket.getInputStream();
						ObjectInputStream rois = new ObjectInputStream(ris);
						byte[] data = (byte[]) rois.readObject();
						OutputStream out = new FileOutputStream(new File(fileName));
						out.write(data);
						out.close();
						
						
						System.out.println("Successfully acquired " + fileName);
					}
				} else if (line.equals("E")) {
					oos.writeObject("E");
					oos.flush();
					System.out.println("Thank you for using P2PFileTransfer!");
					break;
				}
				
			}
			clientSocket.close();
			scan.close();
		} catch (Exception E) {
			System.out.println(E);
		}
	}
}

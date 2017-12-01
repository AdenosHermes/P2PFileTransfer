import java.net.*;
import java.io.*;

public class clientMonitorThread implements Runnable{
	private Socket clientSocket;
	
	public clientMonitorThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		try {
			InetAddress ip = clientSocket.getInetAddress();
			int port = clientSocket.getPort();
		
			InputStream is = clientSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			OutputStream os = clientSocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			while (true) {
				String request = (String) ois.readObject();
				if (request.equals("R")) {
					String fileName = (String) ois.readObject();
					MyFile file = new MyFile(fileName, ip, port);
					
					boolean existed = false;
					for (int i = 0; i < broker.fileList.size(); i++) {
						if (broker.fileList.get(i).getFileName().equals(fileName)) {
							existed = true;
							oos.writeObject("Register Fail! File already registered.");
							oos.flush();
						}
					}
					if (!existed) {
						oos.writeObject("File Registered");
						broker.fileList.add(file);
						System.out.println("Client " + ip + ":" + port + " registered " + fileName);
					}
				} else if (request.equals("U")) {
					String fileName = (String) ois.readObject();
					boolean existed = false;
					for (int i = 0; i < broker.fileList.size(); i++) {
						if (broker.fileList.get(i).getFileName().equals(fileName)) {
							existed = true;
							broker.fileList.remove(i);
							oos.writeObject("You have successfully unregistered " + fileName);
							oos.flush();
							System.out.println("Client " + ip + ":" + port + " unregistered " + fileName);
						}
					}
					if (!existed) {
						oos.writeObject("Unregister fail! The file is not registered.");
						oos.flush();
					}
				} else if (request.equals("S")) {
					String fileName = (String) ois.readObject();
					boolean existed = false;
					for (int i = 0; i < broker.fileList.size(); i++) {
						if (broker.fileList.get(i).getFileName().equals(fileName)) {
							existed = true;
							InetAddress hostIP = broker.fileList.get(i).getIP();
							int hostPort = broker.fileList.get(i).getPort();
							oos.writeObject("Establishing connection with host...");
							oos.flush();
							oos.writeObject(hostIP);
							oos.flush();
							oos.writeObject(hostPort);
							oos.flush();
							System.out.println("Client " + ip + ":" + port + " requested " + fileName);
						}
					}
					if (!existed) {
						oos.writeObject("Sorry. The requested file does not exist.");
						oos.flush();
					}
				} else if (request.equals("E")) {
					System.out.println("Client " + ip + ":" + port + " is now disconnected.");
					clientSocket.close();
					break;
				}
				
			}
		} catch (Exception E) {
			System.out.println(E);
		}
	}
	
}

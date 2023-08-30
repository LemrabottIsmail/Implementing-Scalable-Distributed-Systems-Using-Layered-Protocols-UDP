package Assig1_Part2;
import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

public class loadbalancer {
	public static void main(String args[]) {
		DatagramSocket aSocket = null;
		// args[0] = localhost
		// args[1] = 30000
		// args[2] = 3 (Number of servers)
		try {
			aSocket = new DatagramSocket(20000);
			byte[] buffer = new byte[1000];
			InetAddress serverIP = InetAddress.getByName(args[0]);
            int serverPort = Integer.parseInt(args[1]);
            int numberOfServers = Integer.parseInt(args[2]);
			System.out.println("Load Balancer is ready and accepting clients' requests ... ");
			while(true) {
				for (int i=0; i<numberOfServers; i++) {
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					String msg = new String(request.getData(), 0, request.getLength());
					msg += request.getAddress() + "/" + request.getPort();
					DatagramPacket request2 = new DatagramPacket(msg.getBytes(), msg.length(), serverIP, serverPort+i); //
					aSocket.send(request2);
				}
				
			}
			
			
		}catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
	 	}catch (IOException e) {System.out.println("Error IO: " + e.getMessage());
		}finally {
			if(aSocket != null) aSocket.close();
		}
	}
	
}
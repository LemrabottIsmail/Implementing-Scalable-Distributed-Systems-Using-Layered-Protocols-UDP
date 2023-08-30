package Assig1_Part2;

import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.*;

public class Server_Part2 {
	static String[] arrayLetters = {"0","1","9","a","b","c","^","r","s","t","~","&","%"};
	public static void main(String args[]) {
		DatagramSocket aSocket = null;
		try {
			long total = 0;
        	long startTime = 0;
        	// For server 1
        	// args[0] = 30000 , args[1] = 1
        	// For server 2
        	// args[0] = 30001 , args[1] = 2
        	// For server 3
        	// args[0] = 30002 , args[1] = 3 
			aSocket = new DatagramSocket(Integer.parseInt(args[0]));
			byte[] buffer = new byte[1000];
			System.out.println("Server "+args[1]+" is ready and accepting clients' requests ... " );  
			while(true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String str = new String(request.getData(), 0, request.getLength());
				StringTokenizer tokenizer = new StringTokenizer(str, "/");
				String hash = tokenizer.nextToken();
				String ipAddress = tokenizer.nextToken();
				String port = tokenizer.nextToken();
				InetAddress serverIP = InetAddress.getByName(ipAddress);
				int serverPort = Integer.parseInt(port);
				System.out.println("Received hash is: " + hash);
				startTime = System.currentTimeMillis();
				ArrayList<String> results = functions.generatePasswords(arrayLetters,"", 6);
				String password = functions.compareHashes(results, hash);
				String msg = "From Server " + (Integer.parseInt(args[1])) + password;
				long elapsedTime = System.currentTimeMillis() - startTime;
				msg += "/" + elapsedTime/1000;
				DatagramPacket reply1 = new DatagramPacket(msg.getBytes(), msg.length(), serverIP, serverPort);
				aSocket.send(reply1);
		
			}
			
			
		}catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
	 	}catch (IOException e) {System.out.println("Error IO: " + e.getMessage());
		}finally {
			if(aSocket != null) aSocket.close();
		}
	}
	
}
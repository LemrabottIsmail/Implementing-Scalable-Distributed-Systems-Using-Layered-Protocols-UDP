import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.*;

public class Server_Part1 {
	static String[] arrayLetters =  {"0","1","9","a","b","c","^",
    		"r","s","t","~","&","%"};
	
	public static void main(String args[]) {
		DatagramSocket aSocket = null;
		try {
        	long startTime = 0;
			aSocket = new DatagramSocket(25000);
			byte[] buffer = new byte[1000];
			System.out.println("Server is ready and accepting clients' requests ... ");
			while(true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String str = new String(request.getData(), 0, request.getLength());
				System.out.println("Received Message is: " + str);
				// Calculate the password cracking
				startTime = System.currentTimeMillis();
				// Generate an ArrayList of possible passwords
				ArrayList<String> results = functions.generatePasswords(arrayLetters,"", 6);
				String password = functions.compareHashes(results, str);
				long elapsedTime = System.currentTimeMillis() - startTime;
				// Attach the time to the reply
				password += "/" + elapsedTime/1000;
				DatagramPacket reply1 = new DatagramPacket(password.getBytes(), password.length(), request.getAddress(), request.getPort());
				aSocket.send(reply1);
		
			}
			
			
		}catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
	 	}catch (IOException e) {System.out.println("Error IO: " + e.getMessage());
		}finally {
			if(aSocket != null) aSocket.close();
		}
	}
	
}
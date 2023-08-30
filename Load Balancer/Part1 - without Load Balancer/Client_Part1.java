import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.io.*;

public class Client_Part1{    

    public static void main(String args[]) {  
        DatagramSocket aSocket = null;
        // args[0] = localhost
        // args[1] = 25000
        try {
        	aSocket = new DatagramSocket();
        	long total = 0; 
        	int number0fPassowrds = 3;
        	int lengthOfPasswords = 6;
            // generate 100 6-character passwords
        	ArrayList<String> results = functions.getRandPasswords(number0fPassowrds, lengthOfPasswords);
        	System.out.println("Generated random passwords are: " + results);
        	System.out.println("Number of passwords randomly generated: " + number0fPassowrds);
        	System.out.println("Length of passwords randomly generated: " + lengthOfPasswords);
        	System.out.println("----------------------------------------------------------------------------------------------------");
        	// Send requests based on the number of passwords in the ArrayList
        	for (int i = 0; i<results.size(); i++) {
        		System.out.println("New Generated Password is: " + results.get(i));
        		String password = functions.applySha256(results.get(i));
        		byte [] m = password.getBytes();
                InetAddress server1IP = InetAddress.getByName(args[0]);
                int server1Port = Integer.parseInt(args[1]);
                DatagramPacket request1 = new DatagramPacket(m, password.length(), server1IP, server1Port);
                aSocket.send(request1);
        	    byte[] buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
           
            	aSocket.receive(reply);
            	String receivedReplay = new String(reply.getData(), 0, reply.getLength());
            	//split the received data to extract the time
            	StringTokenizer tokenizer = new StringTokenizer(receivedReplay, "/");
            	String msg = tokenizer.nextToken();
				String time = tokenizer.nextToken();
				long elapsedTime = Long.parseLong(time);
                total += elapsedTime;
                System.out.println("Elapsed time To crack the password in seconds is: " + elapsedTime + "secs");
                System.out.println("Client "+(i+1)+" Received Reply " + (i+1) + ": " + msg);
                System.out.println("----------------------------------------------------------------------------------------------------");
        	}
        	
            System.out.println("Total Time Measured is: " + total + "secs");

        	
        }catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("Error IO: " + e.getMessage());
        }finally { 
            if(aSocket != null) aSocket.close();
        }
        
    }
}
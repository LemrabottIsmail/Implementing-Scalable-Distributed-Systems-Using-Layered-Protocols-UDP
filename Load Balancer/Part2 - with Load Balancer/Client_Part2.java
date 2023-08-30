package Assig1_Part2;

import java.net.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.io.*;

public class Client_Part2{    

    public static void main(String args[]) {  
        DatagramSocket aSocket = null;
        // args[0] = localhost
        // args[1] = 20000
     // args[2] = client ID , for example 1
        try {
        	long startTime = 0;
        	aSocket = new DatagramSocket();
        	long total = 0;
        	int number0fPassowrds = 9;
        	int lengthOfPasswords = 6;
        	ArrayList<String> results = functions.getRandPasswords(number0fPassowrds, lengthOfPasswords);
        	System.out.println("Generated random passwords are: " + results);
        	System.out.println("Number of passwords randomly generated: " + number0fPassowrds);
        	System.out.println("Length of passwords randomly generated: " + lengthOfPasswords);
        	System.out.println("----------------------------------------------------------------------------------------------------");
        	for (int i = 0; i<results.size(); i++) {
        		System.out.println("New Generated Password is: " + results.get(i));
        		String password = functions.applySha256(results.get(i));
        		byte [] m = password.getBytes();
                InetAddress server1IP = InetAddress.getByName(args[0]);
                int server1Port = Integer.parseInt(args[1]);
                DatagramPacket request1 = new DatagramPacket(m, password.length(), server1IP, server1Port);
                aSocket.send(request1);
        	}
        	startTime = System.currentTimeMillis();
        	for (int i = 0; i<results.size(); i++) {
        	    byte[] buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
           
            	aSocket.receive(reply);
            	String receivedReplay = new String(reply.getData(), 0, reply.getLength());
            	StringTokenizer tokenizer = new StringTokenizer(receivedReplay, "/");
            	String msg = tokenizer.nextToken();
				String time = tokenizer.nextToken();
				long elapsedTime = Long.parseLong(time);
                total += elapsedTime;
                System.out.println("Elapsed time To crack the password in seconds is: " + elapsedTime + "secs");
                System.out.println("Client "+(Integer.parseInt(args[2])+i)+" Received Reply " + (i+1) + ": " + msg);
                System.out.println("----------------------------------------------------------------------------------------------------");
        	}
        	long elapsedTime = System.currentTimeMillis() - startTime;
        	System.out.println("Total time to finish all the requests is: " + elapsedTime/1000 + "secs");
        	
        }catch (SocketException e){System.out.println("Error Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("Error IO: " + e.getMessage());
        }finally { 
            if(aSocket != null) aSocket.close();
        }
        
    }
}
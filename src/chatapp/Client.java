package chatapp;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;;

public class Client {

	public Client(){
		
	}
	
	public void run() throws JSONException{
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		Socket clientSocket;
		String response;
		
		try {
			clientSocket = new Socket("localhost", 10221);
			DataOutputStream outbound = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inbound = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			JSONObject message = new JSONObject();
			message.put("UserID", new String("Danny"));
			message.put("message", input);
			outbound.writeBytes(message.toString() + '\n');
			response = inbound.readLine();
			System.out.println(response);
			clientSocket.close();
		} catch (UnknownHostException e) {
			System.out.println("Server Unavailable!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Unable to Open Socket!");
			e.printStackTrace();
		}
		
		scanner.close();
	}
}

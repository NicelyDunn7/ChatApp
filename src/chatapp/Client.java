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
		try {
			Socket clientSocket = new Socket("localhost", 10221);
			DataOutputStream outbound = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inbound = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			JSONObject message;
			Scanner scanner = new Scanner(System.in);
			String input;
			String response;
			String[] words;
			User user = null;
			
			while(true){ 
				input = scanner.nextLine();
				words = input.split(" ", 2);
				message = new JSONObject();
				
				if (words[0].equals("send")){
					if(user != null){
						message.put("Type", "message");
						message.put("UserID", user.getUserID());
						message.put("message", words[1]);
						outbound.writeBytes(message.toString() + '\n');
						response = inbound.readLine();
						System.out.println(response);
					} else {
						System.out.println("Server: Denied. Please login first.");
					}
				} else if (words[0].equals("newuser")){
					String[] credentials = words[1].split(" ");
					message.put("Type", "newuser");
					message.put("UserID", credentials[0]);
					message.put("Password", credentials[1]);
					outbound.writeBytes(message.toString() + '\n');
					response = inbound.readLine();
					System.out.println(response);
					if(response.equals("Server: " + credentials[0] + " created account")){
						user = new User(credentials[0], credentials[1], true);
					}
				} else if (words[0].equals("login")){
					String[] credentials = words[1].split(" ");
					message.put("Type", "login");
					message.put("UserID", credentials[0]);
					message.put("Password", credentials[1]);
					outbound.writeBytes(message.toString() + '\n');
					response = inbound.readLine();
					System.out.println(response);
					if(response.split(" ")[2].equals("joins")){
						user = new User(credentials[0], credentials[1], true);
					}
				} else if (words[0].equals("logout")){
					message.put("Type", "logout");
					message.put("UserID", user.getUserID());
					outbound.writeBytes(message.toString() + '\n');
					response = inbound.readLine();
					System.out.println(response);
					clientSocket.close();
					scanner.close();
					break;
				} else {
					System.out.println("Error: Invalid Input.");
					continue;
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("Server Unavailable!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Unable to Open Socket!");
			e.printStackTrace();
		}
	}
}

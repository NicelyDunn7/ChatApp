package chatapp;

import java.io.*;
import java.net.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Server {

	public Server(){
		
	}
	
	public void run() throws JSONException{
		String clientMessage;
		try {
			ServerSocket serverSocket = new ServerSocket(10221);
			boolean go = true;
			while (go){
				Socket connectSocket = serverSocket.accept();
				BufferedReader inbound = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
				DataOutputStream outbound = new DataOutputStream(connectSocket.getOutputStream());
				clientMessage = inbound.readLine();
				JSONObject message = new JSONObject(clientMessage);
				String response = message.getString("UserID") + ": " + message.getString("message");
				System.out.println(response);
				outbound.writeBytes(response + "\n");
			}
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error Opening Socket!");
			e.printStackTrace();
		}
	}
}
